import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.pos.BPos;

import java.util.HashMap;
import java.util.Map;

public class BiomesSearch {

    public static Map<String, BPos> biomes(MCVersion v, Long seed, int searchArea, int increments) {
        Map<String, BPos> map = new HashMap<>();
        OverworldBiomeSource world = new OverworldBiomeSource(v, seed);
        BPos spawn = world.getSpawnPoint();

        for (int x = spawn.getX() - searchArea; x < spawn.getX() + searchArea + 1; x+=increments) {
            for (int z = spawn.getZ() - searchArea; z <spawn.getZ() + searchArea + 1; z+=increments) {
                map.put(world.getBiome(x, 0, z).getName(), new BPos(x, 0, z));
            }
        }
        return map;
    }

    public static Map<String, BPos> biomes(MCVersion v, Long seed, int searchArea) {
        return biomes(v, seed, searchArea, 16);
    }
}
