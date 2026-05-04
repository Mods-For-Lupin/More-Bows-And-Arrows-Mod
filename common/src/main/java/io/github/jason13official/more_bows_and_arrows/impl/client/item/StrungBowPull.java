package io.github.jason13official.more_bows_and_arrows.impl.client.item;

import com.mojang.serialization.MapCodec;
import io.github.jason13official.more_bows_and_arrows.impl.common.item.StrungBowItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class StrungBowPull implements RangeSelectItemModelProperty {
    public static final MapCodec<StrungBowPull> MAP_CODEC = MapCodec.unit(new StrungBowPull());

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        LivingEntity entity = owner == null ? null : owner.asLivingEntity();
        if (entity == null || entity.getUseItem() != itemStack || level == null) {
            return 0.0F;
        }
        int ticksHeld = UseDuration.useDuration(itemStack, entity);
        return StrungBowItem.getPowerForTime(itemStack, level.registryAccess(), ticksHeld);
    }

    @Override
    public MapCodec<StrungBowPull> type() {
        return MAP_CODEC;
    }
}
