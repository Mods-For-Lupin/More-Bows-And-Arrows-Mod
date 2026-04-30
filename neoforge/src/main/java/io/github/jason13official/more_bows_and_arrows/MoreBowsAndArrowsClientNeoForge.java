package io.github.jason13official.more_bows_and_arrows;

import java.util.function.Consumer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class MoreBowsAndArrowsClientNeoForge {

  public MoreBowsAndArrowsClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> MoreBowsAndArrowsClient.init());
  }
}
