package net.ivqrydev.valcon.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CompassCastingMoldItem extends Item {

    public CompassCastingMoldItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.valcon.compass_casting_mold.tooltip.line0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.valcon.compass_casting_mold.tooltip.line1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.valcon.compass_casting_mold.tooltip.line2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.valcon.compass_casting_mold.tooltip.line3").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.valcon.compass_casting_mold.tooltip.line4").withStyle(ChatFormatting.GRAY));
    }
}
