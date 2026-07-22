package test_gioco.demo.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.GameConstants;
import test_gioco.demo.classes.map.MapGrid;
import test_gioco.demo.classes.map.Portal;
import test_gioco.demo.classes.map.Tile;
import test_gioco.demo.enums.TerrainType;

@Service
public class MapGeneratorService {

    private final DepositGeneratorService depositSerivice;
    private final TerrainGeneratorService terrainService;
    private final TileFactory tileFactory;

    public MapGeneratorService(DepositGeneratorService depositService, TerrainGeneratorService terrainService,
            TileFactory tileFactory) {
        this.depositSerivice = depositService;
        this.terrainService = terrainService;
        this.tileFactory = tileFactory;
    }

    private static final int WIDTH = GameConstants.WIDTH;
    private static final int HEIGHT = GameConstants.HEIGHT;

    private final Random random = new Random();

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
                            newGrid[y][x] = tileFactory.create(majority);
                        }
                    }
                }
            }
        }
        return newGrid;
    }

    private boolean touchesDifferentMassiveBiome(Tile[][] tiles, int x, int y,
            TerrainType proposedType) {
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

    public MapGrid generate() {
        Tile[][] tiles = terrainService.generateTerrain();

        tiles = finalSmooth(tiles);
        tiles = generateDeepBiomes(tiles);

        MapGrid mapGrid = new MapGrid(HEIGHT, WIDTH, tiles);

        depositSerivice.generateDeposits(mapGrid);

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
}