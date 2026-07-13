package test_gioco.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.GameState;
import test_gioco.demo.classes.map.MapGrid;
import test_gioco.demo.classes.map.Portal;
import test_gioco.demo.classes.monsters.Monster;
import test_gioco.demo.classes.units.Unit;
import test_gioco.demo.enums.TerrainType;

@Service
public class TurnService {

    private final Random random = new Random();

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

        processCpuTurn(gameState);

    }

    private void processCpuTurn(GameState gameState) {
        MapGrid map = gameState.getMap();
        List<Monster> monsters = gameState.getMonsters();
        List<Unit> units = gameState.getUnits();
        Portal portal = gameState.getPortal();

        int[] dx = { -1, 0, 1, -1, 1, -1, 0, 1 };
        int[] dy = { -1, -1, -1, 0, 0, 1, 1, 1 };

        for (Monster monster : monsters) {
            if (units.isEmpty()) {
                takeRandomStep(monster, map, portal, monsters, units, dx, dy);
                continue;
            }

            Unit closestUnit = null;
            int minDistance = Integer.MAX_VALUE;

            for (Unit unit : units) {
                int dist = Math.max(Math.abs(monster.getX() - unit.getX()), Math.abs(monster.getY() - unit.getY()));
                if (dist < minDistance) {
                    minDistance = dist;
                    closestUnit = unit;
                }
            }
            int maxAggroRange = monster.getMovement() * 2;

            if (closestUnit != null && minDistance <= maxAggroRange) {
                int movesLeft = monster.getMovement();
                while (movesLeft > 0) {
                    int currentDist = Math.max(Math.abs(monster.getX() - closestUnit.getX()),
                            Math.abs(monster.getY() - closestUnit.getY()));
                    if (currentDist <= 1) {
                        break;
                    }

                    List<int[]> bestMoves = new ArrayList<>();
                    int bestNextDistance = Integer.MAX_VALUE;

                    for (int i = 0; i < 8; i++) {
                        int nextX = monster.getX() + dx[i];
                        int nextY = monster.getY() + dy[i];

                        if (isValidMove(nextX, nextY, map, portal, monsters, units, monster)) {
                            int nextDist = Math.max(Math.abs(nextX - closestUnit.getX()),
                                    Math.abs(nextY - closestUnit.getY()));
                            if (nextDist < bestNextDistance) {
                                bestNextDistance = nextDist;
                                bestMoves.clear();
                                bestMoves.add(new int[] { nextX, nextY });
                            } else if (nextDist == bestNextDistance) {
                                bestMoves.add(new int[] { nextX, nextY });
                            }
                        }
                    }

                    if (!bestMoves.isEmpty() && bestNextDistance < currentDist) {
                        int[] chosenMove = bestMoves.get(random.nextInt(bestMoves.size()));
                        monster.setX(chosenMove[0]);
                        monster.setY(chosenMove[1]);
                        movesLeft--;
                    } else {
                        break;
                    }
                }
            } else {
                takeRandomStep(monster, map, portal, monsters, units, dx, dy);
            }
        }
    }

    private boolean isValidMove(int nextX, int nextY, MapGrid map, Portal portal, List<Monster> monsters,
            List<Unit> units, Monster currentMonster) {
        if (nextX < 0 || nextX >= map.getWidth() || nextY < 0 || nextY >= map.getHeight()) {
            return false;
        }
        if (map.getTile(nextY, nextX).getTerrain() == TerrainType.WATER) {
            return false;
        }
        if (map.getDeposit(nextY, nextX) != null) {
            return false;
        }
        if (portal != null && portal.getX() == nextX && portal.getY() == nextY) {
            return false;
        }
        for (Unit u : units) {
            if (u.getX() == nextX && u.getY() == nextY) {
                return false;
            }
        }
        for (Monster m : monsters) {
            if (m != currentMonster && m.getX() == nextX && m.getY() == nextY) {
                return false;
            }
        }
        return true;
    }

    private void takeRandomStep(Monster monster, MapGrid map, Portal portal, List<Monster> monsters, List<Unit> units,
            int[] dx, int[] dy) {
        List<int[]> validMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int nextX = monster.getX() + dx[i];
            int nextY = monster.getY() + dy[i];
            if (isValidMove(nextX, nextY, map, portal, monsters, units, monster)) {
                validMoves.add(new int[] { nextX, nextY });
            }
        }
        if (!validMoves.isEmpty()) {
            int[] chosenMove = validMoves.get(random.nextInt(validMoves.size()));
            monster.setX(chosenMove[0]);
            monster.setY(chosenMove[1]);
        }
    }

}
