package net.ivqrydev.valcon.worldgen.feature;

import net.ivqrydev.valcon.Valcon;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, Valcon.MOD_ID);

    public static final DeferredHolder<Feature<?>, IronrootTreeFeature> IRONROOT_TREE =
            FEATURES.register("ironroot_tree",
                    () -> new IronrootTreeFeature(NoneFeatureConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}