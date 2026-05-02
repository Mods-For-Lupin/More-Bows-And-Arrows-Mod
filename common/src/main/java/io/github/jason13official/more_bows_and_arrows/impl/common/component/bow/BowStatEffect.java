package io.github.jason13official.more_bows_and_arrows.impl.common.component.bow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record BowStatEffect(Identifier stat, BowStatOperation operation, float value) {

  public static final Codec<BowStatEffect> CODEC = RecordCodecBuilder.create(
      i -> i.group(
          Identifier.CODEC.fieldOf("stat").forGetter(BowStatEffect::stat),
          BowStatOperation.CODEC.fieldOf("operation").forGetter(BowStatEffect::operation),
          Codec.FLOAT.fieldOf("value").forGetter(BowStatEffect::value)
      ).apply(i, BowStatEffect::new));
}
