package com.ozzi.limestonemod.item;

import com.ozzi.limestonemod.LimestoneMod;
import com.ozzi.limestonemod.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item LIMESTONE = registerItem("limestone", new AliasedBlockItem(ModBlocks.LIMESTONE_WIRE, new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(LimestoneMod.MOD_ID, name), item);
    }
    public static void registerModItems() {
        LimestoneMod.LOGGER.info("Registering Mod items for " + LimestoneMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            entries.add(LIMESTONE);
        });
    }
}
