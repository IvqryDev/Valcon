package net.ivqrydev.valcon.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class InsufficientToolHandler {

    private InsufficientToolHandler() {}

    //Tracks the last game time each player triggered a blocked mine attempt.
    private static final Map<UUID, Long> lastAttemptTime = new HashMap<>();

    public static boolean requiresStrongerTool(Player player, BlockState state, ItemStack held) {
        //Block must require a specific tool.
        if (!state.requiresCorrectToolForDrops()) return false;

        //Bare hands or non-tiered items are ignored.
        if (held.isEmpty() || !(held.getItem() instanceof TieredItem)) return false;

        //If the tool is the correct tier, there's nothing to block. (See what I did there?)
        if (held.isCorrectToolForDrops(state)) return false;

        return held.getDestroySpeed(state) > 1.0F;
    }

    public static boolean canAttemptMine(Player player) {
        return player.getAttackStrengthScale(0F) >= 1.0F;
    }

    public static void recordAttempt(Player player) {
        lastAttemptTime.put(player.getUUID(), player.level().getGameTime());
        player.resetAttackStrengthTicker();
    }
}