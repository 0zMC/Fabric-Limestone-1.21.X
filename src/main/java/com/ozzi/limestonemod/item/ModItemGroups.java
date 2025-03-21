package com.ozzi.limestonemod.item;

import com.ozzi.limestonemod.LimestoneMod;
import com.ozzi.limestonemod.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup LIMESTONE_BLOCKS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(LimestoneMod.MOD_ID, "limestone_blocks"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.LIMESTONE))
                    .displayName(Text.translatable("itemgroup.tutorialmod.limestone_blocks"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.LIMESTONE);
                        entries.add(ModBlocks.LIMESTONE_BLOCK);
                    }).build());

    public static void registerItemGroups() {
        LimestoneMod.LOGGER.info("Registering Item Groups for " + LimestoneMod.MOD_ID);
    }
}
