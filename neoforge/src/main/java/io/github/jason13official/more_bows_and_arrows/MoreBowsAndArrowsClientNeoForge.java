package io.github.jason13official.more_bows_and_arrows;

import io.github.jason13official.more_bows_and_arrows.impl.client.inventory.BowyerScreen;
import io.github.jason13official.more_bows_and_arrows.impl.client.item.BowPartTintSource;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModMenus;
import java.util.function.Consumer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.ItemTintSources;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class MoreBowsAndArrowsClientNeoForge {

  public MoreBowsAndArrowsClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> {
      MoreBowsAndArrowsClient.init();
    });

    modEventBus.addListener((Consumer<RegisterMenuScreensEvent>) event -> {
      event.register(ModMenus.BOWYER, BowyerScreen::new);
    });

    modEventBus.addListener((Consumer<RegisterColorHandlersEvent.ItemTintSources>) event -> {
      event.register(MoreBowsAndArrows.identifier("bow_part"), BowPartTintSource.MAP_CODEC);
    });
  }
}
