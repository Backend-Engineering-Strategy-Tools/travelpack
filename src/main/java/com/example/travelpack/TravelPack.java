package com.example.travelpack;

import com.example.travelpack.block.SleepingBagBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TravelPack implements ModInitializer {
    public static final String MOD_ID = "travelpack";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModBlocks.initialize();
        ModItems.initialize();

        // Allow sleeping bags to act as beds
        EntitySleepEvents.ALLOW_BED.register((entity, sleepingPos, state, vanillaResult) -> {
            if (state.getBlock() instanceof SleepingBagBlock) {
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });

        // Sleeping bags do NOT set the player's spawn point
        EntitySleepEvents.ALLOW_SETTING_SPAWN.register((player, sleepingPos) -> {
            BlockState state = player.getWorld().getBlockState(sleepingPos);
            return !(state.getBlock() instanceof SleepingBagBlock);
        });

        // Player faces the direction the sleeping bag's head points
        EntitySleepEvents.MODIFY_SLEEPING_DIRECTION.register((entity, sleepingPos, sleepingDirection) -> {
            BlockState state = entity.getWorld().getBlockState(sleepingPos);
            if (state.getBlock() instanceof SleepingBagBlock) {
                return state.get(HorizontalFacingBlock.FACING);
            }
            return sleepingDirection;
        });

        LOGGER.info("MyMod initialized!");
    }
}
