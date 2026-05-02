package io.github.jason13official.more_bows_and_arrows.impl.common.registry;

import io.github.jason13official.more_bows_and_arrows.MoreBowsAndArrows;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowPartDefinition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModRegistries {

  public static final ResourceKey<Registry<BowPartDefinition>> BOW_LIMB_TYPE_KEY =
      ResourceKey.createRegistryKey(MoreBowsAndArrows.identifier("bow_limb_type"));

  public static final ResourceKey<Registry<BowPartDefinition>> BOW_STRING_TYPE_KEY =
      ResourceKey.createRegistryKey(MoreBowsAndArrows.identifier("bow_string_type"));

  public static final ResourceKey<Registry<BowPartDefinition>> BOW_RISER_TYPE_KEY =
      ResourceKey.createRegistryKey(MoreBowsAndArrows.identifier("bow_riser_type"));

  public static final ResourceKey<Registry<BowPartDefinition>> BOW_REST_TYPE_KEY =
      ResourceKey.createRegistryKey(MoreBowsAndArrows.identifier("bow_rest_type"));

  public static final ResourceKey<BowPartDefinition> DEFAULT_LIMB =
      ResourceKey.create(BOW_LIMB_TYPE_KEY, MoreBowsAndArrows.identifier("default"));

  public static final ResourceKey<BowPartDefinition> DEFAULT_STRING =
      ResourceKey.create(BOW_STRING_TYPE_KEY, MoreBowsAndArrows.identifier("default"));

  public static final ResourceKey<BowPartDefinition> DEFAULT_RISER =
      ResourceKey.create(BOW_RISER_TYPE_KEY, MoreBowsAndArrows.identifier("default"));

  public static final ResourceKey<BowPartDefinition> DEFAULT_REST =
      ResourceKey.create(BOW_REST_TYPE_KEY, MoreBowsAndArrows.identifier("default"));
}
