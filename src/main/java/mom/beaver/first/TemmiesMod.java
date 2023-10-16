package mom.beaver.first;

import mom.beaver.first.features.dam.DamFeature;
import mom.beaver.first.features.dam.DamFeatureConfig;
import mom.beaver.first.features.example.ExampleFeature;
import mom.beaver.first.features.example.ExampleFeatureConfig;
import mom.beaver.first.registry.RegisterItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemmiesMod implements ModInitializer {
	public static String MOD_ID = "temmies-mod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	public static final Identifier EXAMPLE_FEATURE_ID = new Identifier("temmies-mod", "example_feature");
	public static Feature<ExampleFeatureConfig> EXAMPLE_FEATURE = new ExampleFeature(ExampleFeatureConfig.CODEC);

	public static final Identifier DAM_FEATURE_ID = new Identifier("temmies-mod", "dam");
	public static Feature<DamFeatureConfig> DAM_FEATURE = new DamFeature(DamFeatureConfig.CODEC);


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		RegisterItems.register();

		Registry.register(Registries.FEATURE, EXAMPLE_FEATURE_ID, EXAMPLE_FEATURE);
		Registry.register(Registries.FEATURE, DAM_FEATURE_ID, DAM_FEATURE);
	}
}