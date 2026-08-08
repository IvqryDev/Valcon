package net.ivqrydev.valcon.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    //Suppresses all GUI labels on every container, vanilla and modded.
    @Inject(method = "renderLabels", at = @At("HEAD"), cancellable = true)
    private void valcon$suppressLabels(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        ci.cancel();
    }
}