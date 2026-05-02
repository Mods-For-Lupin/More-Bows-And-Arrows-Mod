package io.github.jason13official.more_bows_and_arrows.platform;

import io.github.jason13official.more_bows_and_arrows.platform.services.IRegistryHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;

public class NeoForgeRegistryHelper implements IRegistryHelper {

  @Override
  public Builder tabBuilder() {
    return CreativeModeTab.builder();
  }
}
