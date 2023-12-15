package mom.beaver.first;

import mom.beaver.first.features.dam.DamFeature;
import mom.beaver.first.features.dam.DamFeatureConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemmiesMod implements ModInitializer {
	public static String MOD_ID = "temmies-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	public static final Identifier DAM_FEATURE_ID = new Identifier("temmies-mod", "dam");
	public static Feature<DamFeatureConfig> DAM_FEATURE = new DamFeature(DamFeatureConfig.CODEC);

	public static final RegistryKey<PlacedFeature> CUSTOM_DAM_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("temmies-mod", "dam"));


	@Override
	public void onInitialize() {
		LOGGER.info("Loading Temmie's Custom-Structures");

		Registry.register(Registries.FEATURE, DAM_FEATURE_ID, DAM_FEATURE);
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.LOCAL_MODIFICATIONS, CUSTOM_DAM_PLACED_KEY);
	}
}