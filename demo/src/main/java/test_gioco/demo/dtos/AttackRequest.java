package test_gioco.demo.dtos;

public class AttackRequest {
    private long attackerId;
    private long targetId;

    public long getAttackerId() {
        return this.attackerId;
    }

    public void setAttackerId(long attackerId) {
        this.attackerId = attackerId;
    }

    public long getTargetId() {
        return this.targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

}
