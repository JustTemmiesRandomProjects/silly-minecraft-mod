package mom.beaver.first.features.example;

import com.mojang.serialization.Codec;
import mom.beaver.first.TemmiesMod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Arrays;


public class ExampleFeature extends Feature<ExampleFeatureConfig> {

    public ExampleFeature(Codec<ExampleFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<ExampleFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();

        BlockPos origin = context.getOrigin().withY(0);

        Random random = context.getRandom();

        ExampleFeatureConfig config = context.getConfig();

        int number = config.number();

        BlockPos testPos = new BlockPos(origin);

        for (int y = 0; y < world.getHeight(); y++) {
            testPos = testPos.up();
            TemmiesMod.LOGGER.info(testPos.toString());
            TemmiesMod.LOGGER.info(world.getBlockState(testPos).getBlock().toString());
            if (Arrays.stream(new Block[]{Blocks.DIRT, Blocks.GRASS, Blocks.GRASS_BLOCK, Blocks.WATER}).toList().contains(world.getBlockState(testPos).getBlock())) {
                if (world.getBlockState(testPos.up()).isOf(Blocks.AIR)) {
                    for (int i = 0; i < number; i++) {
//                        world.setBlockState(testPos, blockState, 0x10);
                        world.setBlockState(testPos, Blocks.DIRT.getDefaultState(), 0);
                        testPos = testPos.up();

                        if (testPos.getY() >= world.getTopY()) break;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
