package mom.beaver.first.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import mom.beaver.first.helpers.soundHelper;

public class BeaverSoundsItem extends Item {

    public BeaverSoundsItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        float pitch = (float)(Math.random() / 2.0 + 0.75);
        SoundEvent sound = soundHelper.pickRandomSoundEvent(new SoundEvent[] {
            SoundEvents.BLOCK_BAMBOO_WOOD_BREAK,
            SoundEvents.BLOCK_WOOD_BREAK,
            SoundEvents.ITEM_AXE_STRIP,
            SoundEvents.BLOCK_BAMBOO_WOOD_HIT,
            SoundEvents.BLOCK_WOOD_HIT,
            SoundEvents.BLOCK_WOOD_BREAK
        });

        world.playSound(null, BlockPos.ofFloored(playerEntity.getPos()), sound, SoundCategory.PLAYERS, 1.2F, pitch);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}
