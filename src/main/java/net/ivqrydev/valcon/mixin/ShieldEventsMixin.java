package net.ivqrydev.valcon.mixin;

import net.ivqrydev.valcon.enchantment.EnchantmentComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.infernalstudios.shieldexp.access.LivingEntityAccess;
import org.infernalstudios.shieldexp.events.ShieldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ShieldEvents.class, remap = false)
public class ShieldEventsMixin {

    //Tracks whether a parry was active at the start of onLivingHurt, before SE clears it.
    private static final ThreadLocal<Boolean> WAS_PARRYING = ThreadLocal.withInitial(() -> false);

    //Add bonus ticks to the parry window after SE sets it. (For flash enchantment)
    @Inject(method = "onStartUsing", at = @At("RETURN"), remap = false)
    private static void applyFlash(Entity entity, ItemStack stack,
                                   CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof Player player)) return;
        int baseWindow = LivingEntityAccess.get(player).getParryWindow();
        if (baseWindow <= 0) return;

        int[] bonus = {0};
        EnchantmentHelper.runIterationOnItem(stack, (enchHolder, level) -> {
            var effect = enchHolder.value().effects().get(EnchantmentComponents.PARRY_WINDOW_BONUS.get());
            if (effect != null) bonus[0] += (int) effect.process(level, RandomSource.create(), baseWindow);
        });

        if (bonus[0] > 0) LivingEntityAccess.get(player).setParryWindow(baseWindow + bonus[0]);
    }

    //Keep the parry state before SE clears the window mid-method. (For requite enchantment.)
    @Inject(method = "onLivingHurt", at = @At("HEAD"), remap = false)
    private static void captureParryState(LivingEntity entity, DamageSource source, float amount,
                                          CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Player player)
            WAS_PARRYING.set(LivingEntityAccess.get(player).getParryWindow() > 0);
    }

    //Add bonus parry damage after SE handles the hit. (For requite enchantment.)
    @Inject(method = "onLivingHurt", at = @At("RETURN"), remap = false)
    private static void applyRequite(LivingEntity entity, DamageSource source, float amount,
                                     CallbackInfoReturnable<Boolean> cir) {
        if (!Boolean.TRUE.equals(cir.getReturnValue())) return;
        if (!WAS_PARRYING.get()) return;
        WAS_PARRYING.remove();
        if (!(entity instanceof Player player)) return;
        if (!(source.getDirectEntity() instanceof LivingEntity attacker)) return;

        float[] bonus = {0};
        EnchantmentHelper.runIterationOnItem(player.getUseItem(), (enchHolder, level) -> {
            var effect = enchHolder.value().effects().get(EnchantmentComponents.PARRY_FLAT_DAMAGE_BONUS.get());
            if (effect != null) bonus[0] += effect.process(level, RandomSource.create(), 0);
        });

        if (bonus[0] > 0) attacker.hurt(attacker.damageSources().sting(player), bonus[0]);
    }
}