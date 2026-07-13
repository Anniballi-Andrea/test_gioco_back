package test_gioco.demo.enums;

public enum UnitType {
    WARRIOR(ResourceType.RED_CRYSTAL, 25, 2),
    WIZARD(ResourceType.BLUE_CRYSTAL, 30, 3),
    ARCHER(ResourceType.GREEN_CRYSTAL, 15, 2);

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
