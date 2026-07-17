package test_gioco.demo.classes.map;

import test_gioco.demo.enums.TerrainType;

public class Tile {

    private final TerrainType terrain;
    private final int variant;

    public Tile(TerrainType terrain, int variant) {
        this.terrain = terrain;
        this.variant = variant;
    }

    public TerrainType getTerrain() {
        return terrain;
    }

    public int getVariant() {
        return variant;
    }
}
