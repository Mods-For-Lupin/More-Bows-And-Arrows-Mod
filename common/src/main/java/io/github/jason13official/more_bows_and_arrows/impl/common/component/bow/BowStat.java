package io.github.jason13official.more_bows_and_arrows.impl.common.component.bow;

import io.github.jason13official.more_bows_and_arrows.MoreBowsAndArrows;
import java.util.function.BinaryOperator;
import net.minecraft.resources.Identifier;

public record BowStat<T>(Identifier id, T defaultValue, BinaryOperator<T> combiner) {

  public static final BowStat<Float> VELOCITY = create(MoreBowsAndArrows.identifier("velocity"), 1.0f, Float::sum);
  public static final BowStat<Float> DRAW_TIME = create(MoreBowsAndArrows.identifier("draw_time"), 20f, Float::sum);
  public static final BowStat<Float> ACCURACY = create(MoreBowsAndArrows.identifier("accuracy"), 1.0f, Float::sum);
  public static final BowStat<Float> DAMAGE = create(MoreBowsAndArrows.identifier("damage"), 1.0f, Float::sum);
  public static final BowStat<Float> CRIT_CHANCE = create(MoreBowsAndArrows.identifier("crit_chance"), 0.0f, Float::sum);
  public static final BowStat<Float> STEALTH = create(MoreBowsAndArrows.identifier("stealth"), 1.0f, Float::sum);
  public static final BowStat<Integer> DURABILITY = create(MoreBowsAndArrows.identifier("addedDurability"), 0, Integer::sum);

  public static <T> BowStat<T> create(Identifier internalId, T defaultValue, BinaryOperator<T> combiner) {
    return new BowStat<>(internalId, defaultValue, combiner);
  }
}
