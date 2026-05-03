package io.github.jason13official.more_bows_and_arrows.impl.common.item;

import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowPartDefinition;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStatEffect;
import io.github.jason13official.more_bows_and_arrows.impl.common.inventory.BowyerMenu;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModRegistries;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

public class BowPartItem extends Item {

  private record PartSlot(
      DataComponentType<ResourceKey<BowPartDefinition>> component,
      ResourceKey<Registry<BowPartDefinition>> registry
  ) {}

  private static final List<PartSlot> SLOTS = List.of(
      new PartSlot(ModDataComponents.BOW_LIMB_TYPE,   ModRegistries.BOW_LIMB_TYPE_KEY),
      new PartSlot(ModDataComponents.BOW_STRING_TYPE,  ModRegistries.BOW_STRING_TYPE_KEY),
      new PartSlot(ModDataComponents.BOW_RISER_TYPE,  ModRegistries.BOW_RISER_TYPE_KEY),
      new PartSlot(ModDataComponents.BOW_REST_TYPE,   ModRegistries.BOW_REST_TYPE_KEY)
  );

  public BowPartItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {

    if (context.getPlayer() == null) {
      return super.useOn(context);
    }

    if (!context.getLevel().isClientSide()
        && context.getLevel().getBlockState(context.getClickedPos()).is(Blocks.FLETCHING_TABLE)) {
      ContainerLevelAccess access = ContainerLevelAccess.create(context.getLevel(), context.getClickedPos());
      context.getPlayer().openMenu(new SimpleMenuProvider(
          (i, inventory, player) -> new BowyerMenu(i, inventory, access),
          Component.literal("Bowyer")
      ));
    }

    return super.useOn(context);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, display, builder, tooltipFlag);

    HolderLookup.Provider registries = context.registries();
    if (registries == null) return;

    for (PartSlot slot : SLOTS) {
      ResourceKey<BowPartDefinition> key = stack.get(slot.component());
      if (key == null) continue;
      registries.lookup(slot.registry())
          .flatMap(reg -> reg.get(key))
          .ifPresent(h -> appendPartTooltip(h.value(), builder));
      return;
    }
  }

  private static void appendPartTooltip(BowPartDefinition def, Consumer<Component> builder) {
    builder.accept(def.description().copy().withStyle(ChatFormatting.GRAY));
    for (BowStatEffect effect : def.effects()) {
      builder.accept(statLine(effect.stat(), formatEffect(effect)));
    }
    builder.accept(Component.literal("Durability Added: " + def.addedDurability()).withStyle(ChatFormatting.BLUE));
  }

  static Component statLine(Identifier stat, String value) {
    return Component.translatable("bow_stat." + stat.getNamespace() + "." + stat.getPath())
        .append(Component.literal(": " + value))
        .withStyle(ChatFormatting.DARK_GREEN);
  }

  static String formatEffect(BowStatEffect effect) {
    return switch (effect.operation()) {
      case ADD      -> String.format("%+.2f", effect.value());
      case MULTIPLY -> String.format("x%.2f", effect.value());
      case SET      -> String.format("%.2f", effect.value());
    };
  }
}
