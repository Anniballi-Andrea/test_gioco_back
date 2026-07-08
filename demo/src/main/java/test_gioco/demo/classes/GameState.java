package test_gioco.demo.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameState {

    private final UUID gameId;
    private final MapGrid map;
    private final List<Unit> units;
    private int currentTurn;
    private final Map<ResourceType, Integer> resources;

    public GameState(MapGrid map) {
        this.gameId = UUID.randomUUID();
        this.map = map;
        this.units = new ArrayList<>();
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

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public Map<ResourceType, Integer> getResources() {
        return resources;
    }

}
