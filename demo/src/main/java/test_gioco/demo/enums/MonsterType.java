package test_gioco.demo.enums;

import java.util.List;

public enum MonsterType {
    WOLF(List.of(ResourceType.RED_CRYSTAL, ResourceType.BLUE_CRYSTAL)),
    SNAKE(List.of(ResourceType.GREEN_CRYSTAL)),
    GOBLIN(List.of(ResourceType.RED_CRYSTAL, ResourceType.GREEN_CRYSTAL));

    private final List<ResourceType> droppableResources;

    MonsterType(List<ResourceType> droppableResources) {
        this.droppableResources = droppableResources;
    }

    public List<ResourceType> getDroppableResources() {
        return droppableResources;
    }
}
