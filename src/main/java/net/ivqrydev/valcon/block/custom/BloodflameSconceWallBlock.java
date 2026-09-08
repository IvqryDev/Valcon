package net.ivqrydev.valcon.block.custom;

import com.farcr.nomansland.common.block.torches.SconceWallTorchBlock;
import net.jadenxgamer.netherexp.registry.JNEParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BloodflameSconceWallBlock extends SconceWallTorchBlock {
    public BloodflameSconceWallBlock(Properties properties) {
        super(ParticleTypes.FLAME, properties); //Placeholder
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction direction = state.getValue(FACING).getOpposite();
        double dx = pos.getX() + 0.5;
        double dy = pos.getY() + 0.7;
        double dz = pos.getZ() + 0.5;
        level.addParticle(ParticleTypes.SMOKE, dx + 0.2 * direction.getStepX(), dy + 0.22, dz + 0.2 * direction.getStepZ(), 0, 0, 0);
        level.addParticle(JNEParticleTypes.TREACHEROUS_FLAME.get(), dx + 0.2 * direction.getStepX(), dy + 0.22, dz + 0.2 * direction.getStepZ(), 0, 0, 0);
    }
}