package com.example.travelpack.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SleepingBagBlock extends HorizontalFacingBlock {

    public static final EnumProperty<BedPart> PART = Properties.BED_PART;

    public static final MapCodec<SleepingBagBlock> CODEC = createCodec(SleepingBagBlock::new);

    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 2, 16);

    public SleepingBagBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(PART, BedPart.FOOT));
    }

    @Override
    public MapCodec<SleepingBagBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = ctx.getHorizontalPlayerFacing();
        BlockPos headPos = ctx.getBlockPos().offset(facing);
        World world = ctx.getWorld();
        if (world.getBlockState(headPos).canReplace(ctx) && world.getWorldBorder().contains(headPos)) {
            return getDefaultState().with(FACING, facing).with(PART, BedPart.FOOT);
        }
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            BlockPos headPos = pos.offset(state.get(FACING));
            world.setBlockState(headPos, state.with(PART, BedPart.HEAD), Block.NOTIFY_ALL);
        }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            BlockPos otherPos = otherPartPos(pos, state);
            BlockState otherState = world.getBlockState(otherPos);
            if (otherState.isOf(this) && otherState.get(PART) != state.get(PART)) {
                world.setBlockState(otherPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (player.isSneaking()) {
            if (!world.isClient) {
                BlockPos otherPos = otherPartPos(pos, state);
                BlockPos footPos = state.get(PART) == BedPart.FOOT ? pos : otherPos;
                world.removeBlock(pos, false);
                if (world.getBlockState(otherPos).isOf(this)) {
                    world.removeBlock(otherPos, false);
                }
                ItemScatterer.spawn(world,
                        footPos.getX() + 0.5, footPos.getY() + 0.5, footPos.getZ() + 0.5,
                        new ItemStack(asItem()));
            }
            return ActionResult.SUCCESS;
        }

        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        // Sleep from the head block regardless of which part was clicked
        BlockPos headPos = state.get(PART) == BedPart.FOOT ? pos.offset(state.get(FACING)) : pos;
        player.trySleep(headPos).ifLeft(reason -> {
            if (reason != null) {
                player.sendMessage(reason.getMessage(), true);
            }
        });

        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    private static BlockPos otherPartPos(BlockPos pos, BlockState state) {
        Direction facing = state.get(FACING);
        return state.get(PART) == BedPart.FOOT
                ? pos.offset(facing)
                : pos.offset(facing.getOpposite());
    }
}
