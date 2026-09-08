package net.ivqrydev.valcon.mixin;

import net.jadenxgamer.netherexp.core.block.TreacherousFireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.spell_engine.api.effect.SpellEngineEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TreacherousFireBlock.class)
public abstract class TreacherousFireBlockMixin {

    //Bleed length.
    private static final int VALCON$BLEED_DURATION_TICKS = 300;
    private static final int VALCON$BLEED_AMPLIFIER = 0;

    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void valcon$treacherousFireEntityInside(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        //Mirrors vanilla fire tick calculation.
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
            if (entity.getRemainingFireTicks() == 0) {
                entity.igniteForSeconds(8.0F);
            }
        }

        //Mirrors vanilla soul fire damage ticking.
        entity.hurt(level.damageSources().inFire(), 2.0F);

        if (!level.isClientSide() && entity.isOnFire() && entity instanceof LivingEntity living) {
            MobEffect bleed = SpellEngineEffects.BLEED.effect;
            Holder<MobEffect> bleedHolder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(bleed);
            living.addEffect(new MobEffectInstance(bleedHolder, VALCON$BLEED_DURATION_TICKS, VALCON$BLEED_AMPLIFIER, false, true, true));
        }

        entity.setRemainingFireTicks(18);

        ci.cancel();
    }
}