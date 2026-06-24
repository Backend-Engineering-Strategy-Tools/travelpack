package com.example.travelpack.mixin;

import com.example.travelpack.BedrollEquipped;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin implements BedrollEquipped {

    @Unique
    private static final TrackedData<ItemStack> BEDROLL_STACK =
            DataTracker.registerData(AbstractHorseEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void mymod_addBedrollTracking(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(BEDROLL_STACK, ItemStack.EMPTY);
    }

    /**
     * Inject into the STATIC getInventorySize(int columns) so both the server entity
     * AND the client-side screen-handler factory see the larger size.
     * columns == 0 means "no chest" (regular horses, zombie/skeleton horses, llamas
     * without chest) — we add one extra slot for the bedroll.
     */
    @Inject(method = "getInventorySize(I)I", at = @At("RETURN"), cancellable = true)
    private static void mymod_increaseInventorySize(int columns, CallbackInfoReturnable<Integer> cir) {
        if (columns == 0) {
            int was = cir.getReturnValue();
            cir.setReturnValue(was + 1);
        }
    }

    /** Keep the synced TrackedData in step with the actual inventory slot. */
    @Inject(method = "onInventoryChanged", at = @At("HEAD"))
    private void mymod_syncBedrollItem(Inventory inventory, CallbackInfo ci) {
        AbstractHorseEntity self = (AbstractHorseEntity) (Object) this;
        if (self.getWorld() instanceof ServerWorld && self instanceof HorseEntity && inventory.size() > 1) {
            self.getDataTracker().set(BEDROLL_STACK, inventory.getStack(1).copy());
        }
    }

    @Override
    public ItemStack mymod_getBedrollStack() {
        return ((AbstractHorseEntity) (Object) this).getDataTracker().get(BEDROLL_STACK);
    }
}
