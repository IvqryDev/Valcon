package net.ivqrydev.valcon;

import net.ivqrydev.valcon.block.ModBlocks;
import net.ivqrydev.valcon.block.entity.ModBlockEntities;
import net.ivqrydev.valcon.compat.FeatherCloakAccessory;
import net.ivqrydev.valcon.effect.ModEffects;
import net.ivqrydev.valcon.enchantment.EnchantmentComponents;
import net.ivqrydev.valcon.item.ModCreativeModeTabs;
import net.ivqrydev.valcon.item.ModItems;
import net.ivqrydev.valcon.sound.ModSounds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Valcon.MOD_ID)
public class Valcon {

    public static final String MOD_ID = "valcon";

    public Valcon(IEventBus modEventBus) {
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEffects.register(modEventBus);
        EnchantmentComponents.TYPES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ValconClient::onClientSetup);
            modEventBus.addListener(ValconClient::registerBER);
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(FeatherCloakAccessory::register);
    }
}