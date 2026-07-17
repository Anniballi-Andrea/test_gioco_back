package test_gioco.demo.services;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.GameState;
import test_gioco.demo.classes.map.MapGrid;
import test_gioco.demo.classes.map.Portal;
import test_gioco.demo.classes.units.Archer;
import test_gioco.demo.classes.units.Explorer;
import test_gioco.demo.classes.units.Unit;
import test_gioco.demo.classes.units.Warrior;
import test_gioco.demo.classes.units.Wizard;
import test_gioco.demo.enums.ResourceType;
import test_gioco.demo.enums.TerrainType;
import test_gioco.demo.enums.UnitType;
import test_gioco.demo.exceptions.SpawnException;

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

        if (gameState.getSpawnPower() < type.getSpawnPowerCost()) {
            throw new SpawnException("Hai esaurito lo spazio necessario");
        }

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
            case ARCHER:
                unit = new Archer();
                break;
            case EXPLORER:
                unit = new Explorer();
                break;
            default:
                throw new SpawnException("Tipo di unità non valido.");
        }

        unit.setType(type);
        unit.setId(nextUnitId++);
        unit.setX(x);
        unit.setY(y);
        unit.setRemainingMovement(0);
        unit.setHasExtracted(true);
        unit.setHasAttacked(true);

        gameState.getUnits().add(unit);
        gameState.reduceSpawnPower(type.getSpawnPowerCost());

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

        int cost = (int) Math.ceil(Math.sqrt(distanceSquared));

        MapGrid mapGrid = gameState.getMap();
        TerrainType targetTerrain = mapGrid.getTile(y, x).getTerrain();

        if (targetTerrain == TerrainType.DEEP_WATER) {
            return false;
        }
        if (targetTerrain == TerrainType.WATER) {
            cost = cost * 3;
        }

        if (targetTerrain == TerrainType.SHALLOW_WATER) {
            cost = cost * 2;
        }

        else if (targetTerrain == TerrainType.HIGH_MOUNTAIN) {
            if (unit.getType() != UnitType.EXPLORER) {
                cost = cost * 2;
            }
        }

        if (unit.getRemainingMovement() < cost) {
            return false;
        }

        unit.setX(x);
        unit.setY(y);

        unit.setRemainingMovement(unit.getRemainingMovement() - cost);

        return true;
    }

}