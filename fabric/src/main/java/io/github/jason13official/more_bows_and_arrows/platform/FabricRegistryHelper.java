package io.github.jason13official.more_bows_and_arrows.platform;

import io.github.jason13official.more_bows_and_arrows.platform.services.IRegistryHelper;
import java.util.function.BiFunction;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab.Builder;

public class FabricRegistryHelper implements IRegistryHelper {

  @Override
  public Builder tabBuilder() {
    return FabricCreativeModeTab.builder();
  }

  @Override
  public <T extends AbstractContainerMenu> MenuType<T> menuType(BiFunction<Integer, Inventory, T> constructor, FeatureFlagSet flags) {
    return new MenuType<>(constructor::apply, flags);
  }
}
