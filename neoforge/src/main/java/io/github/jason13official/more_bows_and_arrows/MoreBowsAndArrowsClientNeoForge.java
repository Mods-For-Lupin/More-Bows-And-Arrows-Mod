package io.github.jason13official.more_bows_and_arrows;

import com.mojang.blaze3d.platform.InputConstants.Type;
import io.github.jason13official.more_bows_and_arrows.impl.client.inventory.BowyerScreen;
import io.github.jason13official.more_bows_and_arrows.impl.client.item.BowPartTintSource;
import io.github.jason13official.more_bows_and_arrows.impl.client.item.StrungBowTintSource;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModMenus;
import java.util.function.Consumer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyMapping.Category;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.ItemTintSources;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.util.Lazy;
import org.jline.keymap.KeyMap;
import org.lwjgl.glfw.GLFW;

public class MoreBowsAndArrowsClientNeoForge {

//  public static final KeyMapping.Category MORE_BOWS_AND_ARROWS = new Category(MoreBowsAndArrows.identifier(Constants.MOD_ID));
//  public static Lazy<KeyMapping> TOOLTIP_INFO = Lazy.of(() -> new KeyMapping("key.more_bows_and_arrows.tooltip_info", Type.KEYSYM, GLFW.GLFW_KEY_LEFT_SHIFT, MORE_BOWS_AND_ARROWS));
//
//  public static boolean displayAdditionalTooltipInfo = false;

  public MoreBowsAndArrowsClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> {
      MoreBowsAndArrowsClient.init();
    });

//    modEventBus.addListener((Consumer<RegisterKeyMappingsEvent>) event -> {
//      event.registerCategory(MORE_BOWS_AND_ARROWS);
//      event.register(TOOLTIP_INFO.get());
//    });
//
//    modEventBus.addListener((Consumer<ClientTickEvent.Post>) event -> {
//      while (TOOLTIP_INFO.get().consumeClick()) {
//        displayAdditionalTooltipInfo = true; // toggle value
//      }
//    });

    modEventBus.addListener((Consumer<RegisterMenuScreensEvent>) event -> {
      event.register(ModMenus.BOWYER, BowyerScreen::new);
    });

    modEventBus.addListener((Consumer<RegisterColorHandlersEvent.ItemTintSources>) event -> {
      event.register(MoreBowsAndArrows.identifier("bow_part"), BowPartTintSource.MAP_CODEC);
      event.register(MoreBowsAndArrows.identifier("strung_bow"), StrungBowTintSource.MAP_CODEC);
    });
  }
}
