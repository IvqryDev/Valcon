package net.ivqrydev.valcon.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class RottenEffect extends MobEffect {

    private static final ResourceLocation MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath("valcon", "effect.rotten.critical_chance");

    private static final ResourceLocation CRITICAL_CHANCE_ID =
            ResourceLocation.fromNamespaceAndPath("critical_strike", "chance");

    public RottenEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void addAttributeModifiers(AttributeMap attributeMap, int amplifier) {
        Holder<Attribute> attribute = (Holder<Attribute>) BuiltInRegistries.ATTRIBUTE.getHolder(CRITICAL_CHANCE_ID).orElse(null);
        if (attribute != null) {
            AttributeInstance instance = attributeMap.getInstance(attribute);
            if (instance != null) {
                instance.removeModifier(MODIFIER_ID);
                instance.addOrReplacePermanentModifier(new AttributeModifier(
                        MODIFIER_ID,
                        -1.0,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ));
            }
        }
        super.addAttributeModifiers(attributeMap, amplifier);
    }

    @Override
    public void removeAttributeModifiers(AttributeMap attributeMap) {
        Holder<Attribute> attribute = (Holder<Attribute>) BuiltInRegistries.ATTRIBUTE.getHolder(CRITICAL_CHANCE_ID).orElse(null);
        if (attribute != null) {
            AttributeInstance instance = attributeMap.getInstance(attribute);
            if (instance != null) {
                instance.removeModifier(MODIFIER_ID);
            }
        }
        super.removeAttributeModifiers(attributeMap);
    }
}