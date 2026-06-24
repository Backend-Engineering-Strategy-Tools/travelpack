package com.example.travelpack;

import net.minecraft.item.ItemStack;

/** Implemented by AbstractHorseEntity (via mixin) to expose the synced bedroll item. */
public interface BedrollEquipped {
    ItemStack mymod_getBedrollStack();
}
