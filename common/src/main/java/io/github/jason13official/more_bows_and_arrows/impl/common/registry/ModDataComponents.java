package io.github.jason13official.more_bows_and_arrows.impl.common.registry;

import io.github.jason13official.more_bows_and_arrows.MoreBowsAndArrows;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowLimbType;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStringType;
import java.util.function.BiConsumer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class ModDataComponents {

  public static DataComponentType<ResourceKey<BowLimbType>> BOW_LIMB_TYPE;
  public static DataComponentType<ResourceKey<BowStringType>> BOW_STRING_TYPE;

  public static void register(BiConsumer<DataComponentType<?>, Identifier> consumer) {

    BOW_LIMB_TYPE = DataComponentType.<ResourceKey<BowLimbType>>builder()
        .persistent(ResourceKey.codec(ModRegistries.BOW_LIMB_TYPE_KEY)).build();
    BOW_STRING_TYPE = DataComponentType.<ResourceKey<BowStringType>>builder()
        .persistent(ResourceKey.codec(ModRegistries.BOW_STRING_TYPE_KEY)).build();

    consumer.accept(BOW_LIMB_TYPE, MoreBowsAndArrows.identifier("bow_limb_type"));
    consumer.accept(BOW_STRING_TYPE, MoreBowsAndArrows.identifier("bow_string_type"));
  }
}
