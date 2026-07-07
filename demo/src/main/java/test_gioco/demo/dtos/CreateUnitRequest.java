package test_gioco.demo.dtos;

import test_gioco.demo.classes.UnitType;

public class CreateUnitRequest {
    private UnitType type;
    private int x;
    private int y;

    public UnitType getType() {
        return this.type;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
