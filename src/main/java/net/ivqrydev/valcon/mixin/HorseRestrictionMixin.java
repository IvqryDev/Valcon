package net.ivqrydev.valcon.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public class HorseRestrictionMixin {

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void valcon$blockHorseInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (((Object) this).getClass() == Horse.class) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }
}