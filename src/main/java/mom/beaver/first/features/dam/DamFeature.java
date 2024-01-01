package mom.beaver.first.features.dam;

import com.mojang.serialization.Codec;
import mom.beaver.first.TemmiesMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.Blocks;

import java.util.*;


public class DamFeature extends Feature<DamFeatureConfig> {

    public DamFeature(Codec<DamFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DamFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();

        if (!world.getBiome(context.getOrigin()).isIn(BiomeTags.IS_RIVER)) {
            return false;
        }

        BlockPos origin = context.getOrigin();

        Random random = context.getRandom();

        DamFeatureConfig config = context.getConfig();

        int number = config.number();

        Block[] replacementBlocks = {
                Blocks.WATER,
                Blocks.SEAGRASS,
                Blocks.TALL_SEAGRASS,
                Blocks.KELP
        };

        int y = 90;
        int topBlock = -255;

        while (y > 40) {
            BlockPos tempPos = new BlockPos(origin.withY(y));
            if (Arrays.stream(replacementBlocks).toList().contains(world.getBlockState(tempPos).getBlock())) {
                topBlock = y;
                break;
            }
            y --;
        }

        if (y == 40) {
            return false;
        }

        origin = origin.withY(y);
        BlockPos guessPos = null;
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        int distance = 0;
        float angle_radians = 0;


        outerLoop:
        for (int i = 1; i < 16; i ++) {
            for (float r = 0; r < Math.PI * 2; r += (float) (Math.PI * 1/6)) {
                guessPos = BlockAtOffsetPositionAndAngle(origin, i, r);
                if (world.getBlockState(guessPos).getBlock() == Blocks.ICE) { return false; }
                if (!Arrays.stream(replacementBlocks).toList().contains(world.getBlockState(guessPos).getBlock())) {
                    distance = i;
                    angle_radians = r;
                    pos1 = guessPos;
                    break outerLoop;
                }
            }
        }

        if (distance > 15) {
            return false;
        }

        if (pos1 == null) {
//            System.out.println("failed placing dam...");
            return false;
        }

        float inverse_angle_radians = (int) (Math.PI * 2 -angle_radians);

        for (int i = distance; i < 40; i ++) {
            guessPos = BlockAtOffsetPositionAndAngle(pos1, i, inverse_angle_radians);
            if (!Arrays.stream(replacementBlocks).toList().contains(world.getBlockState(guessPos).getBlock())) {
                distance = i;
                pos2 = guessPos;
                break;
            }
        }

        if (distance >= 35) {
            return false;
        }

        if (distance <= 8) {
            return false;
        }

        if (pos2 == null) {
//            System.out.println("failed placing dam... ");
            return false;
        }

        System.out.println("-----------------------------");
        System.out.println(pos1 + " ; " + world.getBlockState(guessPos).getBlock());
        System.out.println(pos2 + " ; " + world.getBlockState(guessPos).getBlock());

        BlockState blockState = Blocks.STONE.getDefaultState(); // Change to the block you want to place

        placeLineBetweenPoints(world, pos1, pos2, blockState, topBlock, replacementBlocks);
        return true;
    };

    private static boolean placeDamTile(int topBlock, Block[] replacementBlocks, BlockPos position, StructureWorldAccess world) {
        int y = topBlock;
        BlockPos tempPos = new BlockPos(position.withY(y));
        BlockState tempBlockState = world.getBlockState(tempPos);

        BlockState[] blocks = getDamBlocksInBiomeAndLocation(0);

        while (y > -64) {
            if (Arrays.stream(replacementBlocks).toList().contains(world.getBlockState(tempPos).getBlock())) {
                blocks = getDamBlocksInBiomeAndLocation(topBlock - y);
                world.setBlockState(
                        tempPos,
                        blocks[(int) Math.floor(Math.random() * blocks.length)],
                        3);
            } else {
                // place a block on top of the dam
                if (Math.random() > (0.25 - (topBlock - y) * 0.015)) {
                    blocks = getDamBlocksInBiomeAndLocation(-1);
                    world.setBlockState(tempPos.withY(topBlock + 1), blocks[(int) Math.floor(Math.random() * blocks.length)], 3);

                    // place another one on top of that one
                    if (Math.random() > 1.0 - (topBlock - y) * 0.05) {
                        blocks = getDamBlocksInBiomeAndLocation(-2);
                        world.setBlockState(tempPos.withY(topBlock + 2), blocks[(int) Math.floor(Math.random() * blocks.length)], 3);
                    }
                }
                return true;
            }
            y -= 1;
            tempPos = tempPos.withY(y);
        }
        return false;
    };

     private static BlockState[] getDamBlocksInBiomeAndLocation(int blocksBelowWater) {
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

    private static BlockPos BlockAtOffsetPositionAndAngle (BlockPos origin, int distance, float angle_radians) {
        return origin.add(
            (int) (distance * Math.cos(angle_radians)),
            0,
            (int) (distance * Math.sin(angle_radians))
        );
    }

    private static void placeLineBetweenPoints(StructureWorldAccess world, BlockPos startPos, BlockPos endPos, BlockState blockState, int topBlock, Block[] replacementBlocks) {
        int x1 = startPos.getX();
        int z1 = startPos.getZ();
        int x2 = endPos.getX();
        int z2 = endPos.getZ();

        double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (z2 - z1) * (z2 - z1));

        for (int i = 0; i < distance; i++) {
            double t = i / distance;
            int x = (int) Math.round(x1 + (x2 - x1) * t);
            int z = (int) Math.round(z1 + (z2 - z1) * t);
            BlockPos pos = new BlockPos(x, topBlock, z);

            placeDamTile(topBlock, replacementBlocks, pos, world);
            placeDamTile(topBlock, replacementBlocks, pos.add(1, 0, 0), world);
        }
    }
}
