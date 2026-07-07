package test_gioco.demo.classes;

public class MapGrid {

    private final int width;
    private final int height;
    private final Tile[][] tiles;
    private final Deposit[][] deposits;

    public MapGrid(int height, int width, Tile[][] tiles) {
        this.height = height;
        this.width = width;
        this.tiles = tiles;
        this.deposits = new Deposit[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile getTiles(int y, int x) {
        return tiles[y][x];
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile[] getRow(int y) {
        return tiles[y];
    }

    public Deposit getDeposit(int y, int x) {
        return deposits[y][x];
    }

    public void setDeposit(int y, int x, Deposit deposit) {
        this.deposits[y][x] = deposit;
    }

    public void removeDeposit(int y, int x) {
        this.deposits[y][x] = null;
    }

    public Deposit[][] getDeposits() {
        return deposits;
    }
}