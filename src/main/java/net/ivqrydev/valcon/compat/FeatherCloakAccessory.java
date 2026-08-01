package net.ivqrydev.valcon.compat;

import com.rosemods.windswept.core.registry.WindsweptItems;
import io.wispforest.accessories.api.Accessory;
import io.wispforest.accessories.api.AccessoriesAPI;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class FeatherCloakAccessory implements Accessory {

    public static void register() {
        AccessoriesAPI.registerAccessory(WindsweptItems.FEATHER_CLOAK.get(), new FeatherCloakAccessory());
    }

    //+12% movement speed when equipped.
    @Override
    public void getDynamicModifiers(ItemStack stack, SlotReference reference, AccessoryAttributeBuilder builder) {
        builder.addExclusive(
                Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath("valcon", "feather_cloak_speed"),
                0.12,
                Operation.ADD_MULTIPLIED_BASE
        );
    }
}