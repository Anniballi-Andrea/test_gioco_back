package test_gioco.demo.classes.map;

public class Portal {

    private int hp;
    private final int x;
    private final int y;

    public Portal(int x, int y) {
        this.hp = 200;
        this.x = x;
        this.y = y;

    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getHp() {
        return this.hp;
    }

}
