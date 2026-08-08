package net.ivqrydev.valcon.block.custom;

import com.baisylia.modestmagic.recipe.custom.TabletSmithingRecipe;
import com.mojang.serialization.MapCodec;
import net.ivqrydev.valcon.block.entity.SoulForgeBlockEntity;
import net.ivqrydev.valcon.item.ModItems;
import net.ivqrydev.valcon.sound.ModSounds;
import net.ivqrydev.valcon.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SoulForgeBlock extends BaseEntityBlock {

    public static final MapCodec<SoulForgeBlock> CODEC = simpleCodec(SoulForgeBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private enum TabletResult { SUCCESS, INCOMPATIBLE, ALREADY_MAXED }

    public SoulForgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    // Block state.
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        if (!state.hasProperty(FACING)) return state;
        return rotate(state, mirror.getRotation(state.getValue(FACING)));
    }

    // Block entity.
    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SoulForgeBlockEntity(pos, state);
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof SoulForgeBlockEntity forge) {
                forge.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    //Interactions.
    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level,
                                                       @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
                                                       @NotNull BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof SoulForgeBlockEntity forge))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        ItemStack stored = forge.inventory.getStackInSlot(0);

        //If there is an empty hand, extract armament.
        if (stack.isEmpty()) {
            if (!stored.isEmpty()) {
                player.setItemInHand(hand, stored.copy());
                forge.clearContents();
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            }
            return ItemInteractionResult.SUCCESS;
        }

        //If the forge is already empty, only accept items in the great_rune_applicable tag.
        //Durability is only checked when soul steel is applied, great rune targets may lack it.
        if (stored.isEmpty()) {
            if (!stack.is(ModTags.Items.GREAT_RUNE_APPLICABLE)) {
                sendMessage(level, player, "message.valcon.soul_forge.unworthy");
                return ItemInteractionResult.SUCCESS;
            }
            forge.inventory.insertItem(0, stack.copy(), false);
            stack.shrink(1);
            level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
            return ItemInteractionResult.SUCCESS;
        }

        //If the player applies a soul steel ingot, insert the unbreakable component onto the armament.
        //Only damageable or already-unbreakable items are valid targets, others are unworthy.
        if (stack.getItem() == ModItems.SOUL_STEEL_INGOT.get()) {
            if (!stored.isDamageableItem() && !stored.has(DataComponents.UNBREAKABLE)) {
                sendMessage(level, player, "message.valcon.soul_forge.unworthy");
                return ItemInteractionResult.SUCCESS;
            }
            if (stored.has(DataComponents.UNBREAKABLE)) {
                sendMessage(level, player, "message.valcon.soul_forge.already_blessed");
                return ItemInteractionResult.SUCCESS;
            }
            stored.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
            forge.inventory.setStackInSlot(0, stored);
            stack.shrink(1);
            sendMessage(level, player, "message.valcon.soul_forge.forge_success");
            level.playSound(player, pos, ModSounds.SOUL_FORGE_USE.get(), SoundSource.BLOCKS, 1f, 1f);
            for (int i = 0; i < 20; i++) {
                double vx = level.random.nextGaussian() * 0.1;
                double vy = level.random.nextDouble() * 0.1 + 0.02;
                double vz = level.random.nextGaussian() * 0.1;
                level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, vx, vy, vz);
            }
            return ItemInteractionResult.SUCCESS;
        }

        //Great runes/tablets lookup recipes and run server side only, particles run both on both server and client.
        if (!level.isClientSide()) {
            TabletSmithingRecipe recipe = findTabletRecipe(level, stack, stored);
            if (recipe == null) return ItemInteractionResult.SUCCESS;

            TabletResult result = tryApplyTablet(recipe, stored, level);
            switch (result) {
                case INCOMPATIBLE  -> sendMessage(level, player, "message.valcon.soul_forge.incompatible_tablet");
                case ALREADY_MAXED -> sendMessage(level, player, "message.valcon.soul_forge.already_infused");
                case SUCCESS -> {
                    forge.inventory.setStackInSlot(0, applyTabletEnchantments(recipe, stored, level));
                    stack.shrink(1);
                    sendMessage(level, player, "message.valcon.soul_forge.tablet_success");
                }
            }
        } else {
            //Play effects if the recipe exists, while the server handles the actual state change.
            TabletSmithingRecipe recipe = findTabletRecipe(level, stack, stored);
            if (recipe != null && tryApplyTablet(recipe, stored, level) == TabletResult.SUCCESS) {
                level.playSound(player, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1f, 1f);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    //Lookup associated great rune/tablet recipes with held tablet.
    @Nullable
    private TabletSmithingRecipe findTabletRecipe(Level level, ItemStack tablet, ItemStack equipment) {
        for (RecipeHolder<?> holder : level.getRecipeManager().getRecipes()) {
            if (!(holder.value() instanceof TabletSmithingRecipe recipe)) continue;
            if (!recipe.isTemplateIngredient(tablet)) continue;
            if (!recipe.isBaseIngredient(equipment)) continue;
            return recipe;
        }
        return null;
    }

    //Check whether a great rune/tablet can be applied and returns the outcome without mutating anything.
    private TabletResult tryApplyTablet(TabletSmithingRecipe recipe, ItemStack equipment, Level level) {
        var registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        ItemEnchantments existing = equipment.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(existing);

        boolean anyValidForItem = false;
        boolean anyApplied = false;

        for (ResourceKey<Enchantment> key : recipe.enchantments()) {
            Optional<Holder.Reference<Enchantment>> opt = registry.get(key);
            if (opt.isEmpty()) continue;

            Holder<Enchantment> holder = opt.get();
            if (!holder.value().canEnchant(equipment)) continue;
            anyValidForItem = true;

            if (hasEnchantConflict(existing, holder)) continue;
            int next = mutable.getLevel(holder) + 1;
            if (next > holder.value().getMaxLevel()) continue;

            mutable.set(holder, next);
            anyApplied = true;
        }

        if (!anyValidForItem) return TabletResult.INCOMPATIBLE;
        if (!anyApplied)     return TabletResult.ALREADY_MAXED;
        return TabletResult.SUCCESS;
    }

    //Applies the great rune/tablet's enchantments and returns the modified item this only calls after tryApplyTablet returns a success.
    private ItemStack applyTabletEnchantments(TabletSmithingRecipe recipe, ItemStack equipment, Level level) {
        var registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        ItemStack result = equipment.copy();
        ItemEnchantments existing = result.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(existing);

        for (ResourceKey<Enchantment> key : recipe.enchantments()) {
            Optional<Holder.Reference<Enchantment>> opt = registry.get(key);
            if (opt.isEmpty()) continue;

            Holder<Enchantment> holder = opt.get();
            if (!holder.value().canEnchant(result)) continue;
            if (hasEnchantConflict(existing, holder)) continue;

            int next = mutable.getLevel(holder) + 1;
            if (next > holder.value().getMaxLevel()) continue;
            mutable.set(holder, next);
        }

        EnchantmentHelper.setEnchantments(result, mutable.toImmutable());
        return result;
    }

    //Return true if the given enchantment conflicts with any enchantment already on the armament.
    private boolean hasEnchantConflict(ItemEnchantments existing, Holder<Enchantment> enchant) {
        for (Holder<Enchantment> e : existing.keySet()) {
            if (!e.equals(enchant) && !Enchantment.areCompatible(e, enchant)) return true;
        }
        return false;
    }

    //Sends an action bar message server-side only.
    private void sendMessage(Level level, Player player, String key) {
        if (!level.isClientSide()) player.displayClientMessage(Component.translatable(key), true);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context,
                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.valcon.soul_forge.tooltip.description").withStyle(ChatFormatting.GRAY));
    }
}