import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.featureutils.structure.*;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.pos.CPos;

import java.util.*;

public class Searcher {
    MCVersion v;
    public Searcher(MCVersion version) {
        this.v = version;
    }

    public void search(ArrayList<String> biomes, HashMap<RegionStructure<?, ?>, Integer> structures, int searchArea, int numSeeds) {
        Map<RegionStructure<? ,?>, List<CPos>> foundChunks = new HashMap<>();
        int seedCount = 0;

        bottomBits:
        for(long structureSeed = 0; structureSeed < 1L << 48; structureSeed++) {
            for (RegionStructure<?, ?> structure : structures.keySet()) {
                List<CPos> chunks = StructureSearch.getChunks(this.v, structureSeed, structure, searchArea);
                if (chunks.isEmpty() || chunks.size() < structures.get(structure)) continue bottomBits;
                foundChunks.put(structure, chunks);
            }

            upperBits:
            for(long upperBits = 0; upperBits < 1L << 16; upperBits++) {
                long worldSeed = (upperBits << 48) | structureSeed;
                OverworldBiomeSource world = new OverworldBiomeSource(this.v, worldSeed);
                Map<RegionStructure<?, ?>, Integer> counter = new HashMap<>();

                for (RegionStructure<?, ?> structure : structures.keySet()) {
                    counter.put(structure, 0);
                    List<CPos> chunks = foundChunks.get(structure);
                    for (CPos chunk: chunks) {
                        if (!structure.canSpawn(chunk.getX(), chunk.getZ(), world)) continue;
                        counter.put(structure, counter.get(structure)+1);
                    }
                    if (counter.get(structure) < structures.get(structure)) continue upperBits;
                }

                Set<String> biomesSet = BiomesSearch.biomes(this.v, worldSeed, searchArea).keySet();
                if (!biomesSet.containsAll(biomes)) continue;

                System.out.println(worldSeed);
                seedCount++;
                if (seedCount == numSeeds) return;
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<String> biomes = new ArrayList<>();
        biomes.add("mushroom_fields");
        HashMap<RegionStructure<?, ?>, Integer> structures = new HashMap<>();
        structures.put(new Village(MCVersion.v1_16_4), 2);
        structures.put(new Monument(MCVersion.v1_16_4), 1);
        int searchArea = 512;
        new Searcher(MCVersion.v1_16_4).search(biomes, structures, searchArea, 5);
    }
}
