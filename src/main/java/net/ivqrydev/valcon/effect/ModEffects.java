package net.ivqrydev.valcon.effect;

import net.ivqrydev.valcon.Valcon;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Valcon.MOD_ID);

    public static final Holder<MobEffect> ROTTEN_EFFECT = MOB_EFFECTS.register("rotten",
            () -> new RottenEffect(MobEffectCategory.HARMFUL, 0x772214));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}