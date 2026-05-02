package io.github.jason13official.more_bows_and_arrows.impl.client.item;

import com.mojang.serialization.MapCodec;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
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

    if (itemStack.has(ModDataComponents.BOW_LIMB_DATA)) return itemStack.get(ModDataComponents.BOW_LIMB_DATA).color();
    if (itemStack.has(ModDataComponents.BOW_STRING_DATA)) return itemStack.get(ModDataComponents.BOW_STRING_DATA).color();

    return this.defaultColor;
  }

  @Override
  public MapCodec<? extends ItemTintSource> type() {
    return MAP_CODEC;
  }
}
