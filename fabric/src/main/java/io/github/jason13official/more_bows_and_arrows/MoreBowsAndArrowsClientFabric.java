package io.github.jason13official.more_bows_and_arrows;

import io.github.jason13official.more_bows_and_arrows.impl.client.item.BowPartTintSource;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.color.item.ItemTintSources;

public class MoreBowsAndArrowsClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    MoreBowsAndArrowsClient.init();

    ItemTintSources.ID_MAPPER.put(MoreBowsAndArrows.identifier("bow_part"), BowPartTintSource.MAP_CODEC);
  }
}
