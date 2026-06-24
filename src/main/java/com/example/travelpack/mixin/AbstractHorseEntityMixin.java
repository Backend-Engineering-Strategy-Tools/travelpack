package com.example.travelpack.mixin;

import com.example.travelpack.BedrollEquipped;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin implements BedrollEquipped {

    @Unique
    private static final TrackedData<ItemStack> BEDROLL_STACK =
            DataTracker.registerData(AbstractHorseEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    @Unique
    private ItemStack mymod_storedBedroll = ItemStack.EMPTY;

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void mymod_addBedrollTracking(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(BEDROLL_STACK, ItemStack.EMPTY);
    }

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    private void mymod_writeBedroll(WriteView view, CallbackInfo ci) {
        if (!((Object) this instanceof HorseEntity)) return;
        if (!mymod_storedBedroll.isEmpty()) {
            view.put("TravelPackBedroll", ItemStack.OPTIONAL_CODEC, mymod_storedBedroll);
        }
    }

    @Inject(method = "readCustomData", at = @At("TAIL"))
    private void mymod_readBedroll(ReadView view, CallbackInfo ci) {
        if (!((Object) this instanceof HorseEntity)) return;
        view.read("TravelPackBedroll", ItemStack.OPTIONAL_CODEC).ifPresent(loaded -> {
            mymod_storedBedroll = loaded;
            ((AbstractHorseEntity)(Object)this).getDataTracker().set(BEDROLL_STACK, loaded.copy());
        });
    }

    @Override
    public ItemStack mymod_getBedrollStack() {
        return ((AbstractHorseEntity)(Object)this).getDataTracker().get(BEDROLL_STACK);
    }

    @Override
    public void mymod_setBedrollStack(ItemStack stack) {
        mymod_storedBedroll = stack.copy();
        AbstractHorseEntity self = (AbstractHorseEntity)(Object)this;
        if (self.getEntityWorld() instanceof ServerWorld) {
            self.getDataTracker().set(BEDROLL_STACK, stack.copy());
        }
    }
}
