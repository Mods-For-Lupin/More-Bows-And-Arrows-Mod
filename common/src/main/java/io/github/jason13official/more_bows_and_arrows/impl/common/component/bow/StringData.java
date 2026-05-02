package io.github.jason13official.more_bows_and_arrows.impl.common.component.bow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jason13official.more_bows_and_arrows.api.common.component.bow.BowPart;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;

public record StringData(float arrowVelocityModifier, float arrowAccuracyModifier, float drawSpeedModifier, int addedDurability, int color) implements BowPart {

  public static final StringData DEFAULT = new StringData(1.0f, 1.0f, 1.0f, 50, ARGB.opaque(0x00FF00));

  public static final Codec<StringData> CODEC = RecordCodecBuilder.create(
      i -> i.group(
          Codec.FLOAT.optionalFieldOf("arrowVelocityModifier", 1f).forGetter(StringData::arrowVelocityModifier),
          Codec.FLOAT.optionalFieldOf("arrowAccuracyModifier", 0f).forGetter(StringData::arrowAccuracyModifier),
          Codec.FLOAT.optionalFieldOf("drawSpeedModifier", 1f).forGetter(StringData::drawSpeedModifier),
          Codec.INT.fieldOf("addedDurability").forGetter(StringData::addedDurability),
          ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(StringData::color)
      ).apply(i, StringData::new));

  @Override
  public void applyTo(BowStats.Builder stats) {
    stats.modify(BowStat.VELOCITY, v -> v * arrowVelocityModifier);
    stats.modify(BowStat.DRAW_TIME, d -> d * drawSpeedModifier);
    stats.set(BowStat.ACCURACY, arrowAccuracyModifier);
    stats.set(BowStat.DURABILITY, addedDurability);
  }
}
