import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.featureutils.structure.*;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.mcutils.util.pos.CPos;

import java.util.*;

public class Searcher {

    MCVersion v;
    public Searcher(MCVersion version) {
        this.v = version;
    }

    public void search(ArrayList<String> biomes, HashMap<RegionStructure<?, ?>, Integer> structures, int searchArea, int numSeeds) {
        Map<RegionStructure<? ,?>, List<CPos>> foundChunks = new HashMap<>();
        int seedCount = 0;

        System.out.println("BEGIN SEARCH");

        structureSeed:
        for (long lowerBits = 0; lowerBits < 1L << 48; lowerBits++) {
            for (RegionStructure<?, ?> structure : structures.keySet()) {
                List<CPos> chunks = StructureSearch.getChunks(this.v, lowerBits, structure, searchArea);

                if (chunks.isEmpty() || chunks.size() < structures.get(structure)) {
                    continue structureSeed;
                }
                foundChunks.put(structure, chunks);
            }

            worldSeed:
            for (long upperBits = 0; upperBits < 1L << 16; upperBits++) {
                long worldSeed = (upperBits << 48) | lowerBits;
                OverworldBiomeSource world = new OverworldBiomeSource(this.v, worldSeed);
                Map<RegionStructure<?, ?>, Integer> counter = new HashMap<>();

                for (RegionStructure<?, ?> structure : structures.keySet()) {
                    counter.put(structure, 0);
                    List<CPos> chunks = foundChunks.get(structure);

                    for (CPos chunk: chunks) {
                        if (!structure.canSpawn(chunk.getX(), chunk.getZ(), world)) {
                            continue;
                        }
                        counter.put(structure, counter.get(structure)+1);
                    }

                    if (counter.get(structure) < structures.get(structure)) {
                        continue worldSeed;
                    }
                }

                Set<String> biomesSet = BiomeSearch.biomes(this.v, worldSeed, searchArea).keySet();

                if (!biomesSet.containsAll(biomes)) {
                    continue;
                }

                System.out.println(worldSeed);
                seedCount++;

                if (seedCount == numSeeds) {
                    System.out.println("END SEARCH");
                    return;
                }
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
