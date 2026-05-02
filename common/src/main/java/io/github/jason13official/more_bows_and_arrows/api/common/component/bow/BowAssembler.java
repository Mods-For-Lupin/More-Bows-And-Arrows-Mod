package io.github.jason13official.more_bows_and_arrows.api.common.component.bow;

import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowPartDefinition;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStats;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModRegistries;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;

public class BowAssembler {

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

  public static BowStats assemble(ItemStack stack, HolderLookup.Provider registries) {
    var stats = new BowStats();
    for (PartSlot slot : SLOTS) {
      ResourceKey<BowPartDefinition> key = stack.get(slot.component());
      if (key == null) continue;
      registries.lookup(slot.registry())
          .flatMap(reg -> reg.get(key))
          .ifPresent(holder -> holder.value().applyTo(stats));
    }
    return stats;
  }
}
