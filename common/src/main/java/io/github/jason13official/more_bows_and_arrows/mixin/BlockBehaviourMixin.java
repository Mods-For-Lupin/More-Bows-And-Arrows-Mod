package io.github.jason13official.more_bows_and_arrows.mixin;

import io.github.jason13official.more_bows_and_arrows.impl.common.inventory.BowyerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

  @Inject(at = @At("HEAD"), method = "useItemOn")
  private void more_bows_and_arrows$useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult,
      CallbackInfoReturnable<InteractionResult> cir) {
    BlockBehaviour behaviour = (BlockBehaviour) (Object) this;

    if (!(behaviour instanceof Block block)) return;
    if (block != Blocks.FLETCHING_TABLE) return;

    ContainerLevelAccess access = ContainerLevelAccess.create(level, pos);
    player.openMenu(new SimpleMenuProvider(
        (i, inventory, playerX) -> new BowyerMenu(i, inventory, access),
        Component.literal("Bowyer")
    ));
  }
}
