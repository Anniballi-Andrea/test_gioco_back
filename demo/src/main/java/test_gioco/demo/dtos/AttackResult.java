package test_gioco.demo.dtos;

public class AttackResult {
    private final int damageDealt;
    private final int remainingHp;
    private final boolean targetKilled;

    public AttackResult(int damageDealt, int remainingHp, boolean targetKilled) {
        this.damageDealt = damageDealt;
        this.remainingHp = remainingHp;
        this.targetKilled = targetKilled;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public int getRemainingHp() {
        return remainingHp;
    }

    public boolean isTargetKilled() {
        return targetKilled;
    }
}
