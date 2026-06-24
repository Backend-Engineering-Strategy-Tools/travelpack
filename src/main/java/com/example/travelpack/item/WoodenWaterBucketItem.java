package com.example.travelpack.item;

import com.example.travelpack.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WoodenWaterBucketItem extends BucketItem {

    public WoodenWaterBucketItem(Item.Settings settings) {
        super(Fluids.WATER, settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        HitResult hit = user.raycast(5.0, 0.0f, false);
        if (!(hit instanceof BlockHitResult blockHit)) return ActionResult.PASS;

        // Offset by the hit face so fluid goes in front of the clicked block
        BlockPos targetPos = blockHit.getBlockPos().offset(blockHit.getSide());

        if (placeFluid(user, world, targetPos, blockHit)) {
            if (world instanceof ServerWorld) {
                giveItem(user, hand, new ItemStack(ModItems.WOODEN_BUCKET));
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
