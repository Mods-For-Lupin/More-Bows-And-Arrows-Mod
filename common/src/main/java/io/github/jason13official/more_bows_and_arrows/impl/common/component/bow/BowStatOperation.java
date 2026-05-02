package io.github.jason13official.more_bows_and_arrows.impl.common.component.bow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public enum BowStatOperation {
  ADD, MULTIPLY, SET;

  public static final Codec<BowStatOperation> CODEC = Codec.STRING.comapFlatMap(
      s -> {
        try {
          return DataResult.success(BowStatOperation.valueOf(s.toUpperCase()));
        } catch (IllegalArgumentException e) {
          return DataResult.error(() -> "Unknown bow stat operation: " + s);
        }
      },
      op -> op.name().toLowerCase()
  );
}
