package io.github.jason13official.more_bows_and_arrows.impl.common.item;

import java.util.function.Consumer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
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

    builder.accept(Component.literal("Max Durability " + stack.get(DataComponents.MAX_DAMAGE)));
  }
}
