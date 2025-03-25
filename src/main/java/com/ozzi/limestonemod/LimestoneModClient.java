package com.ozzi.limestonemod;

import com.ozzi.limestonemod.block.ModBlocks;
import com.ozzi.limestonemod.block.custom.LimestoneWireBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;

public class LimestoneModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LIMESTONE_WIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LIMESTONE_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LIMESTONE_WALL_TORCH, RenderLayer.getCutout());

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (tintIndex == 0) {
                int power = state.get(LimestoneWireBlock.POWER);
                return LimestoneWireBlock.getWireColor(power);
            }
            return 0xFFFFFF;
        }, ModBlocks.LIMESTONE_WIRE);
    }
}
