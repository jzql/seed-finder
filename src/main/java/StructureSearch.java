import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.featureutils.structure.*;
import kaptainwutax.mcutils.rand.ChunkRand;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.mcutils.util.pos.BPos;
import kaptainwutax.mcutils.util.pos.CPos;
import kaptainwutax.mcutils.util.math.DistanceMetric;
import kaptainwutax.mcutils.util.math.Vec3i;
import kaptainwutax.featureutils.misc.SpawnPoint;

import java.util.ArrayList;

public class StructureSearch {

    public static ArrayList<CPos> getChunks(MCVersion v, Long seed, RegionStructure<?, ?> structure, int searchArea) {
        ArrayList<CPos> chunks = new ArrayList<>();
        OverworldBiomeSource world = new OverworldBiomeSource(v, seed);
        ChunkRand rand = new ChunkRand();

        BPos spawn = SpawnPoint.getApproximateSpawn(world);
        RegionStructure.Data<?> minChunkRegion = structure.at(-searchArea + spawn.getX() >> 4, -searchArea + spawn.getZ() >> 4);
        RegionStructure.Data<?> maxChunkRegion = structure.at(searchArea + spawn.getX() >> 4, searchArea + spawn.getZ() >> 4);

        for (int regionX = minChunkRegion.regionX; regionX <= maxChunkRegion.regionX; regionX++) {
            for (int regionZ = minChunkRegion.regionZ; regionZ <= maxChunkRegion.regionZ; regionZ++) {
                CPos chunkPos = structure.getInRegion(seed, regionX, regionZ, rand);

                if (chunkPos != null) {
                    if (chunkPos.distanceTo(Vec3i.ZERO, DistanceMetric.CHEBYSHEV) <= searchArea >> 4) {
                        chunks.add(chunkPos);
                    }
                }
            }
        }

        return chunks;
    }

}
