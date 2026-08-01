package net.ivqrydev.valcon.mixin;

import com.rosemods.windswept.common.item.FeatherCloakItem;
import com.rosemods.windswept.core.other.WindsweptDataProcessors;
import com.rosemods.windswept.core.other.events.WindsweptEntityEvents;
import com.rosemods.windswept.core.registry.WindsweptItems;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import io.wispforest.accessories.api.AccessoriesCapability;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WindsweptEntityEvents.class)
public class FeatherCloakAccessoryMixin {

    //Hijack tick logic and direct into the necklace slot.
    @Inject(method = "onEntityUpdate", at = @At("HEAD"), cancellable = true, remap = false)
    private static void valcon$overrideFeatherCloakTick(EntityTickEvent.Post event, CallbackInfo ci) {
        if (!(event.getEntity() instanceof LivingEntity entity) || entity.level().isClientSide)
            return;

        boolean inChest = entity.getItemBySlot(EquipmentSlot.CHEST).is(WindsweptItems.FEATHER_CLOAK.get());
        var cap = AccessoriesCapability.get(entity);
        boolean inNecklace = cap != null && cap.isEquipped(WindsweptItems.FEATHER_CLOAK.get());

        if (!inChest && !inNecklace)
            return;

        ci.cancel();

        IDataManager data = (IDataManager) entity;
        //Has to have cloak equipped, and crouched.
        boolean cloaked = entity.isCrouching() && inNecklace;

        if (cloaked != data.getValue(WindsweptDataProcessors.CLOAKED)) {
            data.setValue(WindsweptDataProcessors.CLOAKED, cloaked);
            FeatherCloakItem.spawnFeatherCloakParticle(entity);
        }
    }
}