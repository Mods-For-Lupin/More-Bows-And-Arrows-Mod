package io.github.jason13official.more_bows_and_arrows.impl.common.registry;

import io.github.jason13official.more_bows_and_arrows.MoreBowsAndArrows;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.LimbData;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.StringData;
import java.util.function.BiConsumer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;

public class ModDataComponents {

  public static DataComponentType<LimbData> BOW_LIMB_DATA;
  public static DataComponentType<StringData> BOW_STRING_DATA;

  public static void register(BiConsumer<DataComponentType<?>, Identifier> consumer) {

    BOW_LIMB_DATA = DataComponentType.<LimbData>builder().persistent(LimbData.CODEC).build();
    BOW_STRING_DATA = DataComponentType.<StringData>builder().persistent(StringData.CODEC).build();

    consumer.accept(BOW_LIMB_DATA, MoreBowsAndArrows.identifier("bow_limb_data"));
    consumer.accept(BOW_STRING_DATA, MoreBowsAndArrows.identifier("bow_string_data"));
  }
}
