package net.ivqrydev.valcon.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsjewelry.core.bonuses.AttributeBonusType;
import io.redspace.ironsjewelry.core.data.BonusInstance;
import io.redspace.ironsjewelry.core.data.JewelryData;
import io.redspace.ironsjewelry.item.CurioBaseItem;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mixin(CurioBaseItem.class)
public class CurioBaseItemMixin {

    @Inject(method = "getAttributeModifiers", at = @At("HEAD"), cancellable = true)
    private void valcon$applyBonusesInAnySlot(SlotContext slotContext, ResourceLocation id, ItemStack stack, CallbackInfoReturnable<Multimap<Holder<Attribute>, AttributeModifier>> cir) {
        JewelryData data = JewelryData.getNullable(stack);
        if (data == null) {
            return;
        }
        Map<Holder<Attribute>, Map<AttributeModifier.Operation, AttributeModifier>> collapsedModifiers = new HashMap<>();
        for (BonusInstance instance : data.getBonuses()) {
            if (instance.bonusType() instanceof AttributeBonusType attributeBonus) {
                attributeBonus.getParameterType().resolve(instance.parameter()).ifPresent(attributeInstance -> {
                    var byOperation = collapsedModifiers.computeIfAbsent(attributeInstance.attribute(), key -> new HashMap<>());
                    var modifier = attributeBonus.modifier(attributeInstance, slotContext, instance.quality());
                    var operation = modifier.operation();
                    var existing = byOperation.get(operation);
                    byOperation.put(operation, existing == null ? modifier : new AttributeModifier(existing.id(), existing.amount() + modifier.amount(), operation));
                });
            }
        }
        ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();
        for (var entry : collapsedModifiers.entrySet()) {
            builder.putAll(entry.getKey(), entry.getValue().values());
        }
        cir.setReturnValue(builder.build());
    }

    //Tooltip removal
    @Redirect(
            method = "getAttributesTooltip",
            at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z")
    )
    private boolean valcon$removeShiftPrompt(ArrayList<Component> shiftTooltip, Object promptComponent) {
        return false;
    }

    @Redirect(
            method = "getAttributesTooltip",
            at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;addAll(Ljava/util/Collection;)Z", ordinal = 0)
    )
    private boolean valcon$removeShiftExpansion(ArrayList<Component> shiftTooltip, java.util.Collection<? extends Component> shiftDescription) {
        return false;
    }
}