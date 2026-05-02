package io.github.jason13official.more_bows_and_arrows.impl.client.item;

import com.mojang.serialization.MapCodec;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowLimbType;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStringType;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModRegistries;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record BowPartTintSource(int defaultColor) implements ItemTintSource {

  public static final MapCodec<BowPartTintSource> MAP_CODEC = ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default")
      .xmap(BowPartTintSource::new, BowPartTintSource::defaultColor);

  public BowPartTintSource(int defaultColor) {
    this.defaultColor = ARGB.opaque(defaultColor);
  }

  @Override
  public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
    if (clientLevel == null) return defaultColor;
    HolderLookup.Provider registries = clientLevel.registryAccess();

    if (itemStack.has(ModDataComponents.BOW_LIMB_TYPE)) {
      ResourceKey<BowLimbType> key = itemStack.get(ModDataComponents.BOW_LIMB_TYPE);
      var color = registries.lookup(ModRegistries.BOW_LIMB_TYPE_KEY)
          .flatMap(reg -> reg.get(key))
          .map(h -> h.value().color());
      if (color.isPresent()) return color.get();
    }

    if (itemStack.has(ModDataComponents.BOW_STRING_TYPE)) {
      ResourceKey<BowStringType> key = itemStack.get(ModDataComponents.BOW_STRING_TYPE);
      var color = registries.lookup(ModRegistries.BOW_STRING_TYPE_KEY)
          .flatMap(reg -> reg.get(key))
          .map(h -> h.value().color());
      if (color.isPresent()) return color.get();
    }

    return defaultColor;
  }

  @Override
  public MapCodec<? extends ItemTintSource> type() {
    return MAP_CODEC;
  }
}
