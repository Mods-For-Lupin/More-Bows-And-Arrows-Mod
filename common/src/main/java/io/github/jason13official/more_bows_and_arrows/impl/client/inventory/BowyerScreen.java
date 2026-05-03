package io.github.jason13official.more_bows_and_arrows.impl.client.inventory;

import io.github.jason13official.more_bows_and_arrows.MoreBowsAndArrows;
import io.github.jason13official.more_bows_and_arrows.impl.common.inventory.BowyerMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class BowyerScreen extends ItemCombinerScreen<BowyerMenu> {

  private static final Identifier ANVIL_LOCATION = MoreBowsAndArrows.identifier("textures/gui/container/bowyer.png");

  public BowyerScreen(BowyerMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title, ANVIL_LOCATION);
  }

  @Override
  protected void extractErrorIcon(GuiGraphicsExtractor guiGraphicsExtractor, int i, int i1) {

  }
}
