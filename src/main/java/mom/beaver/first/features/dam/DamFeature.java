package mom.beaver.first.features.dam;

import com.mojang.serialization.Codec;
import mom.beaver.first.TemmiesMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Arrays;


public class DamFeature extends Feature<DamFeatureConfig> {

    public DamFeature(Codec<DamFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DamFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();

        BlockPos origin = context.getOrigin().withY(62);

        Random random = context.getRandom();

        DamFeatureConfig config = context.getConfig();

        int number = config.number();
        String lidBlockID = config.lidBlockID().toString();

        // the block to "end the generation" on the y-axis
        Block lidBlock = Blocks.WATER;
        if (lidBlockID.equals("minecraft:air")) {
            lidBlock = Blocks.AIR;
        }

        BlockState[] blocks = getDamBlocksInBiome();

        Block[] replacementBlocks = {
                Blocks.WATER,
                Blocks.SEAGRASS,
                Blocks.TALL_SEAGRASS,
                Blocks.KELP
        };

        int y = 90;
        int topBlock = -255;

        BlockPos tempPos = new BlockPos(origin.withY(y));
        BlockState tempBlockState = world.getBlockState(tempPos);

        while (y > -64) {
            if (Arrays.stream(replacementBlocks).toList().contains(world.getBlockState(tempPos).getBlock())) {
                if (topBlock == -255) {
                    topBlock = y;
                }
                world.setBlockState(
                        tempPos,
                        blocks[(int) Math.floor(Math.random() * blocks.length)],
                        0);

            } else if (topBlock != -255) {

                // place a block on top of the dam
                TemmiesMod.LOGGER.info("hi");
                if (Math.random() > 0.15) {
                    TemmiesMod.LOGGER.info("place a top block");
                    world.setBlockState(tempPos.withY((int)topBlock + 1), blocks[(int) Math.floor(Math.random() * blocks.length)], 0);
                    // place another one on top of that one
                    if (Math.random() > 0.75) {
                            world.setBlockState(tempPos.withY((int)topBlock + 2), blocks[(int) Math.floor(Math.random() * blocks.length)], 0);
                    }
                }
                return true;
            } else if (y < 60) {
                return false;
            }

            y = y - 1;
            tempPos = tempPos.withY(y);
        }
        return false;

//        for (int y = 62; y < 2; y = y - 1) {
//            if (world.getBlockState(tempPos).isIn(BlockTags.DIRT)) {
//                if (world.getBlockState(tempPos.up()).isOf(lidBlock)) {
//                    for (int i = 0; i < number; i ++) {
//                        if (world.getBlockState(tempPos).isOf(lidBlock)) {
//
//
//                            tempPos = tempPos.up();
//                        }
//
//                        if (tempPos.getY() >= world.getTopY()) break;
//                    }
//                }
//            }
//        }
    }
    private static BlockState[] getDamBlocksInBiome() {
        return new BlockState[] {
                Blocks.MUD.getDefaultState(),
                Blocks.MUD.getDefaultState(),

                Blocks.OAK_FENCE.getDefaultState(),
                Blocks.OAK_PLANKS.getDefaultState(),

                Blocks.OAK_WOOD.getDefaultState().with(Properties.AXIS, Direction.Axis.Y),
                Blocks.OAK_WOOD.getDefaultState().with(Properties.AXIS, Direction.Axis.X),
                Blocks.STRIPPED_OAK_WOOD.getDefaultState().with(Properties.AXIS, Direction.Axis.Y),
                Blocks.STRIPPED_OAK_WOOD.getDefaultState().with(Properties.AXIS, Direction.Axis.X),

                Blocks.OAK_LEAVES.getDefaultState().with(Properties.WATERLOGGED, true).with(Properties.PERSISTENT, true),
                Blocks.OAK_LEAVES.getDefaultState().with(Properties.WATERLOGGED, true).with(Properties.PERSISTENT, true)
        };
    }
}
