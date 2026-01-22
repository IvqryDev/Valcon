package net.ivqrydev.valcon.block.entity;

import net.ivqrydev.valcon.Valcon;
import net.ivqrydev.valcon.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Valcon.MOD_ID);

    public static final Supplier<BlockEntityType<SoulForgeBlockEntity>> SOUL_FORGE_BE =
            BLOCK_ENTITIES.register("soul_forge_be", () -> BlockEntityType.Builder.of(
                    SoulForgeBlockEntity::new, ModBlocks.SOUL_FORGE.get()).build(null));

    public static final Supplier<BlockEntityType<BastStatueBlockEntity>> BAST_STATUE_BE =
            BLOCK_ENTITIES.register("bast_statue_be", () -> BlockEntityType.Builder.of(
                    BastStatueBlockEntity::new, ModBlocks.BAST_STATUE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}