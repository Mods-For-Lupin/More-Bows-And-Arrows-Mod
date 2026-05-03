package io.github.jason13official.more_bows_and_arrows.impl.client.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowPartDefinition;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModRegistries;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record StrungBowTintSource(int slotIndex, int defaultColor) implements ItemTintSource {

  public static final MapCodec<StrungBowTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
          Codec.INT.fieldOf("slot").forGetter(StrungBowTintSource::slotIndex),
          ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(StrungBowTintSource::defaultColor)
      ).apply(i, StrungBowTintSource::new));

  public StrungBowTintSource(int slotIndex, int defaultColor) {
    this.slotIndex = slotIndex;
    this.defaultColor = ARGB.opaque(defaultColor);
  }

  @Override
  public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
    if (clientLevel == null) return defaultColor;
    return switch (slotIndex) {
      case 0 -> resolveColor(itemStack, clientLevel, ModDataComponents.BOW_LIMB_TYPE,   ModRegistries.BOW_LIMB_TYPE_KEY);
      case 1 -> resolveColor(itemStack, clientLevel, ModDataComponents.BOW_REST_TYPE,   ModRegistries.BOW_REST_TYPE_KEY);
      case 2 -> resolveColor(itemStack, clientLevel, ModDataComponents.BOW_RISER_TYPE,  ModRegistries.BOW_RISER_TYPE_KEY);
      case 3 -> resolveColor(itemStack, clientLevel, ModDataComponents.BOW_STRING_TYPE,  ModRegistries.BOW_STRING_TYPE_KEY);
      default -> defaultColor;
    };
  }

  private int resolveColor(
      ItemStack stack, ClientLevel level,
      DataComponentType<ResourceKey<BowPartDefinition>> component,
      ResourceKey<Registry<BowPartDefinition>> registryKey
  ) {
    ResourceKey<BowPartDefinition> key = stack.get(component);
    if (key == null) return defaultColor;
    return level.registryAccess().lookup(registryKey)
        .flatMap(reg -> reg.get(key))
        .map(h -> ARGB.opaque(h.value().color()))
        .orElse(defaultColor);
  }

  @Override
  public MapCodec<? extends ItemTintSource> type() {
    return MAP_CODEC;
  }
}
