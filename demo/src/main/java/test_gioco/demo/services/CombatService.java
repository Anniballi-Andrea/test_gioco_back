package test_gioco.demo.services;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.GameState;
import test_gioco.demo.classes.monsters.Monster;
import test_gioco.demo.classes.units.Unit;
import test_gioco.demo.dtos.AttackResult;

@Service
public class CombatService {

    public AttackResult executeAttack(GameState gameState, long attackerUnitId, long targetMonsterId) {

        Unit attacker = gameState.getUnits().stream()
                .filter(u -> u.getId() == attackerUnitId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Attaccante non trovato"));

        Monster target = gameState.getMonsters().stream()
                .filter(m -> m.getId() == targetMonsterId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Mostro non trovato"));

        if (attacker.isHasAttacked() || attacker.isHasExtracted()) {
            throw new IllegalStateException("L'unità ha già attaccato questo turno");
        }

        int distance = Math.max(Math.abs(attacker.getX() - target.getX()), Math.abs(attacker.getY() - target.getY()));
        if (distance > attacker.getAttackRange()) {
            throw new IllegalStateException("Il mostro è fuori portata");
        }

        int damageDealt = Math.max(1, attacker.getAttack() - target.getDefense());

        int newHp = target.getHp() - damageDealt;
        target.setHp(newHp);

        attacker.setHasAttacked(true);

        boolean isDead = newHp <= 0;
        if (isDead) {
            gameState.getMonsters().remove(target);
        }

        return new AttackResult(damageDealt, Math.max(0, newHp), isDead);
    }
}
