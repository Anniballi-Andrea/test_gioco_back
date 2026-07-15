package test_gioco.demo.classes.units;

import test_gioco.demo.enums.UnitType;

public class Unit {

    protected long id;

    protected UnitType type;

    protected int x;
    protected int y;

    protected int hp;
    protected int maxHp;

    protected int attack;
    protected int attackRange;

    protected int defense;

    protected int movement;
    protected int remainingMovement;

    protected int minExtractionPower;

    protected int maxExtractionPower;

    protected boolean hasAttacked;
    protected boolean hasExtracted;

    protected int level = 1;
    protected int experience = 0;
    protected static final int MAX_LEVEL = 3;

    protected int spawnPowerCost;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UnitType getType() {
        return type;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public int getRemainingMovement() {
        return this.remainingMovement;
    }

    public void setRemainingMovement(int remainingMovement) {
        this.remainingMovement = remainingMovement;
    }

    public boolean isHasAttacked() {
        return hasAttacked;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public boolean isHasExtracted() {
        return hasExtracted;
    }

    public void setHasExtracted(boolean hasExtracted) {
        this.hasExtracted = hasExtracted;
    }

    public int getMinExtractionPower() {
        return this.minExtractionPower;
    }

    public int getMaxExtractionPower() {
        return this.maxExtractionPower;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getSpawnPowerCost() {
        return this.spawnPowerCost;
    }

    public void setSpawnPowerCost(int spawnPowerCost) {
        this.spawnPowerCost = spawnPowerCost;
    }

    public int addExperience(int amount) {
        if (this.level >= MAX_LEVEL) {
            return 0;
        }

        this.experience += amount;
        int totalSpawnPowerRefunded = 0;

        while (this.level < MAX_LEVEL && this.experience >= getXpNeededForNextLevel()) {
            this.experience -= getXpNeededForNextLevel();

            // Salviamo il costo prima del level up per calcolare la differenza
            int previousCost = this.spawnPowerCost;

            levelUp();

            // Calcoliamo quanto spawn power è stato liberato
            totalSpawnPowerRefunded += (previousCost - this.spawnPowerCost);
        }

        if (this.level >= MAX_LEVEL) {
            this.experience = 0;
        }

        return totalSpawnPowerRefunded;
    }

    public int getXpNeededForNextLevel() {
        return 100 * this.level;
    }

    protected void levelUp() {
        this.level++;
        applyStatGrowth();
        this.hp = this.maxHp;
    }

    protected void applyStatGrowth() {

    }

}