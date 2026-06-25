package net.ivqrydev.valcon.event;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;

import java.util.Map;

public class BonfireEventHandler {

    //Assigns each campfire it's designated dimension.
    private static final Map<ResourceKey<Level>, Block> SPAWN_CAMPFIRES = Map.of(
            Level.OVERWORLD, Blocks.CAMPFIRE,
            Level.NETHER,    Blocks.SOUL_CAMPFIRE
    );

    //Prevents beds from setting your spawn.
    @SubscribeEvent
    public static void onPlayerSetSpawn(PlayerSetSpawnEvent event) {
        if (event.getNewSpawn() != null) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        Player player = event.getEntity();
        Level level = event.getLevel();

        if (!player.isShiftKeyDown() || level.isClientSide()) return;

        ResourceKey<Level> dimension = level.dimension();
        Block requiredCampfire = SPAWN_CAMPFIRES.get(dimension);

        //Ignore unknown dimensions.
        if (requiredCampfire == null) return;

        BlockState state = level.getBlockState(event.getPos());

        //Check the supported dimension.
        if (!state.is(requiredCampfire) || !state.getValue(CampfireBlock.LIT)) return;

        ServerPlayer serverPlayer = (ServerPlayer) player;
        BlockPos spawnPos = serverPlayer.blockPosition();

        //Set spawn like vanilla.
        serverPlayer.setRespawnPosition(dimension, spawnPos, 0f, false, true);

        //Flavor text message.
        player.displayClientMessage(
                Component.translatable("message.valcon.spawn_set"),
                true
        );
    }
}