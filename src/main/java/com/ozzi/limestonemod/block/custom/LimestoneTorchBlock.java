package com.ozzi.limestonemod.block.custom;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class LimestoneTorchBlock extends AbstractOmniTorchBlock {
    public static final MapCodec<com.ozzi.limestonemod.block.custom.LimestoneTorchBlock> CODEC = createCodec(com.ozzi.limestonemod.block.custom.LimestoneTorchBlock::new);
    public static final BooleanProperty LIT = Properties.LIT;
    private static final Map<BlockView, List<com.ozzi.limestonemod.block.custom.LimestoneTorchBlock.BurnoutEntry>> BURNOUT_MAP = new WeakHashMap();

    private static final Vec3d[] COLORS = Util.make(new Vec3d[16], colors -> {
        for (int i = 0; i <= 15; i++) {
            float f = i / 15.0F;
            float g = MathHelper.clamp(f * f * 0.7F - 0.5F, 0.0F, 1.0F);
            float h = f * 0.6F + (f > 0.0F ? 0.4F : 0.3F);
            float j = MathHelper.clamp(f * f * 0.6F - 0.7F, 0.0F, 1.0F);
            colors[i] = new Vec3d(g, h, j);
        }
    });
    public static final int field_31227 = 60;
    public static final int field_31228 = 8;
    public static final int field_31229 = 160;
    private static final int SCHEDULED_TICK_DELAY = 2;

    @Override
    public MapCodec<? extends com.ozzi.limestonemod.block.custom.LimestoneTorchBlock> getCodec() {
        return CODEC;
    }

    public LimestoneTorchBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, true));
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        for (Direction direction : Direction.values()) {
            world.updateNeighborsAlways(pos.offset(direction), this);
        }
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!moved) {
            for (Direction direction : Direction.values()) {
                world.updateNeighborsAlways(pos.offset(direction), this);
            }
        }
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(LIT) && Direction.UP != direction ? 15 : 0;
    }

    protected boolean shouldUnpower(World world, BlockPos pos, BlockState state) {
        return world.isEmittingRedstonePower(pos.down(), Direction.DOWN);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean bl = this.shouldUnpower(world, pos, state);
        List<com.ozzi.limestonemod.block.custom.LimestoneTorchBlock.BurnoutEntry> list = (List<com.ozzi.limestonemod.block.custom.LimestoneTorchBlock.BurnoutEntry>)BURNOUT_MAP.get(world);

        while (list != null && !list.isEmpty() && world.getTime() - ((com.ozzi.limestonemod.block.custom.LimestoneTorchBlock.BurnoutEntry)list.get(0)).time > 60L) {
            list.remove(0);
        }

        if ((Boolean)state.get(LIT)) {
            if (bl) {
                world.setBlockState(pos, state.with(LIT, false), Block.NOTIFY_ALL);
                if (isBurnedOut(world, pos, true)) {
                    world.syncWorldEvent(WorldEvents.REDSTONE_TORCH_BURNS_OUT, pos, 0);
                    world.scheduleBlockTick(pos, world.getBlockState(pos).getBlock(), 160);
                }
            }
        } else if (!bl && !isBurnedOut(world, pos, false)) {
            world.setBlockState(pos, state.with(LIT, true), Block.NOTIFY_ALL);
        }
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if ((Boolean)state.get(LIT) == this.shouldUnpower(world, pos, state) && !world.getBlockTickScheduler().isTicking(pos, this)) {
            world.scheduleBlockTick(pos, this, 2);
        }
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return direction == Direction.DOWN ? state.getWeakRedstonePower(world, pos, direction) : 0;
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    private void addPoweredParticles(World world, Random random, BlockPos pos, Vec3d color) {
        double d = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
        double e = pos.getY() + 0.7 + (random.nextDouble() - 0.5) * 0.2;
        double k = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
        world.addParticle(new DustParticleEffect(color.toVector3f(), 1.0F), d, e, k, 0.0, 0.0, 0.0);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if ((Boolean)state.get(LIT)) {
            this.addPoweredParticles(world, random, pos, COLORS[12]);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    private static boolean isBurnedOut(World world, BlockPos pos, boolean addNew) {
        List<com.ozzi.limestonemod.block.custom.LimestoneTorchBlock.BurnoutEntry> list = (List<com.ozzi.limestonemod.block.custom.LimestoneTorchBlock.BurnoutEntry>)BURNOUT_MAP.computeIfAbsent(world, worldx -> Lists.newArrayList());
        if (addNew) {
            list.add(new com.ozzi.limestonemod.block.custom.LimestoneTorchBlock.BurnoutEntry(pos.toImmutable(), world.getTime()));
        }

        int i = 0;

        for (com.ozzi.limestonemod.block.custom.LimestoneTorchBlock.BurnoutEntry burnoutEntry : list) {
            if (burnoutEntry.pos.equals(pos)) {
                if (++i >= 8) {
                    return true;
                }
            }
        }

        return false;
    }

    public static class BurnoutEntry {
        final BlockPos pos;
        final long time;

        public BurnoutEntry(BlockPos pos, long time) {
            this.pos = pos;
            this.time = time;
        }
    }
}
