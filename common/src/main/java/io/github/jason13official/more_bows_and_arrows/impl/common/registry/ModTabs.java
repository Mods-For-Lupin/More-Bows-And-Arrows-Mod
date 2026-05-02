package io.github.jason13official.more_bows_and_arrows.impl.common.registry;

import io.github.jason13official.more_bows_and_arrows.Constants;
import io.github.jason13official.more_bows_and_arrows.MoreBowsAndArrows;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowPartDefinition;
import io.github.jason13official.more_bows_and_arrows.platform.Services;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModTabs {

  public static CreativeModeTab MORE_BOWS_AND_ARROWS;

  public static void register(BiConsumer<CreativeModeTab, Identifier> consumer) {

    MORE_BOWS_AND_ARROWS = Services.registry().tabBuilder()
        .icon(() -> new ItemStack(Items.BOW))
        .title(Component.literal("More Bows and Arrows"))
        .displayItems((display, output) -> {
      generateBowPartTypes(display, output);
    }).build();

    consumer.accept(MORE_BOWS_AND_ARROWS, MoreBowsAndArrows.identifier(Constants.MOD_ID));
  }

  public static void generateBowPartTypes(
      CreativeModeTab.ItemDisplayParameters params,
      CreativeModeTab.Output output
  ) {
    populateSlot(params, output, ModRegistries.BOW_LIMB_TYPE_KEY,   ModDataComponents.BOW_LIMB_TYPE,   ModItems.BOW_LIMB);
    populateSlot(params, output, ModRegistries.BOW_STRING_TYPE_KEY,  ModDataComponents.BOW_STRING_TYPE,  ModItems.BOW_STRING);
    populateSlot(params, output, ModRegistries.BOW_RISER_TYPE_KEY,  ModDataComponents.BOW_RISER_TYPE,  ModItems.BOW_RISER);
    populateSlot(params, output, ModRegistries.BOW_REST_TYPE_KEY,   ModDataComponents.BOW_REST_TYPE,   ModItems.BOW_REST);
  }

  private static void populateSlot(
      CreativeModeTab.ItemDisplayParameters params,
      CreativeModeTab.Output output,
      ResourceKey<Registry<BowPartDefinition>> registryKey,
      DataComponentType<ResourceKey<BowPartDefinition>> component,
      Item item
  ) {
    params.holders().lookup(registryKey).ifPresent(registry ->
        registry.listElements().forEach(holder -> {
          ItemStack stack = new ItemStack(item);
          stack.set(component, holder.key());
          output.accept(stack);
        })
    );
  }
}
