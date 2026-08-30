package net.ivqrydev.valcon.compat;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = "valcon")
public final class ArmorDurabilityDefense {

    //Durability floor.
    private static final double LOWER_DURABILITY_CUTOFF = 0.3D;

    //Defense floor.
    private static final double MIN_DEFENSE = 0.5D;

    private static final double DEFENSE_RANGE = 1.0D - MIN_DEFENSE;               // 0.5
    private static final double DURABILITY_RANGE = 1.0D - LOWER_DURABILITY_CUTOFF; // 0.7

    private record SlotModifier(EquipmentSlot slot, ResourceLocation modifierId) {
    }

    private static final SlotModifier[] ARMOR_SLOTS = {
            new SlotModifier(EquipmentSlot.HEAD,  id("head")),
            new SlotModifier(EquipmentSlot.CHEST, id("chest")),
            new SlotModifier(EquipmentSlot.LEGS,  id("legs")),
            new SlotModifier(EquipmentSlot.FEET,  id("feet")),
    };

    private static ResourceLocation id(String slotName) {
        return ResourceLocation.fromNamespaceAndPath("valcon", "armor_durability_scaling_" + slotName);
    }

    private ArmorDurabilityDefense() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        //Fires on both client and server, then sends stat info to the client.
        if (player.level().isClientSide()) {
            return;
        }

        AttributeInstance armorInstance = player.getAttribute(Attributes.ARMOR);
        if (armorInstance == null) {
            return;
        }

        for (SlotModifier entry : ARMOR_SLOTS) {
            ItemStack stack = player.getItemBySlot(entry.slot());
            double reduction = calculateArmorReduction(stack, entry.slot());

            if (reduction == 0.0D) {
                armorInstance.removeModifier(entry.modifierId());
            } else {
                armorInstance.addOrUpdateTransientModifier(
                        new AttributeModifier(entry.modifierId(), reduction, AttributeModifier.Operation.ADD_VALUE)
                );
            }
        }
    }

    //Calculates how much defense to subtract.
    private static double calculateArmorReduction(ItemStack stack, EquipmentSlot slot) {
        if (stack.isEmpty() || !stack.isDamageableItem()) {
            return 0.0D;
        }

        double baseArmor = getBaseArmorForSlot(stack, slot);
        if (baseArmor <= 0.0D) {
            return 0.0D;
        }

        return scaleArmorValue(baseArmor, stack) - baseArmor; // always <= 0
    }

    private static double scaleArmorValue(double baseArmor, ItemStack stack) {
        int maxDamage = stack.getMaxDamage();
        if (maxDamage <= 0) {
            return baseArmor;
        }

        double effectiveness = durabilityToEffectiveness(1.0D - ((double) stack.getDamageValue() / maxDamage));
        if (effectiveness >= 1.0D) {
            return baseArmor;
        }

        double scaledArmor = Math.ceil(baseArmor * effectiveness - 1e-9);
        double minArmor    = Math.ceil(baseArmor * MIN_DEFENSE   - 1e-9);
        return Math.clamp(scaledArmor, minArmor, baseArmor);
    }

    //Maps remaining durability to armor effectiveness.
    private static double durabilityToEffectiveness(double durabilityRemaining) {
        if (durabilityRemaining <= LOWER_DURABILITY_CUTOFF) {
            return MIN_DEFENSE;
        }
        double progress = (durabilityRemaining - LOWER_DURABILITY_CUTOFF) / DURABILITY_RANGE;
        return MIN_DEFENSE + (progress * DEFENSE_RANGE);
    }

    private static double getBaseArmorForSlot(ItemStack stack, EquipmentSlot slot) {
        EquipmentSlotGroup slotGroup = EquipmentSlotGroup.bySlot(slot);
        double total = 0.0D;

        for (ItemAttributeModifiers.Entry entry : stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY).modifiers()) {
            if (entry.attribute().equals(Attributes.ARMOR)
                    && entry.slot().equals(slotGroup)
                    && entry.modifier().operation() == AttributeModifier.Operation.ADD_VALUE) {
                total += entry.modifier().amount();
            }
        }
        return total;
    }

    @SubscribeEvent
    public static void onItemAttributeModifiers(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isDamageableItem() || stack.getMaxDamage() <= 0) {
            return;
        }

        List<ItemAttributeModifiers.Entry> armorEntries = new ArrayList<>();
        for (ItemAttributeModifiers.Entry entry : event.getModifiers()) {
            if (entry.attribute().equals(Attributes.ARMOR)
                    && entry.modifier().operation() == AttributeModifier.Operation.ADD_VALUE) {
                armorEntries.add(entry);
            }
        }

        for (ItemAttributeModifiers.Entry entry : armorEntries) {
            AttributeModifier modifier = entry.modifier();
            double scaledArmor = scaleArmorValue(modifier.amount(), stack);
            if (scaledArmor == modifier.amount()) {
                continue; //Full durability so nothing to rewrite.
            }

            event.replaceModifier(
                    Attributes.ARMOR,
                    new AttributeModifier(modifier.id(), scaledArmor, AttributeModifier.Operation.ADD_VALUE),
                    entry.slot()
            );
        }
    }
}