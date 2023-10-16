package mom.beaver.first.registry;

import mom.beaver.first.TemmiesMod;
import mom.beaver.first.items.BeaverFuelItem;
import mom.beaver.first.items.BeaverSoundsItem;
import mom.beaver.first.items.blocks.BeaverBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RegisterItems {

    private static final Item BEAVER_FUEL = new BeaverFuelItem(new FabricItemSettings());
    private static final BeaverSoundsItem BEAVER_SOUNDS = new BeaverSoundsItem(new FabricItemSettings().maxCount(1));

//    private static final Block BEAVER_BLOCK = new Block(FabricBlockSettings.create().strength(1.5f));
    private static final BeaverBlock BEAVER_BLOCK = new BeaverBlock(FabricBlockSettings.create().strength(1.5f).requiresTool());

    public static void register() {

        // register items and blocks
        Item[] beaver_group_entries = {
                registerItem("beaver_sounds", BEAVER_SOUNDS),
                Items.OAK_WOOD,
                registerItemAndBlock("beaver_block", BEAVER_BLOCK),
                registerItem("beaver_fuel", BEAVER_FUEL),
        };

        // register fuel
        FuelRegistry.INSTANCE.add(BEAVER_FUEL, 1);

        // item group
        Registry.register(Registries.ITEM_GROUP, new Identifier(TemmiesMod.MOD_ID, "beaver_group"),
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(BEAVER_SOUNDS))
                        .displayName(Text.translatable("Beaver Stuff"))
                        .entries((context, entries) -> {
                            for (Item item : beaver_group_entries) {
                                entries.add(item);
                            }
                        })
                        .build()
        );
    }

    private static Item registerItem(String ID, Item item) {
        Registry.register(Registries.ITEM, new Identifier(TemmiesMod.MOD_ID, ID), item);
        return item;
    }

    private static void registerBlock(String ID, Block block) {
        Registry.register(Registries.BLOCK, new Identifier(TemmiesMod.MOD_ID, ID), block);
    }

    private static Item registerItemAndBlock(String ID, Block block) {
        Item item = new BlockItem(block, new FabricItemSettings());
        Registry.register(Registries.BLOCK, new Identifier(TemmiesMod.MOD_ID, ID), block);
        Registry.register(Registries.ITEM, new Identifier(TemmiesMod.MOD_ID, ID), item);
        return item;
    }
}