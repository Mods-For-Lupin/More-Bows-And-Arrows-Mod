package io.github.jason13official.more_bows_and_arrows.impl.common.registry;

import io.github.jason13official.more_bows_and_arrows.MoreBowsAndArrows;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowLimbType;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStringType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModRegistries {

  public static final ResourceKey<Registry<BowLimbType>> BOW_LIMB_TYPE_KEY =
      ResourceKey.createRegistryKey(MoreBowsAndArrows.identifier("bow_limb_type"));

  public static final ResourceKey<Registry<BowStringType>> BOW_STRING_TYPE_KEY =
      ResourceKey.createRegistryKey(MoreBowsAndArrows.identifier("bow_string_type"));

  public static final ResourceKey<BowLimbType> DEFAULT_LIMB =
      ResourceKey.create(BOW_LIMB_TYPE_KEY, MoreBowsAndArrows.identifier("default"));

  public static final ResourceKey<BowStringType> DEFAULT_STRING =
      ResourceKey.create(BOW_STRING_TYPE_KEY, MoreBowsAndArrows.identifier("default"));
}
