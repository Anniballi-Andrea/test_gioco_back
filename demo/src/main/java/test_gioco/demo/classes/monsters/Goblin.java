package test_gioco.demo.classes.monsters;

import test_gioco.demo.enums.MonsterType;

public class Goblin extends Monster {

    public Goblin() {
        this.type = MonsterType.GOBLIN;
        this.hp = 12;
        this.maxHp = 12;
        this.attack = 3;
        this.attackRange = 1;
        this.defense = 1;
        this.minDropPower = 10;
        this.maxDropPower = 25;
        this.movement = 3;
    }

}
