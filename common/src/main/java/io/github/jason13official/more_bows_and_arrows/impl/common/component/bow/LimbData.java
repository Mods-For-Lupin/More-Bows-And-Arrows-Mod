package io.github.jason13official.more_bows_and_arrows.impl.common.component.bow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jason13official.more_bows_and_arrows.api.common.component.bow.BowPart;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStats.Builder;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;

public record LimbData(float drawSpeedModifier, float arrowVelocityModifier, float critChanceModifier, int addedDurability, int color) implements BowPart {

  public static final LimbData DEFAULT = new LimbData(1.0f, 1.0f, 1.0f, 50, ARGB.opaque(0xFF0000));

  public static final Codec<LimbData> CODEC = RecordCodecBuilder.create(
      i -> i.group(
          Codec.FLOAT.fieldOf("drawSpeedModifier").forGetter(LimbData::drawSpeedModifier),
          Codec.FLOAT.fieldOf("arrowVelocityModifier").forGetter(LimbData::arrowVelocityModifier),
          Codec.FLOAT.optionalFieldOf("critChanceModifier", 0f).forGetter(LimbData::critChanceModifier),
          Codec.INT.fieldOf("addedDurability").forGetter(LimbData::addedDurability),
          ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(LimbData::color)
      ).apply(i, LimbData::new));

  @Override
  public void applyTo(BowStats.Builder stats) {
    stats.set(BowStat.DRAW_TIME, drawSpeedModifier);
    stats.set(BowStat.VELOCITY, arrowVelocityModifier);
    stats.set(BowStat.CRIT_CHANCE, critChanceModifier);
    stats.set(BowStat.DURABILITY, addedDurability);
  }
}
