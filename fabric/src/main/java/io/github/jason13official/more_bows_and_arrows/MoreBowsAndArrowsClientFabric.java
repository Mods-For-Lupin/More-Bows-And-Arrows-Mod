package io.github.jason13official.more_bows_and_arrows;

import com.mojang.blaze3d.platform.InputConstants.Type;
import io.github.jason13official.more_bows_and_arrows.impl.client.item.BowPartTintSource;
import io.github.jason13official.more_bows_and_arrows.impl.client.item.StrungBowTintSource;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.client.color.item.ItemTintSources;
import org.lwjgl.glfw.GLFW;

public class MoreBowsAndArrowsClientFabric implements ClientModInitializer {

  public static final KeyMapping.Category MORE_BOWS_AND_ARROWS = new Category(MoreBowsAndArrows.identifier(Constants.MOD_ID));
  public static KeyMapping TOOLTIP_INFO = new KeyMapping("key.more_bows_and_arrows.tooltip_info", Type.KEYSYM, GLFW.GLFW_KEY_LEFT_SHIFT, MORE_BOWS_AND_ARROWS);

  @Override
  public void onInitializeClient() {

    MoreBowsAndArrowsClient.init();

    ItemTintSources.ID_MAPPER.put(MoreBowsAndArrows.identifier("bow_part"), BowPartTintSource.MAP_CODEC);
    ItemTintSources.ID_MAPPER.put(MoreBowsAndArrows.identifier("strung_bow"), StrungBowTintSource.MAP_CODEC);
  }
}
