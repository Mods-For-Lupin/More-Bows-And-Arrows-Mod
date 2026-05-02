package io.github.jason13official.more_bows_and_arrows.impl.common.component.bow;

import io.github.jason13official.more_bows_and_arrows.MoreBowsAndArrows;
import java.util.Map;
import net.minecraft.resources.Identifier;

public class BowStat {

  public static final Identifier VELOCITY    = MoreBowsAndArrows.identifier("velocity");
  public static final Identifier DRAW_SPEED  = MoreBowsAndArrows.identifier("draw_speed");
  public static final Identifier ACCURACY    = MoreBowsAndArrows.identifier("accuracy");
  public static final Identifier DAMAGE      = MoreBowsAndArrows.identifier("damage");
  public static final Identifier CRIT_CHANCE = MoreBowsAndArrows.identifier("crit_chance");
  public static final Identifier STEALTH     = MoreBowsAndArrows.identifier("stealth");

  public static final Map<Identifier, Float> DEFAULTS = Map.of(
      VELOCITY,    1.0f,
      DRAW_SPEED,  1.0f,
      ACCURACY,    1.0f,
      DAMAGE,      1.0f,
      CRIT_CHANCE, 0.0f,
      STEALTH,     1.0f
  );
}
