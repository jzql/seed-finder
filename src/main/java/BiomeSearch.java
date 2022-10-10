import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.mcutils.util.pos.BPos;
import kaptainwutax.featureutils.misc.SpawnPoint;

import java.util.HashMap;
import java.util.Map;

public class BiomeSearch {

    public static Map<String, BPos> biomes(MCVersion v, Long seed, int searchArea, int increment) {
        Map<String, BPos> map = new HashMap<>();
        OverworldBiomeSource world = new OverworldBiomeSource(v, seed);
        BPos spawn = SpawnPoint.getApproximateSpawn(world);

        for (int x = spawn.getX() - searchArea; x < spawn.getX() + searchArea + 1; x+=increment) {
            for (int z = spawn.getZ() - searchArea; z <spawn.getZ() + searchArea + 1; z+=increment) {
                map.put(world.getBiome(x, 0, z).getName(), new BPos(x, 0, z));
            }
        }

        return map;
    }

    public static Map<String, BPos> biomes(MCVersion v, Long seed, int searchArea) {
        return biomes(v, seed, searchArea, 16);
    }

}
