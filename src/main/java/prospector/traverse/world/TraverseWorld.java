package prospector.traverse.world;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeForest;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import prospector.shootingstar.version.Version;
import prospector.traverse.config.TraverseConfig;
import prospector.traverse.core.TraverseConstants;
import prospector.traverse.world.biomes.*;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

public class TraverseWorld {

    public static List<TraverseBiomeEntry> biomeList = new ArrayList<>();
    public static Biome autumnalWoodsBiome = new BiomeAutumnalWoods();
    public static Biome woodlandsBiome = new BiomeWoodlands();
    public static Biome miniJungleBiome = new BiomeMiniJungle();
    public static Biome meadowBiome = new BiomeMeadow();
    public static Biome lushSwampBiome = new BiomeGreenSwamp();
    public static Biome redDesertBiome = new BiomeRedDesert();
    public static Biome temperateRainforestBiome = new BiomeTemperateRainforest();
    public static Biome badlandsBiome = new BiomeBadlands();
    public static Biome mountainousDesertBiome = new BiomeMountainousDesert();
    public static Biome rockyPlateauBiome = new BiomeRockyPlateau();
    public static Biome forestedHills = new BiomeForestedHills(BiomeForest.Type.NORMAL, "Forested Hills");
    public static Biome birchForestedHills = new BiomeForestedHills(BiomeForest.Type.BIRCH, "Birch Forested Hills");
    public static Biome autumnalWoodedHills = new BiomeAutumnalWoodedHills();
    public static boolean firstTick = true;

    public static void init() {
        register(new Version(1, 0, 0), autumnalWoodsBiome, BiomeManager.BiomeType.COOL, "autumnal_woods", 8, TraverseConfig.disableAutumnalWoods, FOREST);
        register(new Version(1, 0, 0), woodlandsBiome, BiomeManager.BiomeType.WARM, "woodlands", 9, TraverseConfig.disableWoodlands, PLAINS);
        register(new Version(1, 0, 0), miniJungleBiome, BiomeManager.BiomeType.WARM, "mini_jungle", 3, TraverseConfig.disableMiniJungle, DENSE, JUNGLE, HOT, WET);
        register(new Version(1, 0, 0), meadowBiome, BiomeManager.BiomeType.COOL, "meadow", 7, TraverseConfig.disableMeadow, PLAINS, LUSH, WET);
        register(new Version(1, 0, 0), lushSwampBiome, BiomeManager.BiomeType.WARM, "green_swamp", 6, TraverseConfig.disableLushSwamp, LUSH, WET, SWAMP);
        register(new Version(1, 0, 0), redDesertBiome, BiomeManager.BiomeType.DESERT, "red_desert", 6, TraverseConfig.disableRedDesert, HOT, DRY, SANDY);
        register(new Version(1, 0, 0), temperateRainforestBiome, BiomeManager.BiomeType.COOL, "temperate_rainforest", 8, TraverseConfig.disableTemperateRainforest, FOREST, CONIFEROUS);
        register(new Version(1, 1, 0), badlandsBiome, BiomeManager.BiomeType.WARM, "badlands", 5, TraverseConfig.disableBadlands, PLAINS, DRY, HOT, SPARSE);
        register(new Version(1, 1, 0), mountainousDesertBiome, BiomeManager.BiomeType.DESERT, "mountainous_desert", 2, TraverseConfig.disableMountainousDesert, MOUNTAIN, DRY, HOT, SANDY);
        register(new Version(1, 1, 0), rockyPlateauBiome, BiomeManager.BiomeType.WARM, "rocky_plateau", 4, TraverseConfig.disableRockyPlateau);
        register(new Version(1, 1, 0), forestedHills, BiomeManager.BiomeType.COOL, "forested_hills", 6, TraverseConfig.disableForestedHills, FOREST, HILLS);
        register(new Version(1, 1, 0), birchForestedHills, BiomeManager.BiomeType.COOL, "birch_forested_hills", 2, TraverseConfig.disableBirchForestedHills, FOREST, HILLS);
        register(new Version(1, 1, 0), autumnalWoodedHills, BiomeManager.BiomeType.COOL, "autumnal_wooded_hills", 1, TraverseConfig.disableAutumnalWoodedHills, FOREST, HILLS);
    }

    public static void register(Version versionAdded, Biome biome, BiomeManager.BiomeType type, String name, int weight, boolean disabled, BiomeDictionary.Type... biomeDictTypes) {
        if (!disabled) {
            // canRegister = VersionUtils.isVersionLessOrEqual(versionAdded, TraverseConfig.version);
            biome.setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name));
            GameRegistry.register(biome);
            for (BiomeDictionary.Type biomeDictType : biomeDictTypes) {
                BiomeDictionary.addTypes(biome, biomeDictType);
            }
            biomeList.add(new TraverseBiomeEntry(versionAdded, biome, type, name, weight, biomeDictTypes));
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD) {
            for (TraverseBiomeEntry entry : biomeList) {
                BiomeManager.addBiome(entry.type, new BiomeManager.BiomeEntry(entry.biome, entry.weight));
                BiomeManager.addSpawnBiome(entry.biome);
                System.out.println("Adding " + entry.name);
            }
        }
    }

    @SubscribeEvent
    public static void onWorldUnoad(WorldEvent.Unload event) {
        for (TraverseBiomeEntry entry : biomeList) {
            BiomeManager.removeBiome(entry.type, new BiomeManager.BiomeEntry(entry.biome, entry.weight));
            BiomeManager.removeSpawnBiome(entry.biome);
            System.out.println("Removing " + entry.name);
        }
    }

    public static class TraverseBiomeEntry {
        public Version version;
        public Biome biome;
        public BiomeManager.BiomeType type;
        public String name;
        public int weight;
        public BiomeDictionary.Type[] biomeDictTypes;

        public TraverseBiomeEntry(Version version, Biome biome, BiomeManager.BiomeType type, String name, int weight, BiomeDictionary.Type... biomeDictTypes) {
            this.version = version;
            this.biome = biome;
            this.type = type;
            this.name = name;
            this.weight = weight;
            this.biomeDictTypes = biomeDictTypes;
        }
    }
}
