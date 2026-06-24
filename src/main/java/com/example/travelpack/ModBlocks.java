package com.example.travelpack;

import com.example.travelpack.block.SleepingBagBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block SLEEPING_BAG = register("sleeping_bag",
            key -> new SleepingBagBlock(AbstractBlock.Settings.create()
                    .registryKey(key)
                    .mapColor(MapColor.RED)
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
            )
    );

    private static Block register(String name, java.util.function.Function<RegistryKey<Block>, Block> factory) {
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(TravelPack.MOD_ID, name));
        return Registry.register(Registries.BLOCK, key, factory.apply(key));
    }

    public static void initialize() {}
}
