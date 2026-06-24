package com.example.travelpack.mixin;

import com.example.travelpack.TravelPack;
import com.example.travelpack.screen.BedrollSlot;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseScreenHandler.class)
public abstract class HorseScreenHandlerMixin extends ScreenHandler {

    // Compiler stub — never actually called
    protected HorseScreenHandlerMixin() {
        super(null, 0);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void mymod_addBedrollSlot(int syncId, PlayerInventory playerInventory,
                                      Inventory inventory, AbstractHorseEntity entity,
                                      int columns, CallbackInfo ci) {
        TravelPack.LOGGER.info("[MyMod] HorseScreenHandler init — entity: {}, inventory size: {}",
                entity.getClass().getSimpleName(), inventory.size());
        if (entity instanceof HorseEntity && inventory.size() > 1) {
            TravelPack.LOGGER.info("[MyMod] Adding bedroll slot at index 1");
            this.addSlot(new BedrollSlot(inventory, 1, 8, 54));
        }
    }
}
