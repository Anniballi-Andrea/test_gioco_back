package test_gioco.demo.classes.units;

import test_gioco.demo.enums.UnitType;

public class Wizard extends Unit {

    public Wizard() {
        hp = 10;
        maxHp = 10;
        attack = 12;
        defense = 1;
        movement = 4;
        remainingMovement = 4;
        attackRange = 2;
        minExtractionPower = 1;
        maxExtractionPower = 5;
        this.spawnPowerCost = UnitType.WIZARD.getSpawnPowerCost();
    }

    @Override
    protected void applyStatGrowth() {
        this.maxHp += 2;
        this.attack += 3;
        this.defense += 1;
        this.attackRange += 1;
        if (this.level >= MAX_LEVEL) {
            this.movement += 1;
        }
        this.spawnPowerCost = Math.max(1, this.spawnPowerCost - 1);
    }

}
