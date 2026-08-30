package net.ivqrydev.valcon.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Boat.class)
public abstract class BoatFallDamageMixin {

    @Inject(method = "checkFallDamage", at = @At("HEAD"))
    private void valcon$forwardFallDamageToPassengers(double y, boolean onGround, BlockState state, BlockPos pos, CallbackInfo ci) {
        if (!onGround) {
            return;
        }

        Boat boat = (Boat) (Object) this;
        float fallDistance = boat.fallDistance;

        if (fallDistance <= 0.0F) {
            return;
        }

        for (Entity passenger : boat.getPassengers()) {
            passenger.causeFallDamage(fallDistance, 1.0F, passenger.damageSources().fall());
        }
    }
}