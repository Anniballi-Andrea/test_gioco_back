package test_gioco.demo.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.map.Deposit;
import test_gioco.demo.classes.map.MapGrid;
import test_gioco.demo.classes.map.Portal;
import test_gioco.demo.classes.map.Tile;
import test_gioco.demo.enums.ResourceType;
import test_gioco.demo.enums.TerrainType;

@Service
public class MapGeneratorService {

    private static final int WIDTH = 65;
    private static final int HEIGHT = 65;
    private static final int TOTAL_TILES = WIDTH * HEIGHT;

    private static final double SHALLOW_WATER_PERCENT = 0.15;
    private static final double MOUNTAIN_PERCENT = 0.20;
    private static final double FOREST_PERCENT = 0.15;
    private static final double HILL_PERCENT = 0.15;

    private final Random random = new Random();

    private Tile createTile(TerrainType type) {
        int variant = 0;
        if (type == TerrainType.GRASS || type == TerrainType.FOREST || type == TerrainType.HILL) {
            variant = random.nextInt(3) + 1;
        }
        return new Tile(type, variant);
    }

    private Tile[][] createGrid() {
        return new Tile[HEIGHT][WIDTH];
    }

    private void fillWithBaseTerrain(Tile[][] tiles, TerrainType type) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                tiles[y][x] = createTile(type);
            }
        }
    }

    private void generateExpansiveBiome(Tile[][] tiles, TerrainType targetType, int targetCount) {
        int currentCount = 0;
        List<Point> openSet = new ArrayList<>();

        int seeds = random.nextInt(3) + 2;
        int seedAttempts = 0;
        while (openSet.size() < seeds && seedAttempts < 500) {
            seedAttempts++;
            int rx = random.nextInt(WIDTH);
            int ry = random.nextInt(HEIGHT);

            if (tiles[ry][rx].getTerrain() == TerrainType.GRASS) {
                if (!touchesDifferentMassiveBiome(tiles, rx, ry, targetType)) {
                    tiles[ry][rx] = createTile(targetType);
                    openSet.add(new Point(rx, ry));
                    currentCount++;
                }
            }
        }

        while (currentCount < targetCount && !openSet.isEmpty()) {
            int index = random.nextInt(openSet.size());
            Point current = openSet.remove(index);

            int[] dx = { 0, 0, 1, -1 };
            int[] dy = { 1, -1, 0, 0 };

            for (int i = 0; i < 4 && currentCount < targetCount; i++) {
                int nx = current.x + dx[i];
                int ny = current.y + dy[i];

                if (nx >= 0 && nx < WIDTH && ny >= 0 && ny < HEIGHT) {
                    TerrainType neighborType = tiles[ny][nx].getTerrain();

                    if (neighborType == TerrainType.GRASS) {
                        if (!touchesDifferentMassiveBiome(tiles, nx, ny, targetType)) {
                            if (random.nextDouble() < 0.75) {
                                tiles[ny][nx] = createTile(targetType);
                                openSet.add(new Point(nx, ny));
                                currentCount++;
                            }
                        }
                    }
                }
            }
        }
    }

    private Map<TerrainType, Integer> countNeighbors(Tile[][] grid, int cx, int cy) {
        Map<TerrainType, Integer> counts = new HashMap<>();
        for (int y = cy - 1; y <= cy + 1; y++) {
            for (int x = cx - 1; x <= cx + 1; x++) {
                if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && (x != cx || y != cy)) {
                    TerrainType type = grid[y][x].getTerrain();
                    counts.put(type, counts.getOrDefault(type, 0) + 1);
                }
            }
        }
        return counts;
    }

    private Tile[][] finalSmooth(Tile[][] oldGrid) {
        Tile[][] newGrid = new Tile[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                newGrid[y][x] = oldGrid[y][x];

                Map<TerrainType, Integer> neighbors = countNeighbors(oldGrid, x, y);
                TerrainType currentType = oldGrid[y][x].getTerrain();

                if (neighbors.getOrDefault(currentType, 0) <= 1) {
                    TerrainType majority = currentType;
                    int max = 0;
                    for (Map.Entry<TerrainType, Integer> entry : neighbors.entrySet()) {
                        if (entry.getValue() > max) {
                            max = entry.getValue();
                            majority = entry.getKey();
                        }
                    }
                    if (majority != currentType) {
                        if (majority == TerrainType.GRASS || !touchesDifferentMassiveBiome(oldGrid, x, y, majority)) {
                            newGrid[y][x] = createTile(majority);
                        }
                    }
                }
            }
        }
        return newGrid;
    }

    private Tile[][] generateDeepBiomes(Tile[][] grid) {
        Tile[][] step1Grid = new Tile[HEIGHT][WIDTH];
        int[] dx = { -1, 0, 1, -1, 1, -1, 0, 1 };
        int[] dy = { -1, -1, -1, 0, 0, 1, 1, 1 };

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                TerrainType current = grid[y][x].getTerrain();
                step1Grid[y][x] = grid[y][x];

                if (current == TerrainType.SHALLOW_WATER || current == TerrainType.MOUNTAIN) {
                    boolean isInternal = true;
                    for (int i = 0; i < 8; i++) {
                        int nx = x + dx[i];
                        int ny = y + dy[i];
                        if (nx >= 0 && nx < WIDTH && ny >= 0 && ny < HEIGHT) {
                            if (grid[ny][nx].getTerrain() != current) {
                                isInternal = false;
                                break;
                            }
                        } else {
                            isInternal = false;
                        }
                    }
                    if (isInternal) {
                        if (current == TerrainType.SHALLOW_WATER) {
                            step1Grid[y][x] = new Tile(TerrainType.WATER, 0);
                        } else {
                            step1Grid[y][x] = new Tile(TerrainType.HIGH_MOUNTAIN, 0);
                        }
                    }
                }
            }
        }

        Tile[][] finalGrid = new Tile[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                TerrainType current = step1Grid[y][x].getTerrain();
                finalGrid[y][x] = step1Grid[y][x];

                if (current == TerrainType.WATER) {
                    boolean isInternal = true;
                    for (int i = 0; i < 8; i++) {
                        int nx = x + dx[i];
                        int ny = y + dy[i];
                        if (nx >= 0 && nx < WIDTH && ny >= 0 && ny < HEIGHT) {
                            if (step1Grid[ny][nx].getTerrain() != TerrainType.WATER) {
                                isInternal = false;
                                break;
                            }
                        } else {
                            isInternal = false;
                        }
                    }
                    if (isInternal) {
                        double chance = 0.08;
                        boolean hasDeepNeighbor = false;
                        if (x > 0 && finalGrid[y][x - 1] != null
                                && finalGrid[y][x - 1].getTerrain() == TerrainType.DEEP_WATER) {
                            hasDeepNeighbor = true;
                        }
                        if (y > 0) {
                            if (finalGrid[y - 1][x] != null
                                    && finalGrid[y - 1][x].getTerrain() == TerrainType.DEEP_WATER) {
                                hasDeepNeighbor = true;
                            }
                            if (x > 0 && finalGrid[y - 1][x - 1] != null
                                    && finalGrid[y - 1][x - 1].getTerrain() == TerrainType.DEEP_WATER) {
                                hasDeepNeighbor = true;
                            }
                            if (x < WIDTH - 1 && finalGrid[y - 1][x + 1] != null
                                    && finalGrid[y - 1][x + 1].getTerrain() == TerrainType.DEEP_WATER) {
                                hasDeepNeighbor = true;
                            }
                        }
                        if (hasDeepNeighbor) {
                            chance = 0.68;
                        }

                        if (random.nextDouble() < chance) {
                            finalGrid[y][x] = new Tile(TerrainType.DEEP_WATER, 0);
                        }
                    }
                }
            }
        }

        return finalGrid;
    }

    private void generateDeposits(MapGrid mapGrid) {
        for (ResourceType type : ResourceType.values()) {

            int depositsToSpawn = random.nextInt(12) + 8;
            int spawned = 0;

            while (spawned < depositsToSpawn) {
                int rx = random.nextInt(WIDTH);
                int ry = random.nextInt(HEIGHT);

                TerrainType currentTerrain = mapGrid.getTile(ry, rx).getTerrain();

                if (mapGrid.getDeposit(ry, rx) == null
                        && currentTerrain != TerrainType.SHALLOW_WATER
                        && currentTerrain != TerrainType.WATER
                        && currentTerrain != TerrainType.DEEP_WATER) {

                    int amount = random.nextInt(51) + 50;
                    if (currentTerrain == TerrainType.MOUNTAIN || currentTerrain == TerrainType.HIGH_MOUNTAIN) {
                        Deposit newDeposit = new Deposit(ResourceType.RED_CRYSTAL, amount);
                        mapGrid.setDeposit(ry, rx, newDeposit);

                    } else if (currentTerrain == TerrainType.FOREST) {
                        Deposit newDeposit = new Deposit(ResourceType.GREEN_CRYSTAL, amount);
                        mapGrid.setDeposit(ry, rx, newDeposit);

                    } else if (currentTerrain == TerrainType.HILL) {
                        Deposit newDeposit = new Deposit(ResourceType.BLUE_CRYSTAL, amount);
                        mapGrid.setDeposit(ry, rx, newDeposit);
                    } else {
                        Deposit newDeposit = new Deposit(type, amount);
                        mapGrid.setDeposit(ry, rx, newDeposit);
                    }

                    spawned++;
                }
            }
        }
    }

    public MapGrid generate() {
        Tile[][] tiles = createGrid();

        fillWithBaseTerrain(tiles, TerrainType.GRASS);

        generateExpansiveBiome(tiles, TerrainType.MOUNTAIN, (int) (TOTAL_TILES * MOUNTAIN_PERCENT));

        generateExpansiveBiome(tiles, TerrainType.SHALLOW_WATER, (int) (TOTAL_TILES * SHALLOW_WATER_PERCENT));

        generateExpansiveBiome(tiles, TerrainType.FOREST, (int) (TOTAL_TILES * FOREST_PERCENT));

        generateExpansiveBiome(tiles, TerrainType.HILL, (int) (TOTAL_TILES * HILL_PERCENT));

        tiles = finalSmooth(tiles);
        tiles = generateDeepBiomes(tiles);

        MapGrid mapGrid = new MapGrid(HEIGHT, WIDTH, tiles);

        generateDeposits(mapGrid);

        return mapGrid;
    }

    public Portal generatePortal(MapGrid mapGrid) {
        while (true) {
            int x = random.nextInt(WIDTH - 2) + 1;
            int y = random.nextInt(HEIGHT - 2) + 1;

            TerrainType terrain = mapGrid.getTile(y, x).getTerrain();

            if (terrain == TerrainType.GRASS) {
                boolean surroundedByGrass = true;
                int[] dx = { -1, 0, 1, -1, 1, -1, 0, 1 };
                int[] dy = { -1, -1, -1, 0, 0, 1, 1, 1 };

                for (int i = 0; i < 8; i++) {
                    TerrainType neighbor = mapGrid.getTile(y + dy[i], x + dx[i]).getTerrain();
                    if (neighbor != TerrainType.GRASS) {
                        surroundedByGrass = false;
                        break;
                    }
                }
                if (surroundedByGrass) {
                    if (mapGrid.getDeposit(y, x) != null) {
                        mapGrid.removeDeposit(y, x);
                    }
                    return new Portal(x, y);
                }

            }

        }
    }

    private boolean touchesDifferentMassiveBiome(Tile[][] tiles, int x, int y, TerrainType proposedType) {
        int[] dx = { -1, 0, 1, -1, 1, -1, 0, 1 };
        int[] dy = { -1, -1, -1, 0, 0, 1, 1, 1 };

        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx >= 0 && nx < WIDTH && ny >= 0 && ny < HEIGHT) {
                TerrainType neighbor = tiles[ny][nx].getTerrain();

                if (neighbor != TerrainType.GRASS && neighbor != proposedType) {
                    return true;
                }
            }
        }
        return false;
    }

    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}