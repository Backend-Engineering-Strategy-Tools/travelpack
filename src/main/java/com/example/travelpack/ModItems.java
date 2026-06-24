package com.example.travelpack;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModItems {

    public static final Item SLEEPING_BAG = register("sleeping_bag",
            key -> new BlockItem(ModBlocks.SLEEPING_BAG, new Item.Settings().registryKey(key).maxCount(16)) {
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context,
                                          List<Text> tooltip, TooltipType type) {
                    tooltip.add(Text.translatable("item.travelpack.sleeping_bag.tooltip")
                            .formatted(Formatting.GRAY));
                }
            }
    );

    private static Item register(String name, java.util.function.Function<RegistryKey<Item>, Item> factory) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TravelPack.MOD_ID, name));
        return Registry.register(Registries.ITEM, key, factory.apply(key));
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries ->
                entries.add(SLEEPING_BAG)
        );
    }
}
