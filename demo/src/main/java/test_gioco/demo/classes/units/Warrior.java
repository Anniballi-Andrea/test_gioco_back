package test_gioco.demo.classes.units;

import test_gioco.demo.enums.UnitType;

public class Warrior extends Unit {

    public Warrior() {
        hp = 20;
        maxHp = 20;
        attack = 6;
        defense = 3;
        movement = 4;
        remainingMovement = 4;
        attackRange = 1;
        minExtractionPower = 3;
        maxExtractionPower = 10;
        this.spawnPowerCost = UnitType.WARRIOR.getSpawnPowerCost();
    }

    @Override
    protected void applyStatGrowth() {
        this.maxHp += 8;
        this.attack += 3;
        this.defense += 2;
        if (this.level >= MAX_LEVEL) {
            this.movement += 1;
        }
        this.spawnPowerCost = Math.max(1, this.spawnPowerCost - 1);
    }

}
