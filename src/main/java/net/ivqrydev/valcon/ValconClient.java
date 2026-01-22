package net.ivqrydev.valcon;

import net.ivqrydev.valcon.block.ModBlocks;
import net.ivqrydev.valcon.block.entity.ModBlockEntities;
import net.ivqrydev.valcon.block.entity.renderer.SoulForgeBlockEntityRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ValconClient {
    @SuppressWarnings("deprecation")
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.SOUL_FORGE_BE.get(), SoulForgeBlockEntityRenderer::new);
    }
}
