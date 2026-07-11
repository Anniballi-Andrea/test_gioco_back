package test_gioco.demo.services;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.GameState;
import test_gioco.demo.classes.units.Unit;

@Service
public class TurnService {
    public void endTurn(GameState gameState) {
        if (gameState == null) {
            return;
        }

        gameState.setCurrentTurn(gameState.getCurrentTurn() + 1);

        for (Unit unit : gameState.getUnits()) {
            unit.setRemainingMovement(unit.getMovement());
            unit.setHasAttacked(false);
            unit.setHasExtracted(false);
        }
    }

}
