package io.github.jason13official.more_bows_and_arrows.impl.common.item;

import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowLimbType;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStringType;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModRegistries;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class BowPartItem extends Item {

  private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
  private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;

  public BowPartItem(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, display, builder, tooltipFlag);

    HolderLookup.Provider registries = context.registries();
    if (registries == null) return;

    if (stack.has(ModDataComponents.BOW_LIMB_TYPE)) {
      ResourceKey<BowLimbType> key = stack.get(ModDataComponents.BOW_LIMB_TYPE);
      registries.lookup(ModRegistries.BOW_LIMB_TYPE_KEY)
          .flatMap(reg -> reg.get(key))
          .ifPresent(h -> builder.accept(Component.literal("Durability Added: " + h.value().addedDurability())));
    }

    if (stack.has(ModDataComponents.BOW_STRING_TYPE)) {
      ResourceKey<BowStringType> key = stack.get(ModDataComponents.BOW_STRING_TYPE);
      registries.lookup(ModRegistries.BOW_STRING_TYPE_KEY)
          .flatMap(reg -> reg.get(key))
          .ifPresent(h -> builder.accept(Component.literal("Durability Added: " + h.value().addedDurability())));
    }
  }
}
