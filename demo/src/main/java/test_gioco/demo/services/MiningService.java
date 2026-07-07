package test_gioco.demo.services;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.Deposit;
import test_gioco.demo.classes.GameState;
import test_gioco.demo.classes.MapGrid;
import test_gioco.demo.classes.ResourceType;
import test_gioco.demo.classes.Unit;

@Service
public class MiningService {

    public boolean extractResource(Unit unit, int targetY, int targetX, GameState gameState) {

        if (unit.isHasExtracted() || unit.isHasAttacked()) {
            return false;
        }

        int distanceY = Math.abs(unit.getY() - targetY);
        int distanceX = Math.abs(unit.getX() - targetX);

        if (distanceY > 1 || distanceX > 1) {
            return false;
        }

        MapGrid map = gameState.getMap();
        Deposit deposit = map.getDeposit(targetY, targetX);

        if (deposit == null || deposit.isExpired()) {
            return false;
        }

        int min = unit.getMinExtractionPower();
        int max = unit.getMaxExtractionPower();

        int extractionPower = ThreadLocalRandom.current().nextInt(min, max + 1);

        int actuallyExtracted = deposit.extract(extractionPower);

        if (actuallyExtracted > 0) {
            ResourceType type = deposit.getResourceType();
            int currentAmount = gameState.getResources().getOrDefault(type, 0);
            gameState.getResources().put(type, currentAmount + actuallyExtracted);

            unit.setHasExtracted(true);
            unit.setRemainingMovement(0);
            unit.setHasAttacked(true);

            if (deposit.isExpired()) {
                map.removeDeposit(targetY, targetX);
            }
            return true;
        }
        return false;
    }

}
