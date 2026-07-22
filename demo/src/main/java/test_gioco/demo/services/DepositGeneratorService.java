package test_gioco.demo.services;

import java.util.Random;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.map.Deposit;
import test_gioco.demo.classes.map.MapGrid;
import test_gioco.demo.enums.ResourceType;
import test_gioco.demo.enums.TerrainType;

@Service
public class DepositGeneratorService {
    private final Random random = new Random();

    public void generateDeposits(MapGrid mapGrid) {
        for (ResourceType type : ResourceType.values()) {

            int depositsToSpawn = random.nextInt(12) + 8;
            int spawned = 0;

            while (spawned < depositsToSpawn) {
                int rx = random.nextInt(mapGrid.getWidth());
                int ry = random.nextInt(mapGrid.getHeight());

                TerrainType currentTerrain = mapGrid.getTile(ry, rx).getTerrain();

                if (mapGrid.getDeposit(ry, rx) == null
                        && currentTerrain != TerrainType.SHALLOW_WATER
                        && currentTerrain != TerrainType.WATER
                        && currentTerrain != TerrainType.DEEP_WATER) {

                    int amount = random.nextInt(51) + 50;
                    if (currentTerrain == TerrainType.MOUNTAIN || currentTerrain == TerrainType.HIGH_MOUNTAIN) {
                        Deposit newDeposit = new Deposit(ResourceType.RED_CRYSTAL, amount);
                        mapGrid.setDeposit(ry, rx, newDeposit);

                    } else if (currentTerrain == TerrainType.FOREST) {
                        Deposit newDeposit = new Deposit(ResourceType.GREEN_CRYSTAL, amount);
                        mapGrid.setDeposit(ry, rx, newDeposit);

                    } else if (currentTerrain == TerrainType.HILL) {
                        Deposit newDeposit = new Deposit(ResourceType.BLUE_CRYSTAL, amount);
                        mapGrid.setDeposit(ry, rx, newDeposit);
                    } else {
                        Deposit newDeposit = new Deposit(type, amount);
                        mapGrid.setDeposit(ry, rx, newDeposit);
                    }

                    spawned++;
                }
            }
        }
    }

}
