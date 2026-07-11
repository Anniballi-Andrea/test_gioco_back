package test_gioco.demo.classes.map;

import test_gioco.demo.enums.ResourceType;

public class Deposit {
    private final ResourceType resourceType;
    private int amount;

    public Deposit(ResourceType resourceType, int amount) {
        this.resourceType = resourceType;
        this.amount = amount;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getAmount() {
        return amount;
    }

    public int extract(int amountToExtract) {
        if (amountToExtract <= 0) {
            return 0;
        }
        if (amountToExtract >= this.amount) {
            int actualExtracted = this.amount;
            this.amount = 0;
            return actualExtracted;

        }
        this.amount -= amountToExtract;
        return amountToExtract;
    }

    public boolean isExpired() {
        return this.amount <= 0;
    }
}
