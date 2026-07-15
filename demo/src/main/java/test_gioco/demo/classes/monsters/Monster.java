package test_gioco.demo.classes.monsters;

import test_gioco.demo.enums.MonsterType;

public class Monster {
    protected long id;

    protected MonsterType type;

    protected int x;
    protected int y;

    protected int hp;
    protected int maxHp;

    protected int attack;
    protected int attackRange;

    protected int defense;

    protected int minDropPower;
    protected int maxDropPower;

    protected int movement;

    protected int xpReward;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MonsterType getType() {
        return this.type;
    }

    public void setType(MonsterType type) {
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

    public int getHp() {
        return this.hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return this.maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getAttack() {
        return this.attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAttackRange() {
        return this.attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getDefense() {
        return this.defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMinDropPower() {
        return this.minDropPower;
    }

    public void setMinDropPower(int minDropPower) {
        this.minDropPower = minDropPower;
    }

    public int getMaxDropPower() {
        return this.maxDropPower;
    }

    public void setMaxDropPower(int maxDropPower) {
        this.maxDropPower = maxDropPower;
    }

    public int getMovement() {
        return this.movement;
    }

    public int getXpReward() {
        return this.xpReward;
    }

    public void setXpReward(int xp) {
        this.xpReward = xp;
    }
}