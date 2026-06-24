package com.example.travelpack.screen;

import com.example.travelpack.ModItems;
import com.example.travelpack.TravelPack;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class BedrollSlot extends Slot {
    private static final Identifier BACKGROUND = Identifier.of(TravelPack.MOD_ID, "container/slot/sleeping_bag");

    public BedrollSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(ModItems.SLEEPING_BAG);
    }

    @Override
    public Identifier getBackgroundSprite() {
        return BACKGROUND;
    }
}
