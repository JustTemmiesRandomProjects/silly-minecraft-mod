package mom.beaver.first.features.example;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.FeatureConfig;

public record ExampleFeatureConfig(int number) implements FeatureConfig {
    public static Codec<ExampleFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance ->
                    instance.group(
                                    // you can add as many of these as you want, one for each parameter
                                    Codecs.POSITIVE_INT.fieldOf("number").forGetter(ExampleFeatureConfig::number))
                            .apply(instance, ExampleFeatureConfig::new));
}
