package test_gioco.demo.classes.units;

import test_gioco.demo.enums.UnitType;

public class Archer extends Unit {
    public Archer() {
        hp = 10;
        maxHp = 10;
        attack = 6;
        defense = 2;
        movement = 5;
        remainingMovement = 5;
        attackRange = 3;
        minExtractionPower = 2;
        maxExtractionPower = 6;
        this.spawnPowerCost = UnitType.ARCHER.getSpawnPowerCost();
    }

    @Override
    protected void applyStatGrowth() {
        this.maxHp += 5;
        this.attack += 3;
        this.defense += 1;
        this.attackRange += 1;
        if (this.level >= MAX_LEVEL) {
            this.movement += 1;
        }
        this.spawnPowerCost = Math.max(1, this.spawnPowerCost - 1);
    }

}
