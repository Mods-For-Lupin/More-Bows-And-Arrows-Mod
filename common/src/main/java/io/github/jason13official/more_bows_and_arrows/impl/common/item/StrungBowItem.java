package io.github.jason13official.more_bows_and_arrows.impl.common.item;

import io.github.jason13official.more_bows_and_arrows.api.common.component.bow.BowAssembler;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStat;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStats;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class StrungBowItem extends Item {

  public StrungBowItem(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, display, builder, tooltipFlag);

    HolderLookup.Provider registries = context.registries();
    if (registries != null) {
      BowStats stats = BowAssembler.assemble(stack, registries);
      for (Identifier stat : BowStat.ORDERED) {
        builder.accept(BowPartItem.statLine(stat, String.format("%.2f", stats.get(stat))));
      }
      if (stats.getAddedDurability() != 0) {
        builder.accept(Component.literal("Durability Added: " + stats.getAddedDurability()).withStyle(ChatFormatting.BLUE));
      }
    }

    builder.accept(Component.literal("Max Durability: " + stack.get(DataComponents.MAX_DAMAGE)).withStyle(ChatFormatting.GRAY));
  }
}
