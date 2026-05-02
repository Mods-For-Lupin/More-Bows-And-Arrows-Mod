package io.github.jason13official.more_bows_and_arrows.impl.common.component.bow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jason13official.more_bows_and_arrows.api.common.component.bow.BowPart;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.ExtraCodecs;

public record BowPartDefinition(Component description, List<BowStatEffect> effects, int addedDurability, int color) implements BowPart {

  public static final Codec<BowPartDefinition> DIRECT_CODEC = RecordCodecBuilder.create(
      i -> i.group(
          ComponentSerialization.CODEC.fieldOf("description").forGetter(BowPartDefinition::description),
          BowStatEffect.CODEC.listOf().fieldOf("effects").forGetter(BowPartDefinition::effects),
          Codec.INT.fieldOf("added_durability").forGetter(BowPartDefinition::addedDurability),
          ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(BowPartDefinition::color)
      ).apply(i, BowPartDefinition::new));

  @Override
  public void applyTo(BowStats stats) {
    for (BowStatEffect effect : effects) stats.apply(effect);
    stats.addDurability(addedDurability);
  }
}
