package test_gioco.demo.services;

import java.util.Random;

import org.springframework.stereotype.Service;

import test_gioco.demo.classes.map.Tile;
import test_gioco.demo.enums.TerrainType;

@Service
public class TileFactory {

    private final Random random = new Random();

    public Tile create(TerrainType type) {
        int variant = 0;

        if (type == TerrainType.GRASS || type == TerrainType.FOREST
                || type == TerrainType.HILL) {

            variant = random.nextInt(3) + 1;
        }

        return new Tile(type, variant);
    }

}
