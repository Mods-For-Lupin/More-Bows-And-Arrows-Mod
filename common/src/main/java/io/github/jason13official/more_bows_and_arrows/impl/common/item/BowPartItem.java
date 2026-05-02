package io.github.jason13official.more_bows_and_arrows.impl.common.item;

import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowPartDefinition;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModRegistries;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

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
  public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, display, builder, tooltipFlag);

    HolderLookup.Provider registries = context.registries();
    if (registries == null) return;

    for (PartSlot slot : SLOTS) {
      ResourceKey<BowPartDefinition> key = stack.get(slot.component());
      if (key == null) continue;
      registries.lookup(slot.registry())
          .flatMap(reg -> reg.get(key))
          .ifPresent(h -> {
            builder.accept(h.value().description().copy().withStyle(ChatFormatting.GRAY));
            builder.accept(Component.literal("Durability Added: " + h.value().addedDurability()).withStyle(ChatFormatting.BLUE));
          });
      return;
    }
  }
}
