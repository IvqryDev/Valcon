package net.ivqrydev.valcon.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.ResourceLocation;

public class CrumblingEffect extends MobEffect {

    private static final ResourceLocation MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath("valcon", "effect.crumbling.armor");

    public CrumblingEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(
                Attributes.ARMOR,
                MODIFIER_ID,
                -0.33,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}