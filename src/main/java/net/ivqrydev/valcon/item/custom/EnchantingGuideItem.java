package net.ivqrydev.valcon.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;

public class EnchantingGuideItem extends Item {

    public EnchantingGuideItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.valcon.enchanting_guide.tooltip.line0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.valcon.enchanting_guide.tooltip.line1").withStyle(ChatFormatting.GRAY));
    }
}
