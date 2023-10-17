package mom.beaver.first.features.dam;

import com.mojang.serialization.Codec;
import mom.beaver.first.TemmiesMod;
import mom.beaver.first.items.blocks.BeaverBlock;
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
import java.util.List;


public class DamFeature extends Feature<DamFeatureConfig> {

    public DamFeature(Codec<DamFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DamFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();

        BlockPos origin = context.getOrigin();

        Random random = context.getRandom();

        DamFeatureConfig config = context.getConfig();

        int number = config.number();
        String lidBlockID = config.lidBlockID().toString();

        // the block to "end the generation" on the y-axis
        Block lidBlock = Blocks.WATER;
        if (lidBlockID.equals("minecraft:air")) {
            lidBlock = Blocks.AIR;
        }

        Block[] replacementBlocks = {
                Blocks.WATER,
                Blocks.SEAGRASS,
                Blocks.TALL_SEAGRASS,
                Blocks.KELP
        };

        int y = 90;
        int topBlock = -255;

        BlockState[] blocks = getDamBlocksInBiome(0);

        BlockPos tempPos = new BlockPos(origin.withY(y));
        BlockState tempBlockState = world.getBlockState(tempPos);

        while (y > -64) {
            if (Arrays.stream(replacementBlocks).toList().contains(world.getBlockState(tempPos).getBlock())) {
                if (topBlock == -255) {
                    topBlock = y;
                }

                blocks = getDamBlocksInBiome(topBlock - y);
                world.setBlockState(
                        tempPos,
                        blocks[(int) Math.floor(Math.random() * blocks.length)],
                        0);

            } else if (topBlock != -255) {
                // place a block on top of the dam
                if (Math.random() > 0.15) {
                    blocks = getDamBlocksInBiome(-1);
                    world.setBlockState(tempPos.withY(topBlock + 1), blocks[(int) Math.floor(Math.random() * blocks.length)], 0);

                    // place another one on top of that one
                    if (Math.random() > 0.75) {
                        blocks = getDamBlocksInBiome(-2);
                        world.setBlockState(tempPos.withY(topBlock + 2), blocks[(int) Math.floor(Math.random() * blocks.length)], 0);
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
    }

    private static BlockState[] getDamBlocksInBiome(int blocksBelowWater) {
        boolean waterLogg = blocksBelowWater>=0;

        BlockState mud = Blocks.MUD.getDefaultState();

        BlockState oak_planks = Blocks.OAK_PLANKS.getDefaultState();
        BlockState oak_trapdoor = Blocks.OAK_TRAPDOOR.getDefaultState();
        BlockState oak_fence = Blocks.OAK_FENCE.getDefaultState().with(Properties.WATERLOGGED, waterLogg);

        BlockState oak_wood = new BlockState[] {
                Blocks.OAK_WOOD.getDefaultState().with(Properties.AXIS, Direction.Axis.Y),
                Blocks.OAK_WOOD.getDefaultState().with(Properties.AXIS, Direction.Axis.X)
        }[new java.util.Random().nextInt(2)];
        BlockState oak_log = new BlockState[] {
                Blocks.OAK_LOG.getDefaultState().with(Properties.AXIS, Direction.Axis.Y),
                Blocks.OAK_LOG.getDefaultState().with(Properties.AXIS, Direction.Axis.X)
        }[new java.util.Random().nextInt(2)];
        BlockState stripped_oak_wood = new BlockState[] {
                Blocks.STRIPPED_OAK_WOOD.getDefaultState().with(Properties.AXIS, Direction.Axis.Y),
                Blocks.STRIPPED_OAK_WOOD.getDefaultState().with(Properties.AXIS, Direction.Axis.X)
        }[new java.util.Random().nextInt(2)];

        BlockState oak_leaves = Blocks.OAK_LEAVES.getDefaultState().with(Properties.WATERLOGGED, waterLogg).with(Properties.PERSISTENT, true);


        return switch (blocksBelowWater) {
            case -2 -> new BlockState[] {oak_fence, oak_fence, oak_fence, oak_fence, oak_fence, oak_leaves, oak_trapdoor, oak_trapdoor};
            case -1 -> new BlockState[] {oak_fence, oak_fence, oak_leaves, oak_leaves, oak_leaves, oak_leaves, oak_wood, oak_log, oak_planks};
            case 0 -> new BlockState[] {oak_fence, stripped_oak_wood, oak_wood, oak_wood, oak_wood, oak_wood, oak_wood, oak_log, oak_log, oak_leaves, oak_leaves, oak_planks, oak_planks};
            case 1 -> new BlockState[] {oak_fence, stripped_oak_wood, oak_wood, oak_wood, oak_wood, oak_wood, oak_log, oak_leaves, mud, oak_planks};
            case 2 -> new BlockState[] {stripped_oak_wood, oak_wood, oak_wood, oak_wood, oak_log, mud};
            case 3 -> new BlockState[] {stripped_oak_wood, oak_wood, oak_wood, oak_log, mud, mud};
            case 4 -> new BlockState[] {oak_wood, oak_wood, oak_log, oak_log, mud, mud};
            default -> new BlockState[] {oak_wood, oak_wood, oak_log, mud, mud};
        };
    }
}
