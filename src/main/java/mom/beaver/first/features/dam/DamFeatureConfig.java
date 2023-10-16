package mom.beaver.first.features.dam;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.FeatureConfig;

public record DamFeatureConfig(int number, Identifier lidBlockID) implements FeatureConfig {
    public static Codec<DamFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance ->
                    instance.group(
                                    // you can add as many of these as you want, one for each parameter
                                    Codecs.POSITIVE_INT.fieldOf("number").forGetter(DamFeatureConfig::number),
                                    Identifier.CODEC.fieldOf("lidBlockID").forGetter(DamFeatureConfig::lidBlockID))
                            .apply(instance, DamFeatureConfig::new));
}
