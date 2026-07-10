package test_gioco.demo.services;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.Archer;
import test_gioco.demo.classes.GameState;
import test_gioco.demo.classes.Portal;
import test_gioco.demo.classes.ResourceType;
import test_gioco.demo.classes.Unit;
import test_gioco.demo.classes.UnitType;
import test_gioco.demo.classes.Warrior;
import test_gioco.demo.classes.Wizard;
import test_gioco.demo.exeptions.SpawnException;

@Service
public class UnitService {

    private long nextUnitId = 1;

    private boolean isTileOccupied(GameState gameState, int x, int y) {

        for (Unit unit : gameState.getUnits()) {
            if (unit.getX() == x && unit.getY() == y) {
                return true;
            }
        }

        return false;
    }

    private boolean isAdjacentToPortal(GameState gameState, int x, int y) {

        Portal portal = gameState.getPortal();

        if (portal == null) {
            return false;
        }

        int dx = Math.abs(x - portal.getX());
        int dy = Math.abs(y - portal.getY());

        return dx <= 1
                && dy <= 1
                && !(dx == 0 && dy == 0);
    }

    public void createUnit(GameState gameState, UnitType type, int x, int y) {

        if (!isAdjacentToPortal(gameState, x, y)) {
            throw new SpawnException("Il portale è troppo lontano.");
        }

        if (isTileOccupied(gameState, x, y)) {
            throw new SpawnException("La casella è già occupata.");
        }

        ResourceType requiredResource = type.getResource();
        int requiredCost = type.getCost();

        int currentBalance = gameState.getResources().getOrDefault(requiredResource, 0);

        if (currentBalance < requiredCost) {
            throw new SpawnException("Risorse insufficienti per creare l'unità.");
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
                throw new SpawnException("Tipo di unità non valido.");
            case ARCHER:
                unit = new Archer();
                break;
        }

        unit.setType(type);
        unit.setId(nextUnitId++);
        unit.setX(x);
        unit.setY(y);

        gameState.getUnits().add(unit);

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