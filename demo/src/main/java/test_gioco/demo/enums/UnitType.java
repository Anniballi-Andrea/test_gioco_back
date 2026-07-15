package test_gioco.demo.enums;

public enum UnitType {
    WARRIOR(ResourceType.RED_CRYSTAL, 25, 3),
    WIZARD(ResourceType.BLUE_CRYSTAL, 30, 4),
    ARCHER(ResourceType.GREEN_CRYSTAL, 15, 3),
    EXPLORER(ResourceType.RED_CRYSTAL, 5, 1);

    private final ResourceType resource;
    private final int cost;
    private final int spawnPowerCost;

    UnitType(ResourceType resource, int cost, int spawnPowerCost) {
        this.resource = resource;
        this.cost = cost;
        this.spawnPowerCost = spawnPowerCost;
    }

    public ResourceType getResource() {
        return resource;
    }

    public int getCost() {
        return cost;
    }

    public int getSpawnPowerCost() {
        return spawnPowerCost;
    }
}
