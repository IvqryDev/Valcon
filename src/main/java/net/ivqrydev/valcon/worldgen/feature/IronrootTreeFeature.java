package net.ivqrydev.valcon.worldgen.feature;

import com.mojang.serialization.Codec;
import net.ivqrydev.valcon.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
public class IronrootTreeFeature extends Feature<NoneFeatureConfiguration> {

    public IronrootTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        int height = 3 + random.nextInt(4);
        BlockState logY = ModBlocks.IRONWOOD_LOG.get().defaultBlockState()
                .setValue(RotatedPillarBlock.AXIS, net.minecraft.core.Direction.Axis.Y);

        //Check if there is enough room.
        if (!level.getBlockState(origin).isAir() && !level.getBlockState(origin).canBeReplaced()) {
            return false;
        }

        //Place trunk.
        for (int i = 0; i < height; i++) {
            BlockPos pos = origin.above(i);
            if (level.getBlockState(pos).isAir() || level.getBlockState(pos).canBeReplaced()) {
                level.setBlock(pos, logY, 2);
            }
        }

        //Place 0-2 branches.
        int branchCount = random.nextInt(2);
        Direction[] horizontals = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

        //Place branch on 1-5.
        for (int b = 0; b < branchCount; b++) {
            int branchY = 1 + random.nextInt(Math.max(1, height - 1));
            BlockPos branchOrigin = origin.above(branchY);
            Direction dir = horizontals[random.nextInt(4)];
            BlockState logH = ModBlocks.IRONWOOD_LOG.get().defaultBlockState()
                    .setValue(RotatedPillarBlock.AXIS, dir.getAxis());

            //Random branch length.
            int branchLength = random.nextFloat() < 0.3f ? 2 : 1;
            for (int l = 1; l <= branchLength; l++) {
                BlockPos branchPos = branchOrigin.relative(dir, l);
                if (level.getBlockState(branchPos).isAir() || level.getBlockState(branchPos).canBeReplaced()) {
                    level.setBlock(branchPos, logH, 2);
                }
            }
        }
        return true;
    }
}