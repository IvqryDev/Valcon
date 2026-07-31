package net.ivqrydev.valcon.mixin;

import com.rosemods.windswept.common.entity.Frostbiter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Frostbiter.class)
@SuppressWarnings("unused")
public abstract class FrostbiterControlMixin {

    @Shadow public abstract boolean isSaddled();

    @Overwrite
    private boolean canBeControlledBy(Player player) { return this.isSaddled(); }

    @Overwrite
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) { return new Vec3(player.xxa * 0.5, 0.0, player.zza); }
}