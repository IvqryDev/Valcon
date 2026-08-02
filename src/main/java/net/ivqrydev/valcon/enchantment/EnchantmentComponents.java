package net.ivqrydev.valcon.enchantment;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EnchantmentComponents {

    public static final DeferredRegister.DataComponents TYPES =
            DeferredRegister.createDataComponents(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, "valcon");
    //Flash enchantment.
    public static final Supplier<DataComponentType<EnchantmentValueEffect>> PARRY_WINDOW_BONUS =
            TYPES.registerComponentType("parry_window_bonus",
                    builder -> builder.persistent(EnchantmentValueEffect.CODEC));
    //Requite enchantment.
    public static final Supplier<DataComponentType<EnchantmentValueEffect>> PARRY_FLAT_DAMAGE_BONUS =
            TYPES.registerComponentType("parry_flat_damage_bonus",
                    builder -> builder.persistent(EnchantmentValueEffect.CODEC));
}