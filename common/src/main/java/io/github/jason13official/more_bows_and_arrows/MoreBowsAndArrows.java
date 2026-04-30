package io.github.jason13official.more_bows_and_arrows;

import net.minecraft.resources.Identifier;

public class MoreBowsAndArrows {

  public static void init() {
  }

  public static Identifier identifier(final String path) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}