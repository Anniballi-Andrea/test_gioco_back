package test_gioco.demo.services;

import org.springframework.stereotype.Service;
import test_gioco.demo.classes.GameConstants;
import test_gioco.demo.classes.map.MapGrid;
import test_gioco.demo.classes.map.Tile;

@Service
public class MapGeneratorService {

    private final DepositGeneratorService depositSerivice;
    private final TerrainGeneratorService terrainService;

    public MapGeneratorService(DepositGeneratorService depositService, TerrainGeneratorService terrainService) {
        this.depositSerivice = depositService;
        this.terrainService = terrainService;

    }

    private static final int WIDTH = GameConstants.WIDTH;
    private static final int HEIGHT = GameConstants.HEIGHT;

    public MapGrid generate() {
        Tile[][] tiles = terrainService.generateTerrain();

        tiles = terrainService.finalSmooth(tiles);
        tiles = terrainService.generateDeepBiomes(tiles);

        MapGrid mapGrid = new MapGrid(HEIGHT, WIDTH, tiles);

        depositSerivice.generateDeposits(mapGrid);

        return mapGrid;
    }

}