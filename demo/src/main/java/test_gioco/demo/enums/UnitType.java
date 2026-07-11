package test_gioco.demo.enums;

public enum UnitType {
    WARRIOR(ResourceType.RED_CRYSTAL, 25),
    WIZARD(ResourceType.BLUE_CRYSTAL, 30),
    ARCHER(ResourceType.GREEN_CRYSTAL, 15);

    private final ResourceType resource;
    private final int cost;

    UnitType(ResourceType resource, int cost) {
        this.resource = resource;
        this.cost = cost;
    }

    public ResourceType getResource() {
        return resource;
    }

    public int getCost() {
        return cost;
    }

}
