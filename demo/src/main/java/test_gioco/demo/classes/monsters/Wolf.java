package test_gioco.demo.classes.monsters;

import test_gioco.demo.enums.MonsterType;

public class Wolf extends Monster {

    public Wolf() {
        this.type = MonsterType.WOLF;
        this.hp = 15;
        this.maxHp = 15;
        this.attack = 4;
        this.attackRange = 1;
        this.defense = 2;
        this.minDropPower = 20;
        this.maxDropPower = 45;
    }

}
