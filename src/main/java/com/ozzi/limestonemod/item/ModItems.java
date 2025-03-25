package com.ozzi.limestonemod.item;

import com.ozzi.limestonemod.LimestoneMod;
import com.ozzi.limestonemod.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class ModItems {
    public static final Item LIMESTONE = registerItem("limestone", new AliasedBlockItem(ModBlocks.LIMESTONE_WIRE, new Item.Settings()));

    public static final Item LIMESTONE_TORCH = registerItem("limestone_torch", new VerticallyAttachableBlockItem(ModBlocks.LIMESTONE_TORCH, ModBlocks.LIMESTONE_WALL_TORCH, new Item.Settings(), Direction.DOWN));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(LimestoneMod.MOD_ID, name), item);
    }
    public static void registerModItems() {
        LimestoneMod.LOGGER.info("Registering Mod items for " + LimestoneMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            entries.add(LIMESTONE);
            entries.add(LIMESTONE_TORCH);
        });
    }
}
