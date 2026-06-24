package com.example.travelpack.mixin;

import com.example.travelpack.BedrollEquipped;
import com.example.travelpack.screen.BedrollSlot;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseScreenHandler.class)
public abstract class HorseScreenHandlerMixin extends ScreenHandler {

    protected HorseScreenHandlerMixin() {
        super(null, 0);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void mymod_addBedrollSlot(int syncId, PlayerInventory playerInventory,
                                      Inventory inventory, AbstractHorseEntity entity,
                                      int columns, CallbackInfo ci) {
        if (!(entity instanceof HorseEntity)) return;

        SimpleInventory bedrollInv = new SimpleInventory(1);

        if (entity.getWorld() instanceof ServerWorld && entity instanceof BedrollEquipped be) {
            ItemStack stored = be.mymod_getBedrollStack();
            if (!stored.isEmpty()) {
                bedrollInv.setStack(0, stored.copy());
            }
            bedrollInv.addListener(inv ->
                    ((BedrollEquipped) entity).mymod_setBedrollStack(inv.getStack(0))
            );
        }

        this.addSlot(new BedrollSlot(bedrollInv, 0, 8, 54));
    }
}
