package io.github.jason13official.more_bows_and_arrows.impl.common.inventory;

import io.github.jason13official.more_bows_and_arrows.api.common.component.bow.BowAssembler;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowPartDefinition;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStats;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModItems;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModMenus;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BowyerMenu extends ItemCombinerMenu {

  public static final int SLOT_LIMB   = 0;
  public static final int SLOT_REST   = 1;
  public static final int SLOT_RISER  = 2;
  public static final int SLOT_STRING = 3;
  public static final int SLOT_OUTPUT = 4;

  public BowyerMenu(int containerId, Inventory inventory) {
    this(containerId, inventory, ContainerLevelAccess.NULL);
  }

  public BowyerMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
    super(ModMenus.BOWYER, containerId, inventory, access, createInputSlotDefinitions());
  }

  private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
    return ItemCombinerMenuSlotDefinition.create()
        .withSlot(SLOT_LIMB,   27 + (18 * SLOT_LIMB),   47, stack -> stack.has(ModDataComponents.BOW_LIMB_TYPE))
        .withSlot(SLOT_REST,   27 + (18 * SLOT_REST),   47, stack -> stack.has(ModDataComponents.BOW_REST_TYPE))
        .withSlot(SLOT_RISER,  27 + (18 * SLOT_RISER),  47, stack -> stack.has(ModDataComponents.BOW_RISER_TYPE))
        .withSlot(SLOT_STRING, 27 + (18 * SLOT_STRING), 47, stack -> stack.has(ModDataComponents.BOW_STRING_TYPE))
        .withResultSlot(SLOT_OUTPUT, 134, 47)
        .build();
  }

  @Override
  public void createResult() {
    ItemStack limb   = this.inputSlots.getItem(SLOT_LIMB);
    ItemStack string = this.inputSlots.getItem(SLOT_STRING);

    if (limb.isEmpty() || string.isEmpty()) {
      this.resultSlots.setItem(0, ItemStack.EMPTY);
      return;
    }

    ResourceKey<BowPartDefinition> limbKey   = limb.get(ModDataComponents.BOW_LIMB_TYPE);
    ResourceKey<BowPartDefinition> stringKey = string.get(ModDataComponents.BOW_STRING_TYPE);

    if (limbKey == null || stringKey == null) {
      this.resultSlots.setItem(0, ItemStack.EMPTY);
      return;
    }

    ItemStack result = new ItemStack(ModItems.STRUNG_BOW);
    result.set(ModDataComponents.BOW_LIMB_TYPE,   limbKey);
    result.set(ModDataComponents.BOW_STRING_TYPE, stringKey);

    ItemStack rest  = this.inputSlots.getItem(SLOT_REST);
    ItemStack riser = this.inputSlots.getItem(SLOT_RISER);

    if (!rest.isEmpty()) {
      ResourceKey<BowPartDefinition> restKey = rest.get(ModDataComponents.BOW_REST_TYPE);
      if (restKey != null) result.set(ModDataComponents.BOW_REST_TYPE, restKey);
    }
    if (!riser.isEmpty()) {
      ResourceKey<BowPartDefinition> riserKey = riser.get(ModDataComponents.BOW_RISER_TYPE);
      if (riserKey != null) result.set(ModDataComponents.BOW_RISER_TYPE, riserKey);
    }

    // Base durability (50) + added durability from all present parts resolved via registry
    int[] maxDamage = {50};
    this.access.execute((level, pos) -> {
      BowStats stats = BowAssembler.assemble(result, level.registryAccess());
      maxDamage[0] = 50 + stats.getAddedDurability();
    });
    result.set(DataComponents.MAX_DAMAGE, maxDamage[0]);

    this.resultSlots.setItem(0, result);
  }

  @Override
  protected void onTake(Player player, ItemStack carried) {
    carried.onCraftedBy(player, carried.getCount());
    shrinkInputIfPresent(SLOT_LIMB);
    shrinkInputIfPresent(SLOT_STRING);
    shrinkInputIfPresent(SLOT_REST);
    shrinkInputIfPresent(SLOT_RISER);
  }

  private void shrinkInputIfPresent(int slot) {
    ItemStack stack = this.inputSlots.getItem(slot);
    if (!stack.isEmpty()) {
      stack.shrink(1);
      this.inputSlots.setItem(slot, stack);
    }
  }

  @Override
  protected boolean canMoveIntoInputSlots(ItemStack stack) {
    return (stack.has(ModDataComponents.BOW_LIMB_TYPE)   && !this.getSlot(SLOT_LIMB).hasItem())
        || (stack.has(ModDataComponents.BOW_STRING_TYPE)  && !this.getSlot(SLOT_STRING).hasItem())
        || (stack.has(ModDataComponents.BOW_RISER_TYPE)   && !this.getSlot(SLOT_RISER).hasItem())
        || (stack.has(ModDataComponents.BOW_REST_TYPE)    && !this.getSlot(SLOT_REST).hasItem());
  }

  @Override
  protected boolean isValidBlock(BlockState blockState) {
    return blockState.is(Blocks.FLETCHING_TABLE);
  }
}
