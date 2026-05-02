package io.github.jason13official.more_bows_and_arrows.impl.common.component.bow;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;

public class BowStats {

  private final Map<Identifier, Float> values = new HashMap<>(BowStat.DEFAULTS);
  private int addedDurability = 0;

  public float get(Identifier stat) {
    return values.getOrDefault(stat, 0.0f);
  }

  public void apply(BowStatEffect effect) {
    float current = get(effect.stat());
    values.put(effect.stat(), switch (effect.operation()) {
      case ADD      -> current + effect.value();
      case MULTIPLY -> current * effect.value();
      case SET      -> effect.value();
    });
  }

  public void addDurability(int amount) {
    addedDurability += amount;
  }

  public int getAddedDurability() {
    return addedDurability;
  }
}
