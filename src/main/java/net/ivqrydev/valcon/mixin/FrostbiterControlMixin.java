package net.ivqrydev.valcon.mixin;

import com.rosemods.windswept.common.entity.Frostbiter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Frostbiter.class)
public abstract class FrostbiterControlMixin implements PlayerRideableJumping {

    @Unique private int dashCooldown = 0;
    @Unique private int dashActiveTicks = 0;
    @Unique private int chargedPower = 0;
    @Unique private float lastDashPower = 0f;
    @Unique private boolean wasJumping = false;
    @Unique private boolean soundPlayedThisDash = false;

    @Shadow public abstract boolean isSaddled();

    @Override public boolean canJump() { return dashCooldown == 0; }
    @Override public void onPlayerJump(int jumpPower) { if (dashCooldown == 0) chargedPower = jumpPower; }
    @Override public void handleStartJump(int jumpPower) { if (dashCooldown == 0) chargedPower = jumpPower; }
    @Override public void handleStopJump() {}
    @Override public int getJumpCooldown() { return 0; }

    @Redirect(
            method = "getControllingPassenger",
            at = @At(value = "INVOKE", target = "Lcom/rosemods/windswept/common/entity/Frostbiter;canBeControlledBy(Lnet/minecraft/world/entity/player/Player;)Z")
    )
    private boolean redirectCanBeControlledBy(Frostbiter instance, Player player) {
        return isSaddled();
    }

    @Redirect(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lcom/rosemods/windswept/common/entity/Frostbiter;hasControllingPassenger()Z")
    )
    private boolean redirectRamCheck(Frostbiter instance) {
        return dashActiveTicks > 0 && instance.hasControllingPassenger();
    }

    @Overwrite
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float strafe = player.xxa * 0.5f;
        float forward = player.zza;
        if (forward != 0f) {
            float len = (float) Math.sqrt(strafe * strafe + forward * forward);
            strafe /= len;
            forward /= len;
        }
        return new Vec3(strafe, 0.0, forward);
    }

    @Inject(method = "tickRidden", at = @At("HEAD"))
    private void onTickRidden(Player player, Vec3 travelVector, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (dashCooldown > 0) dashCooldown--;

        boolean jumping = ((LivingEntityAccessor) player).isJumping();

        if (wasJumping && !jumping && chargedPower > 0 && dashCooldown == 0) {
            lastDashPower = chargedPower / 100f;
            Vec3 look = self.getLookAngle().multiply(1, 0, 1).normalize();
            double speed = self.getAttributeValue(Attributes.MOVEMENT_SPEED);
            self.setDeltaMovement(self.getDeltaMovement().add(look.scale(speed * 14.0 * lastDashPower)));
            dashCooldown = 40;
            dashActiveTicks = 8;
            chargedPower = 0;
            soundPlayedThisDash = false;
        }

        if (dashCooldown > 0) chargedPower = 0;

        wasJumping = jumping;

        if (dashActiveTicks <= 0) return;
        dashActiveTicks--;

        Vec3 pos = self.position();
        for (int i = 0; i < 6; i++) {
            double ox = (self.getRandom().nextDouble() - 0.5) * self.getBbWidth();
            double oz = (self.getRandom().nextDouble() - 0.5) * self.getBbWidth();
            double oy = self.getRandom().nextDouble() * self.getBbHeight() * 0.5;
            self.level().addParticle(ParticleTypes.SNOWFLAKE, pos.x + ox, pos.y + oy, pos.z + oz, 0, 0.05, 0);
        }

        List<LivingEntity> hits = self.level().getEntitiesOfClass(LivingEntity.class, self.getBoundingBox().inflate(1.2), e ->
                e != self && e != player && e.isAlive() && !(e instanceof TamableAnimal t && t.getOwner() == player)
        );

        float damage = lastDashPower * 7f;
        for (LivingEntity entity : hits) {
            Vec3 knockback = self.position().subtract(entity.position()).multiply(-1, 0, -1).normalize().scale(0.6 + lastDashPower * 0.4);
            entity.setDeltaMovement(knockback.add(0, 0.25, 0));
            entity.hurt(self.damageSources().playerAttack(player), damage);
            if (entity instanceof Mob mob) mob.setTarget(player);
        }

        if (!hits.isEmpty() && !soundPlayedThisDash) {
            SoundEvent sound = SoundEvent.createVariableRangeEvent(ResourceLocation.parse("block_factorys_bosses:entity.knight.block"));
            self.level().playSound(null, self.blockPosition(), sound, SoundSource.NEUTRAL, 0.6f, 1f);
            soundPlayedThisDash = true;
        }
    }
}