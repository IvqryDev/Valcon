package net.ivqrydev.valcon.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BastStatueBlockEntity extends BlockEntity {

    private final Map<UUID, Integer> cooldownMap = new HashMap<>();

    public BastStatueBlockEntity(BlockPos pos, BlockState state) {
        super(net.ivqrydev.valcon.block.entity.ModBlockEntities.BAST_STATUE_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BastStatueBlockEntity blockEntity) {
        if (level.isClientSide) return;

        blockEntity.cooldownMap.replaceAll((uuid, ticks) -> Math.max(0, ticks - 1));

        AABB range = new AABB(pos).inflate(24);
        List<Player> players = level.getEntitiesOfClass(Player.class, range);

        for (Player player : players) {
            UUID id = player.getUUID();
            int cooldown = blockEntity.cooldownMap.getOrDefault(id, 0);

            if (cooldown == 0) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 0, false, false, true)); // 15s duration
                blockEntity.cooldownMap.put(id, 60); // 3s cooldown
            }
        }
    }
}
