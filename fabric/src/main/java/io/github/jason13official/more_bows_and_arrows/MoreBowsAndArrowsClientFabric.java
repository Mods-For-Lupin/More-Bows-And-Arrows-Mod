package io.github.jason13official.more_bows_and_arrows;

import net.fabricmc.api.ClientModInitializer;

public class MoreBowsAndArrowsClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    MoreBowsAndArrowsClient.init();
  }
}
