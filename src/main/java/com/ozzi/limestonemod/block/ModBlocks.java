package com.ozzi.limestonemod.block;

import com.ozzi.limestonemod.LimestoneMod;
import com.ozzi.limestonemod.block.custom.LimestoneBlock;
import com.ozzi.limestonemod.block.custom.LimestoneTorchBlock;
import com.ozzi.limestonemod.block.custom.LimestoneWireBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.function.ToIntFunction;

public class ModBlocks {

    public static final Block LIMESTONE_BLOCK = registerBlock("limestone_block",
            new LimestoneBlock(AbstractBlock.Settings.create().mapColor(MapColor.LIME).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL).solidBlock(Blocks::never)));

    public static final Block LIMESTONE_WIRE = registerBlock("limestone_wire",
            new LimestoneWireBlock(AbstractBlock.Settings.create().noCollision().breakInstantly().pistonBehavior(PistonBehavior.DESTROY)));

    public static final Block LIMESTONE_TORCH = registerBlock("limestone_torch",
            new LimestoneTorchBlock(AbstractBlock.Settings.create().noCollision().breakInstantly().luminance(createLightLevelFromLitBlockState(7)).sounds(BlockSoundGroup.WOOD).pistonBehavior(PistonBehavior.DESTROY)
            )
    );
    public static final Block LIMESTONE_WALL_TORCH = registerBlock("limestone_wall_torch",
            new WallRedstoneTorchBlock(AbstractBlock.Settings.create().noCollision().breakInstantly().luminance(createLightLevelFromLitBlockState(7)).sounds(BlockSoundGroup.WOOD).dropsLike(LIMESTONE_TORCH).pistonBehavior(PistonBehavior.DESTROY)
            )
    );

    public static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) ? litLevel : 0;
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(LimestoneMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(LimestoneMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        LimestoneMod.LOGGER.info("Registering Mod Blocks for " + LimestoneMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            entries.add(LIMESTONE_BLOCK);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(LIMESTONE_BLOCK);
        });
    }
}
