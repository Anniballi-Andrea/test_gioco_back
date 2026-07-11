package test_gioco.demo.services;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.GameState;
import test_gioco.demo.classes.units.Unit;

@Service
public class CombatService {

    private final UnitService unitService;

    public CombatService(UnitService unitService) {
        this.unitService = unitService;
    }

    public void attack(GameState gameState, Unit attacker, Unit target) {

        if (attacker == null || target == null || attacker.isHasAttacked()) {
            return;
        }

        int deltaX = Math.abs(attacker.getX() - target.getX());
        int deltaY = Math.abs(attacker.getX() - target.getY());

        if (deltaX > attacker.getAttackRange() || deltaY > attacker.getAttackRange()) {
            return;
        }

        int damage = attacker.getAttack() - target.getDefense();

        if (damage < 1) {
            damage = 1;
        }

        int newHp = target.getHp() - damage;
        target.setHp(newHp);

        if (newHp <= 0) {
            unitService.removeUnit(gameState, target.getId());
        }

        attacker.setHasAttacked(true);
    }
}
