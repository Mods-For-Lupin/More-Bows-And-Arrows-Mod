package io.github.jason13official.more_bows_and_arrows.impl.common.component.bow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jason13official.more_bows_and_arrows.api.common.component.bow.BowPart;
import net.minecraft.util.ExtraCodecs;

public record BowLimbType(float drawSpeedModifier, float arrowVelocityModifier, float critChanceModifier, int addedDurability, int color) implements BowPart {

  public static final Codec<BowLimbType> DIRECT_CODEC = RecordCodecBuilder.create(
      i -> i.group(
          Codec.FLOAT.fieldOf("draw_speed_modifier").forGetter(BowLimbType::drawSpeedModifier),
          Codec.FLOAT.fieldOf("arrow_velocity_modifier").forGetter(BowLimbType::arrowVelocityModifier),
          Codec.FLOAT.optionalFieldOf("crit_chance_modifier", 0f).forGetter(BowLimbType::critChanceModifier),
          Codec.INT.fieldOf("added_durability").forGetter(BowLimbType::addedDurability),
          ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(BowLimbType::color)
      ).apply(i, BowLimbType::new));

  @Override
  public void applyTo(BowStats.Builder stats) {
    stats.set(BowStat.DRAW_TIME, drawSpeedModifier);
    stats.set(BowStat.VELOCITY, arrowVelocityModifier);
    stats.set(BowStat.CRIT_CHANCE, critChanceModifier);
    stats.set(BowStat.DURABILITY, addedDurability);
  }
}
