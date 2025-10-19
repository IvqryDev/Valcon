package net.ivqrydev.valcon.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {
    public static final FoodProperties LEMBAS;

    static {
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(16)
                .saturationModifier(0.8f)
                .alwaysEdible();

        ResourceLocation comfortId = ResourceLocation.fromNamespaceAndPath("farmersdelight", "comfort");
        ResourceLocation nourishmentId = ResourceLocation.fromNamespaceAndPath("farmersdelight", "nourishment");
        MobEffect comfort = BuiltInRegistries.MOB_EFFECT.get(comfortId);
        MobEffect nourishment = BuiltInRegistries.MOB_EFFECT.get(nourishmentId);

        if (comfort != null) {
            BuiltInRegistries.MOB_EFFECT.getResourceKey(comfort)
                    .flatMap(BuiltInRegistries.MOB_EFFECT::getHolder)
                    .ifPresent(holder -> builder.effect(
                            () -> new MobEffectInstance(holder, 6000, 0, false, false, true), 1.0f)); // 5 min
        }

        if (nourishment != null) {
            BuiltInRegistries.MOB_EFFECT.getResourceKey(nourishment)
                    .flatMap(BuiltInRegistries.MOB_EFFECT::getHolder)
                    .ifPresent(holder -> builder.effect(
                            () -> new MobEffectInstance(holder, 2400, 0, false, false, true), 1.0f)); // 2 min
        }

        LEMBAS = builder.build();
    }
}
