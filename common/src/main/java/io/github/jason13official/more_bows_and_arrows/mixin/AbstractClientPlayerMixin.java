package io.github.jason13official.more_bows_and_arrows.mixin;

import com.mojang.authlib.GameProfile;
import io.github.jason13official.more_bows_and_arrows.api.common.component.bow.BowAssembler;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStat;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStats;
import io.github.jason13official.more_bows_and_arrows.impl.common.item.StrungBowItem;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player implements ClientAvatarEntity {

  @Inject(at = @At("TAIL"), method = "getFieldOfViewModifier", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
  private void more_bows_and_arrows$getFieldOfViewModifier(boolean firstPerson, float effectScale, CallbackInfoReturnable<Float> cir, float modifier, float walkingSpeed) {
    AbstractClientPlayer self = (AbstractClientPlayer) (Object) this;

    if (self.isUsingItem() && self.getUseItem().getItem() instanceof StrungBowItem strungBow) {

      BowStats stats = BowAssembler.assemble(self.getUseItem(), self.registryAccess());

      float drawSpeed = stats.get(BowStat.DRAW_SPEED);

      float scale = Math.min(this.getTicksUsingItem() / (20.0F * drawSpeed), 1.0F);
      modifier *= 1.0F - Mth.square(scale) * 0.15F;
      cir.setReturnValue(Mth.lerp(effectScale, 1.0F, modifier));
    }
  }

  public AbstractClientPlayerMixin(Level level, GameProfile gameProfile) {
    super(level, gameProfile);
  }
}
