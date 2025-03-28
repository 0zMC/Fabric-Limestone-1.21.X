package com.ozzi.limestonemod;

import com.ozzi.limestonemod.block.ModBlocks;
import com.ozzi.limestonemod.item.ModItemGroups;
import com.ozzi.limestonemod.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LimestoneMod implements ModInitializer {
	public static final String MOD_ID = "limestonemod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}