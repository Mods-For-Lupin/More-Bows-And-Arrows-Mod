package io.github.jason13official.more_bows_and_arrows.impl.common.item;

import io.github.jason13official.more_bows_and_arrows.api.common.component.bow.BowAssembler;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStat;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStats;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class StrungBowItem extends BowItem {

  public StrungBowItem(Properties properties) {
    super(properties);
  }

  public static float getPowerForTime(ItemStack stack, RegistryAccess registryAccess, int timeHeld) {

    BowStats stats = BowAssembler.assemble(stack, registryAccess);
    float drawSpeed = stats.get(BowStat.DRAW_SPEED);

    float pow = (float) timeHeld / (20.0F * drawSpeed);
    pow = (pow * pow + pow * 2.0F) / 3.0F;
    if (pow > 1.0F) {
      pow = 1.0F;
    }

    return pow;
  }

  @Override
  public Predicate<ItemStack> getAllSupportedProjectiles() {
    return ARROW_ONLY;
  }

  @Override
  public int getDefaultProjectileRange() {
    return 15;
  }

  @Override
  protected void shootProjectile(LivingEntity shooter, Projectile projectileEntity, int index, float power, float uncertainty, float angle, @Nullable LivingEntity targetOverride) {
    projectileEntity.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, power, uncertainty);
  }

  @Override
  public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
    return ItemUseAnimation.BOW;
  }

  @Override
  public int getUseDuration(ItemStack itemStack, LivingEntity user) {
    return 72000;
  }

  @Override
  public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
    if (entity instanceof Player player) {
      ItemStack projectile = player.getProjectile(itemStack);
      if (projectile.isEmpty()) {
        return false;
      } else {
        int timeHeld = this.getUseDuration(itemStack, entity) - remainingTime;
        float pow = getPowerForTime(itemStack, level.registryAccess(), timeHeld);
        if ((double) pow < 0.1) {
          return false;
        } else {
          List<ItemStack> firedProjectiles = draw(itemStack, projectile, player);
          if (level instanceof ServerLevel serverLevel) {
            if (!firedProjectiles.isEmpty()) {
              this.shoot(serverLevel, player, player.getUsedItemHand(), itemStack, firedProjectiles, pow * 3.0F, 1.0F, pow == 1.0F, null);
            }
          }

          level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F,
              1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + pow * 0.5F);
          player.awardStat(Stats.ITEM_USED.get(this));
          return true;
        }
      }
    } else {
      return false;
    }
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, display, builder, tooltipFlag);

    if (!Minecraft.getInstance().hasShiftDown()) {

      builder.accept(Component.literal("Hold [SHIFT] to view stats.").withStyle(ChatFormatting.GRAY));
      return;
    }

    HolderLookup.Provider registries = context.registries();
    if (registries != null) {
      BowStats stats = BowAssembler.assemble(stack, registries);
      for (Identifier stat : BowStat.ORDERED) {
        builder.accept(BowPartItem.statLine(stat, String.format("%.2f", stats.get(stat))));
      }
      if (stats.getAddedDurability() != 0) {
        builder.accept(Component.literal("Durability Added: " + stats.getAddedDurability()).withStyle(ChatFormatting.BLUE));
      }
    }

    builder.accept(Component.literal("Max Durability: " + stack.get(DataComponents.MAX_DAMAGE)).withStyle(ChatFormatting.GRAY));
  }
}
