package com.ozzi.limestonemod.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class WallLimestoneTorchBlock extends LimestoneTorchBlock {
    public static final MapCodec<com.ozzi.limestonemod.block.custom.WallLimestoneTorchBlock> CODEC = createCodec(com.ozzi.limestonemod.block.custom.WallLimestoneTorchBlock::new);
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty LIT = LimestoneTorchBlock.LIT;

    private static final Vec3d[] COLORS = Util.make(new Vec3d[16], colors -> {
        for (int i = 0; i <= 15; i++) {
            float f = i / 15.0F;
            float g = MathHelper.clamp(f * f * 0.7F - 0.5F, 0.0F, 1.0F);
            float h = f * 0.6F + (f > 0.0F ? 0.4F : 0.3F);
            float j = MathHelper.clamp(f * f * 0.6F - 0.7F, 0.0F, 1.0F);
            colors[i] = new Vec3d(g, h, j);
        }
    });

    @Override
    public MapCodec<com.ozzi.limestonemod.block.custom.WallLimestoneTorchBlock> getCodec() {
        return CODEC;
    }

    public WallLimestoneTorchBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(LIT, true));
    }

    @Override
    public String getTranslationKey() {
        return this.asItem().getTranslationKey();
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return WallTorchBlock.getBoundingShape(state);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return WallTorchBlock.canPlaceAt(world, pos, state.get(FACING));
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
    ) {
        return direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = Blocks.WALL_TORCH.getPlacementState(ctx);
        return blockState == null ? null : this.getDefaultState().with(FACING, (Direction)blockState.get(FACING));
    }

    private void addPoweredParticles(BlockState state, World world, Random random, BlockPos pos, Vec3d color) {
        Direction direction = ((Direction)state.get(FACING)).getOpposite();
        double d = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2 + 0.27 * direction.getOffsetX();
        double e = pos.getY() + 0.7 + (random.nextDouble() - 0.5) * 0.2 + 0.22;
        double k = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2 + 0.27 * direction.getOffsetZ();
        world.addParticle(new DustParticleEffect(color.toVector3f(), 1.0F), d, e, k, 0.0, 0.0, 0.0);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if ((Boolean)state.get(LIT)) {
            this.addPoweredParticles(state, world, random, pos, COLORS[12]);
        }
    }

    @Override
    protected boolean shouldUnpower(World world, BlockPos pos, BlockState state) {
        Direction direction = ((Direction)state.get(FACING)).getOpposite();
        return world.isEmittingRedstonePower(pos.offset(direction), direction);
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(LIT) && state.get(FACING) != direction ? 15 : 0;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }
}
