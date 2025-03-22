package com.ozzi.limestonemod.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class LimestoneBlock extends Block {
    public static final MapCodec<com.ozzi.limestonemod.block.custom.LimestoneBlock> CODEC = createCodec(com.ozzi.limestonemod.block.custom.LimestoneBlock::new);

    @Override
    public MapCodec<com.ozzi.limestonemod.block.custom.LimestoneBlock> getCodec() {
        return CODEC;
    }

    public LimestoneBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 15;
    }
}
