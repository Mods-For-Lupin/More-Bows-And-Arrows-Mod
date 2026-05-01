package io.github.jason13official.more_bows_and_arrows.api.common.component.bow;

import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStats;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

public class BowAssembler {
  public static BowStats assemble(ItemStack stack) {
    var builder = BowStats.builder();

    applyPart(stack, ModDataComponents.BOW_LIMB_DATA,   builder);
    applyPart(stack, ModDataComponents.BOW_STRING_DATA, builder);
//    applyPart(stack, BowComponents.RISER,  builder);
//    applyPart(stack, BowComponents.REST,   builder);

    return builder.build();
  }

  private static <T extends BowPart> void applyPart(
      ItemStack stack, DataComponentType<T> type, BowStats.Builder builder
  ) {
    T part = stack.get(type);
    if (part != null) part.applyTo(builder);
  }
}
