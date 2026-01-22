package net.ivqrydev.valcon.item;

import net.ivqrydev.valcon.Valcon;
import net.ivqrydev.valcon.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Valcon.MOD_ID);

    public static final Supplier<CreativeModeTab> VALCON_TAB = CREATIVE_MODE_TAB.register("valcon_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ENCHANTING_GUIDE.get()))
                    .title(Component.translatable("creativetab.valcon.valcon_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.SOULBORN_BLADE);
                        output.accept(ModItems.SOUL_STEEL_INGOT);
                        output.accept(ModItems.SOUL_QUARTZ);
                        output.accept(ModItems.SOUL);
                        output.accept(ModItems.MYTHRIL_CORE);
                        output.accept(ModItems.ENCHANTING_GUIDE);
                        output.accept(ModItems.COMPASS_CASTING_MOLD);
                        output.accept(ModItems.LEMBAS);
                        output.accept(ModBlocks.BAST_STATUE);
                        output.accept(ModBlocks.SOUL_FORGE);
                        output.accept(ModItems.ANCIENT_FANG);
                        output.accept(ModItems.ASHEN_STEEL_INGOT);
                        output.accept(ModItems.ASHEN_STEEL_SHEET);
                        output.accept(ModItems.YETI_FUR);
                        output.accept(ModItems.NIGHTMARE_SCRAP);
                        output.accept(ModItems.UMBRAL_SHARD);
                        output.accept(ModItems.STAR_TAKER_MUSIC_DISC);
                        output.accept(ModItems.DEVIL_TRIGGER_MUSIC_DISC);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
