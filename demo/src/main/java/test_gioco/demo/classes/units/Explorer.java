package test_gioco.demo.classes.units;

import test_gioco.demo.enums.UnitType;

public class Explorer extends Unit {
    public Explorer() {
        hp = 5;
        maxHp = 5;
        attack = 0;
        defense = 0;
        movement = 5;
        remainingMovement = 5;
        attackRange = 1;
        minExtractionPower = 10;
        maxExtractionPower = 20;
        this.spawnPowerCost = UnitType.EXPLORER.getSpawnPowerCost();
    }

    @Override
    protected void applyStatGrowth() {
        this.maxHp += 5;
        this.movement += 1;
        this.minExtractionPower += 3;
        this.maxExtractionPower += 5;
    }

}
