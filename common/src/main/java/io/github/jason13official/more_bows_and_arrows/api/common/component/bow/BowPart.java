package io.github.jason13official.more_bows_and_arrows.api.common.component.bow;

import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStats;

public interface BowPart {

  void applyTo(BowStats.Builder stats);
}
