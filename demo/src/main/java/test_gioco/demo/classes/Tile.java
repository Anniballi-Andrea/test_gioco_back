package test_gioco.demo.classes;

public class Tile {

    private final TerrainType terrain;

    public Tile(TerrainType terrain) {
        this.terrain = terrain;
    }

    public TerrainType getTerrain() {
        return terrain;
    }
}
