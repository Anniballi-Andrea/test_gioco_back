package test_gioco.demo.services;

import java.util.Random;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.GameConstants;
import test_gioco.demo.classes.map.MapGrid;
import test_gioco.demo.classes.map.Portal;
import test_gioco.demo.enums.TerrainType;

@Service
public class PortalService {

    private static final int WIDTH = GameConstants.WIDTH;
    private static final int HEIGHT = GameConstants.HEIGHT;
    private final Random random = new Random();

    public Portal generatePortal(MapGrid mapGrid) {
        while (true) {
            int x = random.nextInt(WIDTH - 2) + 1;
            int y = random.nextInt(HEIGHT - 2) + 1;

            TerrainType terrain = mapGrid.getTile(y, x).getTerrain();

            if (terrain == TerrainType.GRASS) {
                boolean surroundedByGrass = true;
                int[] dx = { -1, 0, 1, -1, 1, -1, 0, 1 };
                int[] dy = { -1, -1, -1, 0, 0, 1, 1, 1 };

                for (int i = 0; i < 8; i++) {
                    TerrainType neighbor = mapGrid.getTile(y + dy[i], x + dx[i]).getTerrain();
                    if (neighbor != TerrainType.GRASS) {
                        surroundedByGrass = false;
                        break;
                    }
                }
                if (surroundedByGrass) {
                    if (mapGrid.getDeposit(y, x) != null) {
                        mapGrid.removeDeposit(y, x);
                    }
                    return new Portal(x, y);
                }

            }

        }
    }

}
