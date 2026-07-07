package test_gioco.demo.classes;

public enum UnitType {
    WARRIOR(ResourceType.RED_CRYSTAL, 50);

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
