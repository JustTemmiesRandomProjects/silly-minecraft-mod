package mom.beaver.first.items.blocks;

import mom.beaver.first.TemmiesMod;
import mom.beaver.first.helpers.soundHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.Console;

public class BeaverBlock extends Block {
    public BeaverBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            SoundEvent sound = soundHelper.pickRandomSoundEvent(new SoundEvent[] {
                    SoundEvents.BLOCK_BAMBOO_WOOD_BREAK,
                    SoundEvents.BLOCK_WOOD_BREAK,
                    SoundEvents.ITEM_AXE_STRIP,
                    SoundEvents.BLOCK_BAMBOO_WOOD_HIT,
                    SoundEvents.BLOCK_WOOD_HIT,
                    SoundEvents.BLOCK_WOOD_BREAK
            });
            world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.2f, 1.4f);
        }

        return ActionResult.SUCCESS;
    }
}
