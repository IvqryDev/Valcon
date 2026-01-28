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

    public static final DeferredItem<Item> ANCIENT_FANG = ITEMS.register("ancient_fang",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ASHEN_STEEL_INGOT = ITEMS.register("ashen_steel_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ASHEN_STEEL_SHEET = ITEMS.register("ashen_steel_sheet",
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
            () -> new Item(new Item.Properties()
                    .rarity(Rarity.RARE)));

    public static final DeferredItem<Item> YETI_FUR = ITEMS.register("yeti_fur",
            () -> new Item(new Item.Properties()
                    .rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> NIGHTMARE_SCRAP = ITEMS.register("nightmare_scrap",
            () -> new Item(new Item.Properties()
                    .rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> UMBRAL_SHARD = ITEMS.register("umbral_shard",
            () -> new UmbralShardItem(new Item.Properties()
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

    public static final DeferredItem<Item> DEVIL_TRIGGER_MUSIC_DISC = ITEMS.registerItem("devil_trigger_music_disc",
            (properties) -> new Item(properties.jukeboxPlayable(ModSounds.DEVIL_TRIGGER_KEY)
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

    public static void register(IEventBus eventBus) {ITEMS.register(eventBus);
    }
}