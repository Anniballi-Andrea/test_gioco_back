package test_gioco.demo.classes;

public final class GameConstants {

    public static final int WIDTH = 65;
    public static final int HEIGHT = 65;

    public static final double SHALLOW_WATER_PERCENT = 0.15;
    public static final double MOUNTAIN_PERCENT = 0.20;
    public static final double FOREST_PERCENT = 0.15;
    public static final double HILL_PERCENT = 0.15;

    private GameConstants() {
    }

    public static final int getTotalTiles() {
        return HEIGHT * WIDTH;
    }
}
