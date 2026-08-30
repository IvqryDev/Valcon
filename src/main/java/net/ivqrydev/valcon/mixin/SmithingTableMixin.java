package net.ivqrydev.valcon.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SmithingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingTableBlock.class)
public class SmithingTableMixin {

    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void openAnvilInstead(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ContainerLevelAccess access = ContainerLevelAccess.create(level, pos);
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new AnvilMenu(id, inv, access) {
                        @Override
                        public boolean stillValid(Player player) {
                            return stillValid(access, player, net.minecraft.world.level.block.Blocks.SMITHING_TABLE);
                        }
                    },
                    Component.translatable("container.repair")
            ));
        }
        cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
    }
}