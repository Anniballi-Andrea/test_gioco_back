package test_gioco.demo.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import test_gioco.demo.classes.map.MapGrid;
import test_gioco.demo.classes.map.Portal;
import test_gioco.demo.classes.monsters.Monster;
import test_gioco.demo.classes.units.Unit;
import test_gioco.demo.enums.ResourceType;

public class GameState {

    private final UUID gameId;
    private final MapGrid map;
    private final List<Unit> units;
    private final List<Monster> monsters;
    private int currentTurn;
    private final Map<ResourceType, Integer> resources;
    private Portal portal;

    public GameState(MapGrid map) {
        this.gameId = UUID.randomUUID();
        this.map = map;
        this.units = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.currentTurn = 1;
        this.resources = new HashMap<>();
        this.resources.put(ResourceType.RED_CRYSTAL, 50);
        this.resources.put(ResourceType.BLUE_CRYSTAL, 50);
        this.resources.put(ResourceType.GREEN_CRYSTAL, 50);
    }

    public UUID getGameId() {
        return gameId;
    }

    public MapGrid getMap() {
        return map;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public Map<ResourceType, Integer> getResources() {
        return resources;
    }

    public Portal getPortal() {
        return portal;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

}
