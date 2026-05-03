package io.github.jason13official.more_bows_and_arrows.impl.common.registry;

import io.github.jason13official.more_bows_and_arrows.MoreBowsAndArrows;
import io.github.jason13official.more_bows_and_arrows.impl.common.inventory.BowyerMenu;
import io.github.jason13official.more_bows_and_arrows.platform.Services;
import java.util.function.BiConsumer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {

  public static MenuType<BowyerMenu> BOWYER;

  public static void register(BiConsumer<MenuType<?>, Identifier> consumer) {

    BOWYER = Services.registry().menuType(BowyerMenu::new, FeatureFlags.VANILLA_SET);

    consumer.accept(BOWYER, MoreBowsAndArrows.identifier("bowyer"));
  }
}
