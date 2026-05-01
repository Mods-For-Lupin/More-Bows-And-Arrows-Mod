package io.github.jason13official.more_bows_and_arrows.impl.common.component.bow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class BowStats {

  private BowStats(Map<BowStat<?>, Object> values) {
    this.values = Map.copyOf(values);
  }
  private final Map<BowStat<?>, Object> values;

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  public <T> T get(BowStat<T> stat) {
    return (T) values.getOrDefault(stat, stat.defaultValue());
  }

  public static final class Builder {

    private final Map<BowStat<?>, Object> values = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Builder set(BowStat<T> stat, T value) {
      values.merge(stat, value, (a, b) -> stat.combiner().apply((T) a, (T) b));
      return this;
    }

    public <T> Builder modify(BowStat<T> stat, UnaryOperator<T> fn) {
      return set(stat, fn.apply(get(stat)));
    }

    @SuppressWarnings("unchecked")
    public <T> T get(BowStat<T> stat) {
      return (T) values.getOrDefault(stat, stat.defaultValue());
    }

    public BowStats build() {
      return new BowStats(values);
    }
  }
}
