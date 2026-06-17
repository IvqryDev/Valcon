package net.ivqrydev.valcon.events;

import net.ivqrydev.valcon.effect.ModEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = "valcon")
public class KunaiCrumblingHandler {

    private static final ResourceLocation KUNAI_ENTITY_TYPE =
            ResourceLocation.fromNamespaceAndPath("caverns_and_chasms", "kunai");

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        DamageSource source = event.getSource();

        if (!(source.getDirectEntity() instanceof Projectile projectile)) return;

        ResourceLocation entityTypeId = projectile.getType().builtInRegistryHolder().key().location();
        if (!entityTypeId.equals(KUNAI_ENTITY_TYPE)) return;

        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        target.addEffect(new MobEffectInstance(ModEffects.CRUMBLING_EFFECT, 160, 0));
    }
}