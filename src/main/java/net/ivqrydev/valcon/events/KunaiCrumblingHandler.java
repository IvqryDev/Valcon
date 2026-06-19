package net.ivqrydev.valcon.events;

import net.ivqrydev.valcon.effect.ModEffects;
import net.ivqrydev.valcon.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//If you're looking at this code to re-create it, go look at my other mod Crumbling Kunai.
//https://modrinth.com/mod/crumbling-kunai
//It's a lot nicer and has config options. This mod is only meant for my modpack.
@EventBusSubscriber(modid = "valcon")
public class KunaiCrumblingHandler {

    //Obtain the thrown kunai.
    private static final ResourceLocation KUNAI_ENTITY_TYPE =
            ResourceLocation.fromNamespaceAndPath("caverns_and_chasms", "kunai");

    //Start tracking UUID of antagonistic players.
    private static final Map<UUID, UUID> KUNAI_SHOOTERS = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        DamageSource source = event.getSource();

        if (!(source.getDirectEntity() instanceof Projectile projectile)) return;

        ResourceLocation entityTypeId = projectile.getType().builtInRegistryHolder().key().location();
        if (!entityTypeId.equals(KUNAI_ENTITY_TYPE)) return;

        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        target.addEffect(new MobEffectInstance(
                ModEffects.CRUMBLING_EFFECT,
                160,
                0,
                true,
                false
        ));

        if (projectile.getOwner() instanceof Player shooter) {
            KUNAI_SHOOTERS.put(target.getUUID(), shooter.getUUID());
        } else {
            KUNAI_SHOOTERS.remove(target.getUUID());
        }
    }

    @SubscribeEvent
    public static void onEffectExpire(MobEffectEvent.Expired event) {
        if (!event.getEffectInstance().is(ModEffects.CRUMBLING_EFFECT)) return;

        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;

        UUID shooterId = KUNAI_SHOOTERS.remove(entity.getUUID());
        Player shooter = shooterId != null ? entity.level().getPlayerByUUID(shooterId) : null;

        //Check if the UUID is invalid. If it is, fall back onto the enemy's position
        BlockPos soundPos = (shooter != null) ? shooter.blockPosition() : entity.blockPosition();
        SoundSource soundCategory = (shooter != null) ? SoundSource.PLAYERS : entity.getSoundSource();

        //Play the sound
        entity.level().playSound(
                null,
                soundPos,
                ModSounds.REFRESH.get(),
                soundCategory,
                1.0f,
                1.0f
        );
    }
}