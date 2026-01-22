package net.ivqrydev.valcon;

import com.mojang.logging.LogUtils;
import net.ivqrydev.valcon.block.ModBlocks;
import net.ivqrydev.valcon.block.entity.ModBlockEntities;
import net.ivqrydev.valcon.block.entity.renderer.SoulForgeBlockEntityRenderer;
import net.ivqrydev.valcon.item.ModCreativeModeTabs;
import net.ivqrydev.valcon.item.ModItems;
import net.ivqrydev.valcon.sound.ModSounds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import org.slf4j.Logger;

@Mod(Valcon.MOD_ID)
public class Valcon {
    public static final String MOD_ID = "valcon";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Valcon(IEventBus modEventBus) {

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModSounds.register(modEventBus);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ValconClient::onClientSetup);
            modEventBus.addListener(ValconClient::registerBER);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Placeholder
    }

    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.SOUL_FORGE_BE.get(), SoulForgeBlockEntityRenderer::new);
        }
    }
}
