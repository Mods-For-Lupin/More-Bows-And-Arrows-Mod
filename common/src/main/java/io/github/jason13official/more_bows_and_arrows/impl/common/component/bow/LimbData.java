package io.github.jason13official.more_bows_and_arrows.impl.common.component.bow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jason13official.more_bows_and_arrows.api.common.component.bow.BowPart;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStats.Builder;

public record LimbData(float drawSpeedModifier, float arrowVelocityModifier, float critChanceModifier, int addedDurability) implements BowPart {

  public static final LimbData DEFAULT = new LimbData(1.0f, 1.0f, 1.0f, 50);

  public static final Codec<LimbData> CODEC = RecordCodecBuilder.create(
      i -> i.group(Codec.FLOAT.fieldOf("draw_time").forGetter(LimbData::drawSpeedModifier), Codec.FLOAT.fieldOf("velocity").forGetter(LimbData::arrowVelocityModifier),
          Codec.FLOAT.optionalFieldOf("crit_chance", 0f).forGetter(LimbData::critChanceModifier), Codec.INT.fieldOf("addedDurability").forGetter(LimbData::addedDurability)).apply(i, LimbData::new));

  @Override
  public void applyTo(Builder stats) {
    stats.set(BowStat.DRAW_TIME, drawSpeedModifier);
    stats.set(BowStat.VELOCITY, arrowVelocityModifier);
    stats.set(BowStat.CRIT_CHANCE, critChanceModifier);
    stats.set(BowStat.DURABILITY, addedDurability);
  }
}
