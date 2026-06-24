package com.example.travelpack.mixin.client;

import com.example.travelpack.client.BedrollStateAccess;
import net.minecraft.client.render.entity.state.HorseEntityRenderState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HorseEntityRenderState.class)
public class HorseEntityRenderStateMixin implements BedrollStateAccess {

    @Unique
    private ItemStack mymod_bedrollStack = ItemStack.EMPTY;

    @Override
    public ItemStack mymod_getBedrollStack() {
        return mymod_bedrollStack;
    }

    @Override
    public void mymod_setBedrollStack(ItemStack stack) {
        mymod_bedrollStack = stack;
    }
}
