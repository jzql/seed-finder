import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.featureutils.structure.*;
import kaptainwutax.seedutils.mc.ChunkRand;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.pos.BPos;
import kaptainwutax.seedutils.mc.pos.CPos;
import kaptainwutax.seedutils.util.math.DistanceMetric;
import kaptainwutax.seedutils.util.math.Vec3i;

import java.util.ArrayList;

public class StructureSearch {

    private static ArrayList<CPos> search(MCVersion v, Long seed, RegionStructure<?, ?> structure, int searchArea) {
        ArrayList<CPos> chunks = new ArrayList<>();
        OverworldBiomeSource world = new OverworldBiomeSource(v, seed);
        ChunkRand rand = new ChunkRand();

        BPos spawn = world.getSpawnPoint();
        RegionStructure.Data<?> minChunkRegion = structure.at(-searchArea + spawn.getX() >> 4, -searchArea + spawn.getZ() >> 4);
        RegionStructure.Data<?> maxChunkRegion = structure.at(searchArea + spawn.getX() >> 4, searchArea + spawn.getZ() >> 4);

        for (int regionX = minChunkRegion.regionX; regionX <= maxChunkRegion.regionX; regionX++) {
            for (int regionZ = minChunkRegion.regionZ; regionZ <= maxChunkRegion.regionZ; regionZ++) {
                CPos chunkPos = structure.getInRegion(seed, regionX, regionZ, rand);
                if (structure.canSpawn(chunkPos.getX(), chunkPos.getZ(), world)) {
                    if (distance(spawn, chunkPos) <= searchArea) {
                        chunks.add(chunkPos);
                    }
                }
            }
        }
        return chunks;
    }

    private static double distance(BPos spawn, CPos chunk) {
        return Math.sqrt(Math.pow((spawn.getX() - (chunk.getX() << 4)), 2) + Math.pow((spawn.getZ()-(chunk.getZ() << 4)), 2));
    }

    public static ArrayList<CPos> getChunks(MCVersion v, Long seed, RegionStructure<?, ?> structure, int searchArea) {
        ArrayList<CPos> chunks = new ArrayList<>();
        OverworldBiomeSource world = new OverworldBiomeSource(v, seed);
        ChunkRand rand = new ChunkRand();

        BPos spawn = world.getSpawnPoint();
        RegionStructure.Data<?> minChunkRegion = structure.at(-searchArea + spawn.getX() >> 4, -searchArea + spawn.getZ() >> 4);
        RegionStructure.Data<?> maxChunkRegion = structure.at(searchArea + spawn.getX() >> 4, searchArea + spawn.getZ() >> 4);

        for (int regionX = minChunkRegion.regionX; regionX <= maxChunkRegion.regionX; regionX++) {
            for (int regionZ = minChunkRegion.regionZ; regionZ <= maxChunkRegion.regionZ; regionZ++) {
                CPos chunkPos = structure.getInRegion(seed, regionX, regionZ, rand);
                if (chunkPos == null) continue;
                if (chunkPos.distanceTo(Vec3i.ZERO, DistanceMetric.CHEBYSHEV) > searchArea >> 4) continue;
                chunks.add(chunkPos);
            }
        }
        return chunks;
    }
    
}
