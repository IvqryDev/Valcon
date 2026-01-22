package net.ivqrydev.valcon.util;

import net.ivqrydev.valcon.Valcon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_SOUL_STEEL_TOOL = createTag("needs_soul_steel_tool");
        public static final TagKey<Block> NEEDS_ANCIENT_TOOL = createTag("needs_ancient_tool");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Valcon.MOD_ID, name));
        }
    }
}