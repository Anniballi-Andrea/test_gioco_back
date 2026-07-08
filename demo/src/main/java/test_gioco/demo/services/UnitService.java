package test_gioco.demo.services;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.Archer;
import test_gioco.demo.classes.GameState;
import test_gioco.demo.classes.ResourceType;
import test_gioco.demo.classes.Unit;
import test_gioco.demo.classes.UnitType;
import test_gioco.demo.classes.Warrior;
import test_gioco.demo.classes.Wizard;

@Service
public class UnitService {

    private long nextUnitId = 1;

    public Unit createUnit(GameState gameState, UnitType type, int x, int y) {

        ResourceType requiredResource = type.getResource();
        int requiredCost = type.getCost();

        int currentBalance = gameState.getResources().getOrDefault(requiredResource, 0);

        if (currentBalance < requiredCost) {
            return null;
        }

        gameState.getResources().put(requiredResource, currentBalance - requiredCost);

        Unit unit;

        switch (type) {
            case WARRIOR:

                unit = new Warrior();
                break;
            case WIZARD:
                unit = new Wizard();
                break;
            default:
                return null;
            case ARCHER:
                unit = new Archer();
                break;
        }

        unit.setType(type);
        unit.setId(nextUnitId++);
        unit.setX(x);
        unit.setY(y);

        gameState.getUnits().add(unit);

        return unit;
    }

    public Unit findUnit(GameState gameState, long id) {
        return gameState.getUnits()
                .stream()
                .filter(unit -> unit.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean removeUnit(GameState gameState, long id) {
        return gameState.getUnits()
                .removeIf(unit -> unit.getId() == id);
    }

    public boolean moveUnit(GameState gameState, long id, int x, int y) {
        Unit unit = findUnit(gameState, id);

        if (unit == null || unit.getRemainingMovement() <= 0) {
            return false;
        }
        int deltaX = x - unit.getX();
        int deltaY = y - unit.getY();

        int distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
        int maxMovementSquared = unit.getRemainingMovement() * unit.getRemainingMovement();

        if (distanceSquared > maxMovementSquared) {
            return false;
        }

        int cost = (int) Math.ceil(Math.sqrt(distanceSquared));

        unit.setX(x);
        unit.setY(y);

        unit.setRemainingMovement(unit.getRemainingMovement() - cost);

        return true;
    }

}