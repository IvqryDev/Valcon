package net.ivqrydev.valcon.mixin;

import net.ivqrydev.valcon.enchantment.EnchantmentComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.core.component.DataComponentType;
import org.infernalstudios.shieldexp.config.ShieldExpansionConfig;
import org.infernalstudios.shieldexp.events.ShieldEvents;
import org.infernalstudios.shieldexp.events.TooltipEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = TooltipEvents.class, remap = false)
public class TooltipEventsMixin {

    //Passes the ItemStack into the private addTooltipLine method via ThreadLocal.
    private static final ThreadLocal<ItemStack> CURRENT_STACK = ThreadLocal.withInitial(() -> null);

    @Inject(method = "addTooltip", at = @At("HEAD"), remap = false)
    private static void storeStack(ItemStack stack, Player player, List<Component> tooltip, CallbackInfo ci) {
        CURRENT_STACK.set(stack);
    }

    @Inject(method = "addTooltip", at = @At("RETURN"), remap = false)
    private static void clearStack(ItemStack stack, Player player, List<Component> tooltip, CallbackInfo ci) {
        CURRENT_STACK.remove();
    }

    //Intercepts tooltip line creation to inject enchantment bonuses into parry damage window tooltip.
    @Redirect(
            method = "addTooltipLine",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;"),
            remap = false
    )
    private static MutableComponent injectEnchantmentBonuses(String key, Object[] args) {
        ItemStack stack = CURRENT_STACK.get();
        if (stack != null) {
            Item item = stack.getItem();

            if (key.contains("parry_damage")) {
                int bonus = sumBonus(stack, EnchantmentComponents.PARRY_FLAT_DAMAGE_BONUS.get());
                if (bonus > 0) {
                    double flat = ShieldEvents.getShieldValue(item, "flatDamage") + bonus;
                    double pct = ShieldEvents.getShieldValue(item, "parryDamage") * 100;
                    return Component.translatable(key, (int) flat + " + " + pct + "%")
                            .withStyle(ChatFormatting.DARK_GREEN);
                }
            }

            if (key.contains("parry_ticks")) {
                int bonus = sumBonus(stack, EnchantmentComponents.PARRY_WINDOW_BONUS.get());
                if (bonus > 0) {
                    double baseTicks = ShieldEvents.getShieldValue(item, "parryTicks");
                    if (ShieldExpansionConfig.lenientParryEnabled()) baseTicks *= 2;
                    return Component.translatable(key, String.valueOf((baseTicks + bonus) / 20.0))
                            .withStyle(ChatFormatting.DARK_GREEN);
                }
            }
        }
        return Component.translatable(key, args).withStyle(ChatFormatting.DARK_GREEN);
    }

    //Adds up the total bonus to tooltip values. (For flash and requite enchantment.)
    private static int sumBonus(ItemStack stack, DataComponentType<EnchantmentValueEffect> componentType) {
        int[] total = {0};
        EnchantmentHelper.runIterationOnItem(stack, (enchHolder, level) -> {
            var effect = enchHolder.value().effects().get(componentType);
            if (effect != null) total[0] += (int) effect.process(level, RandomSource.create(), 0);
        });
        return total[0];
    }
}