package io.github.jason13official.more_bows_and_arrows.platform;

import io.github.jason13official.more_bows_and_arrows.platform.services.IRegistryHelper;
import java.util.function.BiFunction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;

public class NeoForgeRegistryHelper implements IRegistryHelper {

  @Override
  public Builder tabBuilder() {
    return CreativeModeTab.builder();
  }

  @Override
  public <T extends AbstractContainerMenu> MenuType<T> menuType(BiFunction<Integer, Inventory, T> constructor, FeatureFlagSet flags) {
    return new MenuType<>(constructor::apply, flags);
  }
}
