package test_gioco.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.map.MapGrid;
import test_gioco.demo.classes.monsters.Goblin;
import test_gioco.demo.classes.monsters.Monster;
import test_gioco.demo.classes.monsters.Snake;
import test_gioco.demo.classes.monsters.Wolf;
import test_gioco.demo.enums.MonsterType;
import test_gioco.demo.enums.TerrainType;

@Service
public class MonsterService {

    private long idCounter = 1;
    private final Random random = new Random();

    public List<Monster> generateRandomMonsters(MapGrid mapGrid, int amount) {
        List<Monster> spawnedMonsters = new ArrayList<>();

        int mapWidth = mapGrid.getWidth();
        int mapHeight = mapGrid.getHeight();

        MonsterType[] monsterTypes = MonsterType.values();

        while (spawnedMonsters.size() < amount) {
            int x = random.nextInt(mapWidth);
            int y = random.nextInt(mapHeight);

            if (mapGrid.getTile(y, x).getTerrain() != TerrainType.WATER && mapGrid.getDeposit(y, x) == null) {

                if (!isPositionOccupied(spawnedMonsters, x, y)) {
                    MonsterType randomType = monsterTypes[random.nextInt(monsterTypes.length)];
                    Monster monster = createMonster(randomType);

                    monster.setId(idCounter++);
                    monster.setX(x);
                    monster.setY(y);

                    spawnedMonsters.add(monster);
                }
            }
        }
        return spawnedMonsters;
    }

    private boolean isPositionOccupied(List<Monster> monsters, int x, int y) {
        for (Monster m : monsters) {
            if (m.getX() == x && m.getY() == y) {
                return true;
            }
        }
        return false;
    }

    private Monster createMonster(MonsterType type) {
        return switch (type) {
            case WOLF -> new Wolf();
            case SNAKE -> new Snake();
            case GOBLIN -> new Goblin();
        };
    }

}