package io.github.jason13official.more_bows_and_arrows.impl.common.component.bow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jason13official.more_bows_and_arrows.api.common.component.bow.BowPart;
import net.minecraft.util.ExtraCodecs;

public record BowStringType(float arrowVelocityModifier, float arrowAccuracyModifier, float drawSpeedModifier, int addedDurability, int color) implements BowPart {

  public static final Codec<BowStringType> DIRECT_CODEC = RecordCodecBuilder.create(
      i -> i.group(
          Codec.FLOAT.optionalFieldOf("arrow_velocity_modifier", 1f).forGetter(BowStringType::arrowVelocityModifier),
          Codec.FLOAT.optionalFieldOf("arrow_accuracy_modifier", 0f).forGetter(BowStringType::arrowAccuracyModifier),
          Codec.FLOAT.optionalFieldOf("draw_speed_modifier", 1f).forGetter(BowStringType::drawSpeedModifier),
          Codec.INT.fieldOf("added_durability").forGetter(BowStringType::addedDurability),
          ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(BowStringType::color)
      ).apply(i, BowStringType::new));

  @Override
  public void applyTo(BowStats.Builder stats) {
    stats.modify(BowStat.VELOCITY, v -> v * arrowVelocityModifier);
    stats.modify(BowStat.DRAW_TIME, d -> d * drawSpeedModifier);
    stats.set(BowStat.ACCURACY, arrowAccuracyModifier);
    stats.set(BowStat.DURABILITY, addedDurability);
  }
}
