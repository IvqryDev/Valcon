package net.ivqrydev.valcon.event;

import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

import static net.ivqrydev.valcon.Valcon.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public final class InsufficientToolClientHandler {

    private InsufficientToolClientHandler() {}

    @SubscribeEvent
    public static void onInteractionKeyMapping(InputEvent.InteractionKeyMappingTriggered event) {
        //Only care about left-click on a block.
        if (!event.isAttack()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.BLOCK) return;

        Player player = mc.player;
        Level level = mc.level;
        BlockPos pos = ((BlockHitResult) mc.hitResult).getBlockPos();
        Direction face = ((BlockHitResult) mc.hitResult).getDirection();
        BlockState state = level.getBlockState(pos);
        ItemStack held = player.getMainHandItem();

        if (!InsufficientToolHandler.requiresStrongerTool(player, state, held)) return;

        //Suppress all attempts until thecooldown resets.
        if (!InsufficientToolHandler.canAttemptMine(player)) {
            event.setSwingHand(false);
            event.setCanceled(true);
            return;
        }

        InsufficientToolHandler.recordAttempt(player);

        //Swing the arm to sell the hit, then spawn sparks at the clicked face.
        player.swing(event.getHand());
        spawnSparks(level, pos, face, player);

        //Play the block's own breaking sound three times to sell the impact.
        SoundType sounds = state.getSoundType();
        for (int i = 0; i < 3; i++) {
            level.playSound(player, pos, sounds.getBreakSound(), player.getSoundSource(),
                    sounds.getVolume() * 0.8F, sounds.getPitch() * (0.9F + player.getRandom().nextFloat() * 0.2F));
        }

        //Display warning message.
        mc.gui.setOverlayMessage(
                Component.translatable("message.valcon.pathetic"),
                false
        );

        event.setSwingHand(false);
        event.setCanceled(true);
    }

    private static void spawnSparks(Level level, BlockPos pos, Direction face, Player player) {
        //Spawn tin spark particles at the clicked block face, all thanks to C&C!
        Vec3 hitPos = Vec3.atCenterOf(pos).add(Vec3.atLowerCornerOf(face.getNormal()).scale(0.5));
        for (int i = 0; i < 12; i++) {
            double vx = player.getRandom().nextGaussian() * 0.12;
            double vy = 0.08 + player.getRandom().nextDouble() * 0.2;
            double vz = player.getRandom().nextGaussian() * 0.12;
            level.addParticle(
                    CCParticleTypes.TIN_SPARK.get(),
                    hitPos.x + player.getRandom().nextGaussian() * 0.1,
                    hitPos.y + player.getRandom().nextGaussian() * 0.1,
                    hitPos.z + player.getRandom().nextGaussian() * 0.1,
                    vx, vy, vz
            );
        }
    }
}