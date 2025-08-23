package net.ivqrydev.valcon.item;

import net.ivqrydev.valcon.Valcon;
import net.ivqrydev.valcon.item.custom.*;
import net.ivqrydev.valcon.sound.ModSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Valcon.MOD_ID);


    public static final DeferredItem<Item> SOUL_QUARTZ = ITEMS.register("soul_quartz",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SOUL_STEEL_INGOT = ITEMS.register("soul_steel_ingot",
            () -> new Item(new Item.Properties()
                    .rarity(Rarity.RARE)));

    public static final DeferredItem<Item> SOUL = ITEMS.register("soul",
            () -> new SoulItem(new Item.Properties()
                    .rarity(Rarity.RARE)));

    public static final DeferredItem<Item> ENCHANTING_GUIDE = ITEMS.register("enchanting_guide",
            () -> new EnchantingGuideItem(new Item.Properties()
                    .rarity(Rarity.RARE)));

    public static final DeferredItem<Item> COMPASS_CASTING_MOLD = ITEMS.register("compass_casting_mold",
            () -> new CompassCastingMoldItem(new Item.Properties()
                    .rarity(Rarity.RARE)));

    public static final DeferredItem<Item> MYTHRIL_CORE = ITEMS.register("mythril_core",
            () -> new MythrilCoreItem(new Item.Properties()
                    .rarity(Rarity.RARE)));

    public static final DeferredItem<Item> LEMBAS = ITEMS.register("lembas",
            () -> new Item(new Item.Properties()
                    .food(ModFoodProperties.LEMBAS)
                    .stacksTo(16)
            ));

    public static final DeferredItem<Item> STAR_TAKER_MUSIC_DISC = ITEMS.registerItem("star_taker_music_disc",
            (properties) -> new Item(properties.jukeboxPlayable(ModSounds.STAR_TAKER_KEY)
                    .stacksTo(1)
                    .rarity(Rarity.RARE)
            ));

    public static final DeferredItem<SwordItem> SOULBORN_BLADE = ITEMS.register("soulborn_blade",
            () -> new SwordItem(ModToolTiers.SOUL_STEEL, new Item.Properties()
                    .rarity(Rarity.RARE)
                    .fireResistant()
                    .durability(1024)
                    .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
                    .attributes(SwordItem.createAttributes(ModToolTiers.SOUL_STEEL, 8, -3f))
            ));

    public static final DeferredItem<SwordItem> SARISSA = ITEMS.register("sarissa",
            () -> new SwordItem(ModToolTiers.ANCIENT, new Item.Properties()
                    .rarity(Rarity.RARE)
                    .fireResistant()
                    .durability(6144)
                    .attributes(SwordItem.createAttributes(ModToolTiers.ANCIENT, 8, -2.6f))
            ));

    public static void register(IEventBus eventBus) {ITEMS.register(eventBus);
    }
}