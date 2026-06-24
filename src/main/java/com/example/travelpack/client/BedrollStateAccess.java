package com.example.travelpack.client;

import net.minecraft.item.ItemStack;

/** Implemented by HorseEntityRenderState (via mixin) to carry the bedroll item into the render layer. */
public interface BedrollStateAccess {
    ItemStack mymod_getBedrollStack();
    void mymod_setBedrollStack(ItemStack stack);
}
