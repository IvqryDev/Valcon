package net.ivqrydev.valcon.block;

import net.ivqrydev.valcon.Valcon;
import net.ivqrydev.valcon.block.custom.BastStatueBlock;
import net.ivqrydev.valcon.block.custom.SoulForgeBlock;
import net.ivqrydev.valcon.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Valcon.MOD_ID);

    public static final DeferredBlock<Block> SOUL_FORGE = registerBlock("soul_forge",
            () -> new SoulForgeBlock(BlockBehaviour.Properties.of()
                    .strength(4.5F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .mapColor(MapColor.COLOR_BLACK)
                    .sound(SoundType.DEEPSLATE)
                    .lightLevel((state) -> 15)
            ));

    public static final DeferredBlock<Block> BAST_STATUE = registerBlock("bast_statue",
            () -> new BastStatueBlock(BlockBehaviour.Properties.of()
                    .strength(3F, 1024F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .mapColor(MapColor.SAND)
                    .sound(SoundType.STONE)
            ));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block, boolean registerItem) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        if (registerItem) {
            registerBlockItem(name, toReturn);
        }
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
