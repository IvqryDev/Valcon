package net.ivqrydev.valcon.block;

import net.ivqrydev.valcon.Valcon;
import net.ivqrydev.valcon.block.custom.BastStatueBlock;
import net.ivqrydev.valcon.block.custom.BloodflameSconceBlock;
import net.ivqrydev.valcon.block.custom.BloodflameSconceWallBlock;
import net.ivqrydev.valcon.block.custom.SoulForgeBlock;
import net.ivqrydev.valcon.item.ModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
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

    public static final DeferredBlock<FlowerBlock> ATHELAS = registerBlock("athelas",
            () -> new FlowerBlock(MobEffects.REGENERATION, 6, BlockBehaviour.Properties.of()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<RotatedPillarBlock> IRONWOOD_LOG = registerBlock("ironwood_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.BASALT)
            ));

    public static final DeferredBlock<BloodflameSconceBlock> BLOODFLAME_SCONCE = BLOCKS.register("bloodflame_sconce",
            () -> new BloodflameSconceBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .instabreak()
                    .lightLevel((state) -> 9)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)
            ));

    public static final DeferredBlock<BloodflameSconceWallBlock> BLOODFLAME_SCONCE_WALL = BLOCKS.register("bloodflame_sconce_wall",
            () -> new BloodflameSconceWallBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .instabreak()
                    .lightLevel((state) -> 9)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)
                    .dropsLike(BLOODFLAME_SCONCE.get())
            ));

    static {
        ModItems.ITEMS.register("bloodflame_sconce", () -> new StandingAndWallBlockItem(
                BLOODFLAME_SCONCE.get(), BLOODFLAME_SCONCE_WALL.get(), new Item.Properties(), Direction.DOWN));
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}