package test_gioco.demo.classes.monsters;

import test_gioco.demo.enums.MonsterType;

public class Snake extends Monster {

    public Snake() {
        this.type = MonsterType.SNAKE;
        this.hp = 10;
        this.maxHp = 10;
        this.attack = 3;
        this.attackRange = 1;
        this.defense = 1;
        this.minDropPower = 10;
        this.maxDropPower = 15;
        this.movement = 4;
    }

}
