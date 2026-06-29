package com.example.travelpack.item;

import com.example.travelpack.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WoodenBucketItem extends Item {

    public WoodenBucketItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        HitResult hit = user.raycast(5.0, 0.0f, true);
        if (!(hit instanceof BlockHitResult blockHit)) return ActionResult.PASS;

        BlockPos pos = blockHit.getBlockPos();
        BlockState state = world.getBlockState(pos);
        FluidState fluid = state.getFluidState();

        if (fluid.isStill() && fluid.isOf(Fluids.WATER) && state.isOf(Blocks.WATER)) {
            if (world instanceof ServerWorld) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        SoundEvents.ITEM_BUCKET_FILL, SoundCategory.PLAYERS, 1.0f, 1.0f);
                giveItem(user, hand, new ItemStack(ModItems.WOODEN_WATER_BUCKET));
            }
            return ActionResult.SUCCESS;
        }

        if (fluid.isStill() && fluid.isOf(Fluids.LAVA)) {
            if (world instanceof ServerWorld && !user.isCreative()) {
                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1.0f, 1.0f);
                user.getStackInHand(hand).decrement(1);
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private static void giveItem(PlayerEntity user, Hand hand, ItemStack newStack) {
        ItemStack held = user.getStackInHand(hand);
        if (held.getCount() == 1) {
            user.setStackInHand(hand, newStack);
        } else {
            held.decrement(1);
            user.giveItemStack(newStack);
        }
    }
}
