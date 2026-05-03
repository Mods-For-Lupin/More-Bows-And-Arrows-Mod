package io.github.jason13official.more_bows_and_arrows.impl.common.inventory;

import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class BowyerMenu extends ItemCombinerMenu {

  public static final int SLOT_LIMB = 0;
  public static final int SLOT_REST = 1;
  public static final int SLOT_RISER = 2;
  public static final int SLOT_STRING = 3;
  public static final int SLOT_OUTPUT = 4;

  public BowyerMenu(int containerId, Inventory inventory) {
    this(containerId, inventory, ContainerLevelAccess.NULL);
  }

  public BowyerMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
    super(ModMenus.BOWYER, containerId, inventory, access, BowyerMenu.createInputSlotDefinitions());
  }

  private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {

    ItemCombinerMenuSlotDefinition.Builder builder = ItemCombinerMenuSlotDefinition.create();

    builder.withSlot(SLOT_LIMB,   27 + (18 * SLOT_LIMB), 47, stack -> stack.has(ModDataComponents.BOW_LIMB_TYPE));
    builder.withSlot(SLOT_REST,   27 + (18 * SLOT_REST), 47, stack -> stack.has(ModDataComponents.BOW_REST_TYPE));
    builder.withSlot(SLOT_RISER,  27 + (18 * SLOT_RISER), 47, stack -> stack.has(ModDataComponents.BOW_RISER_TYPE));
    builder.withSlot(SLOT_STRING, 27 + (18 * SLOT_STRING), 47, stack -> stack.has(ModDataComponents.BOW_STRING_TYPE));

    builder.withResultSlot(SLOT_OUTPUT, 134, 47);

    return builder.build();
  }

  @Override
  protected void onTake(Player player, ItemStack itemStack) {

  }

  @Override
  protected boolean isValidBlock(BlockState blockState) {
    return blockState.is(Blocks.FLETCHING_TABLE);
  }

  @Override
  public void createResult() {

  }
}
