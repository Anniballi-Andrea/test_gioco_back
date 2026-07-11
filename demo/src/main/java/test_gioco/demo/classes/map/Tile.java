package test_gioco.demo.classes.map;

import test_gioco.demo.enums.TerrainType;

public class Tile {

    private final TerrainType terrain;

    public Tile(TerrainType terrain) {
        this.terrain = terrain;
    }

    public TerrainType getTerrain() {
        return terrain;
    }
}
