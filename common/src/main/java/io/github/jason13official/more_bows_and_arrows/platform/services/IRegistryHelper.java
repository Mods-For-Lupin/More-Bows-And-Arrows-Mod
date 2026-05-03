package io.github.jason13official.more_bows_and_arrows.platform.services;

import java.util.function.BiFunction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;

public interface IRegistryHelper {

  CreativeModeTab.Builder tabBuilder();

  <T extends AbstractContainerMenu> MenuType<T> menuType(BiFunction<Integer, Inventory, T> constructor, FeatureFlagSet flags);
}
