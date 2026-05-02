package io.github.jason13official.more_bows_and_arrows.api.common.component.bow;

import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStats;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;

public class BowAssembler {

  public static BowStats assemble(ItemStack stack, HolderLookup.Provider registries) {
    var builder = BowStats.builder();
    applyType(stack, ModDataComponents.BOW_LIMB_TYPE, ModRegistries.BOW_LIMB_TYPE_KEY, registries, builder);
    applyType(stack, ModDataComponents.BOW_STRING_TYPE, ModRegistries.BOW_STRING_TYPE_KEY, registries, builder);
    return builder.build();
  }

  private static <T extends BowPart> void applyType(
      ItemStack stack,
      DataComponentType<ResourceKey<T>> component,
      ResourceKey<Registry<T>> registryKey,
      HolderLookup.Provider registries,
      BowStats.Builder builder
  ) {
    ResourceKey<T> key = stack.get(component);
    if (key == null) return;
    registries.lookup(registryKey)
        .flatMap(reg -> reg.get(key))
        .ifPresent(holder -> holder.value().applyTo(builder));
  }
}
