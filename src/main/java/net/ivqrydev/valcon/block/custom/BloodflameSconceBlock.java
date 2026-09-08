package net.ivqrydev.valcon.block.custom;

import com.farcr.nomansland.common.block.torches.SconceTorchBlock;
import net.jadenxgamer.netherexp.registry.JNEParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BloodflameSconceBlock extends SconceTorchBlock {
    public BloodflameSconceBlock(Properties properties) {
        super(ParticleTypes.FLAME, properties); //Placeholder
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double d0 = pos.getX() + 0.5D;
        double d1 = pos.getY() + 0.8D;
        double d2 = pos.getZ() + 0.5D;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0, 0, 0);
        level.addParticle(JNEParticleTypes.TREACHEROUS_FLAME.get(), d0, d1, d2, 0, 0, 0);
    }
}