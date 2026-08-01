package net.ivqrydev.valcon.mixin;

import com.rosemods.windswept.common.item.FeatherCloakItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FeatherCloakItem.class)
public abstract class FeatherCloakArmorMixin implements IItemExtension {

    //Blocks armor slot equipping.
    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
        return false;
    }
}