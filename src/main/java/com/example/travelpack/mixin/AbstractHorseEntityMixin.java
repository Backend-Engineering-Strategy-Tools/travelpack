package com.example.travelpack.mixin;

import com.example.travelpack.BedrollEquipped;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
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

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void mymod_writeBedroll(NbtCompound nbt, CallbackInfo ci) {
        if (!((Object) this instanceof HorseEntity)) return;
        if (!mymod_storedBedroll.isEmpty()) {
            RegistryWrapper.WrapperLookup regs = ((AbstractHorseEntity)(Object)this).getWorld().getRegistryManager();
            nbt.put("TravelPackBedroll", mymod_storedBedroll.toNbt(regs));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void mymod_readBedroll(NbtCompound nbt, CallbackInfo ci) {
        if (!((Object) this instanceof HorseEntity)) return;
        if (nbt.contains("TravelPackBedroll", NbtElement.COMPOUND_TYPE)) {
            RegistryWrapper.WrapperLookup regs = ((AbstractHorseEntity)(Object)this).getWorld().getRegistryManager();
            mymod_storedBedroll = ItemStack.fromNbt(regs, nbt.getCompound("TravelPackBedroll"))
                    .orElse(ItemStack.EMPTY);
            ((AbstractHorseEntity)(Object)this).getDataTracker().set(BEDROLL_STACK, mymod_storedBedroll.copy());
        }
    }

    @Override
    public ItemStack mymod_getBedrollStack() {
        return ((AbstractHorseEntity)(Object)this).getDataTracker().get(BEDROLL_STACK);
    }

    @Override
    public void mymod_setBedrollStack(ItemStack stack) {
        mymod_storedBedroll = stack.copy();
        AbstractHorseEntity self = (AbstractHorseEntity)(Object)this;
        if (self.getWorld() instanceof ServerWorld) {
            self.getDataTracker().set(BEDROLL_STACK, stack.copy());
        }
    }
}
