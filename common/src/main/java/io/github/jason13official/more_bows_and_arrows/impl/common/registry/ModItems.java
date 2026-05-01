package io.github.jason13official.more_bows_and_arrows.impl.common.registry;

import io.github.jason13official.more_bows_and_arrows.MoreBowsAndArrows;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.LimbData;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.StringData;
import io.github.jason13official.more_bows_and_arrows.impl.common.item.BowPartItem;
import io.github.jason13official.more_bows_and_arrows.impl.common.item.StrungBowItem;
import java.util.function.BiConsumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class ModItems {

  public static Item BOW_LIMB;
  public static Item BOW_STRING;
  public static Item STRUNG_BOW;

  public static void register(BiConsumer<Item, Identifier> consumer) {

    BOW_LIMB = new BowPartItem(new Properties().component(ModDataComponents.BOW_LIMB_DATA, LimbData.DEFAULT).setId(key(MoreBowsAndArrows.identifier("bow_limb"))));
    consumer.accept(BOW_LIMB, MoreBowsAndArrows.identifier("bow_limb"));

    BOW_STRING = new BowPartItem(new Properties().component(ModDataComponents.BOW_STRING_DATA, StringData.DEFAULT).setId(key(MoreBowsAndArrows.identifier("bow_string"))));
    consumer.accept(BOW_STRING, MoreBowsAndArrows.identifier("bow_string"));

    STRUNG_BOW = new StrungBowItem(new Properties().durability(50).component(ModDataComponents.BOW_LIMB_DATA, LimbData.DEFAULT).component(ModDataComponents.BOW_STRING_DATA, StringData.DEFAULT).setId(key(MoreBowsAndArrows.identifier("strung_bow"))));
    consumer.accept(STRUNG_BOW, MoreBowsAndArrows.identifier("strung_bow"));
  }

  private static ResourceKey<Item> key(Identifier path) {
    return ResourceKey.create(Registries.ITEM, path);
  }
}
