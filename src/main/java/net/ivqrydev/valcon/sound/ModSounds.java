package net.ivqrydev.valcon.sound;

import net.ivqrydev.valcon.Valcon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Valcon.MOD_ID);

    public static final Supplier<SoundEvent> SOUL_FORGE_USE = registerSoundEvent("soul_forge_use");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Valcon.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static final Supplier<SoundEvent> STAR_TAKER = registerSoundEvent("star_taker");
    public static final ResourceKey<JukeboxSong> STAR_TAKER_KEY = createSong("star_taker");

    public static final Supplier<SoundEvent> DEVIL_TRIGGER = registerSoundEvent("devil_trigger");
    public static final ResourceKey<JukeboxSong> DEVIL_TRIGGER_KEY = createSong("devil_trigger");

    private static ResourceKey<JukeboxSong> createSong(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(Valcon.MOD_ID, name));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}