package net.ivqrydev.valcon.item;

import net.ivqrydev.valcon.util.ModTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class ModToolTiers {
    public static final Tier SOUL_STEEL = new SimpleTier(ModTags.Blocks.NEEDS_SOUL_STEEL_TOOL,
            1024, 12f, 3f, 22, () -> Ingredient.of(ModItems.SOUL_STEEL_INGOT));
}