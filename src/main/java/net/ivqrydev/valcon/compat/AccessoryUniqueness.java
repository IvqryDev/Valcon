package net.ivqrydev.valcon.compat;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.events.CanEquipCallback;
import io.wispforest.accessories.api.slot.SlotReference;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.item.ItemStack;

public final class AccessoryUniqueness {

    private AccessoryUniqueness() {}

    public static void init() {
        CanEquipCallback.EVENT.register(AccessoryUniqueness::onCanEquip);
    }

    //Blocks player from equipping a talisman of the same type in more than one slot.
    private static TriState onCanEquip(ItemStack stack, SlotReference reference) {
        if (stack.isEmpty()) return TriState.DEFAULT;

        var capability = AccessoriesCapability.get(reference.entity());
        if (capability == null) return TriState.DEFAULT;

        var item = stack.getItem();

        for (var equipped : capability.getAllEquipped()) {
            if (equipped.reference().equals(reference)) continue;
            if (equipped.stack().getItem() != item) continue;

            return TriState.FALSE;
        }

        return TriState.DEFAULT;
    }
}