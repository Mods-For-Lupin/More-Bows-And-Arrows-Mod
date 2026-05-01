package io.github.jason13official.more_bows_and_arrows.impl.common.item;

import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.LimbData;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.StringData;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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

    if (stack.has(ModDataComponents.BOW_LIMB_DATA)) {

      LimbData data = stack.get(ModDataComponents.BOW_LIMB_DATA);
      assert data != null;

      builder.accept(Component.literal("Durability Added: " + data.addedDurability()));
    }

    if (stack.has(ModDataComponents.BOW_STRING_DATA)) {

      StringData data = stack.get(ModDataComponents.BOW_STRING_DATA);
      assert data != null;

      builder.accept(Component.literal("Durability Added: " + data.addedDurability()));
    }
  }
}
