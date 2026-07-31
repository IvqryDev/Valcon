package net.ivqrydev.valcon.mixin;

import com.rosemods.windswept.common.entity.Frostbiter;
import com.rosemods.windswept.core.registry.WindsweptItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Frostbiter.class)
public abstract class FrostbiterFoodMixin {

    @Overwrite
    public boolean isFood(ItemStack stack) {
        return stack.is(WindsweptItems.GINGER_ROOT.get());
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void onMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(WindsweptItems.GINGER_ROOT.get())) return;

        Frostbiter self = (Frostbiter) (Object) this;

        if (self.level().isClientSide) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        if (!self.isTame()) {
            if (self.getRandom().nextInt(3) == 0) {
                self.tame(player);
                self.level().broadcastEntityEvent(self, (byte) 7);
            } else {
                self.level().broadcastEntityEvent(self, (byte) 6);
            }
        } else if (self.getHealth() < self.getMaxHealth()) {
            self.heal(7f);
        } else if (self.canFallInLove()) {
            self.setInLove(player);
        }

        stack.shrink(1);
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void replaceTemptGoal(CallbackInfo ci) {
        Frostbiter self = (Frostbiter) (Object) this;
        self.goalSelector.getAvailableGoals().removeIf(w -> w.getGoal() instanceof TemptGoal);
        self.goalSelector.addGoal(3, new TemptGoal(self, 1.15f, Ingredient.of(WindsweptItems.GINGER_ROOT.get()), false));
    }
}