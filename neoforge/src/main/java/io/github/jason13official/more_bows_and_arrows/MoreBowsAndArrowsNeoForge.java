package io.github.jason13official.more_bows_and_arrows;

import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowPartDefinition;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModBlocks;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModDataComponents;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModEntities;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModItems;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModMenus;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModParticles;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModRegistries;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModTabs;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModTiles;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class MoreBowsAndArrowsNeoForge {

  public static IEventBus EVENT_BUS;

  public MoreBowsAndArrowsNeoForge(final IEventBus modEventBus) {

    EVENT_BUS = modEventBus;

    EVENT_BUS.addListener((Consumer<DataPackRegistryEvent.NewRegistry>) event -> {
      event.dataPackRegistry(ModRegistries.BOW_LIMB_TYPE_KEY,   BowPartDefinition.DIRECT_CODEC, BowPartDefinition.DIRECT_CODEC);
      event.dataPackRegistry(ModRegistries.BOW_STRING_TYPE_KEY,  BowPartDefinition.DIRECT_CODEC, BowPartDefinition.DIRECT_CODEC);
      event.dataPackRegistry(ModRegistries.BOW_RISER_TYPE_KEY,  BowPartDefinition.DIRECT_CODEC, BowPartDefinition.DIRECT_CODEC);
      event.dataPackRegistry(ModRegistries.BOW_REST_TYPE_KEY,   BowPartDefinition.DIRECT_CODEC, BowPartDefinition.DIRECT_CODEC);
    });

    bind(Registries.BLOCK, ModBlocks::register);
    bind(Registries.ENTITY_TYPE, ModEntities::register);
    bind(Registries.ITEM, ModItems::register);
    bind(Registries.PARTICLE_TYPE, ModParticles::register);
    bind(Registries.BLOCK_ENTITY_TYPE, ModTiles::register);
    bind(Registries.MENU, ModMenus::register);
    bind(Registries.CREATIVE_MODE_TAB, ModTabs::register);
    bind(Registries.DATA_COMPONENT_TYPE, ModDataComponents::register);

    EVENT_BUS.addListener((Consumer<FMLCommonSetupEvent>) event -> MoreBowsAndArrows.init());

    NeoForge.EVENT_BUS.addListener((Consumer<AddServerReloadListenersEvent>) event -> {
      event.addListener(MoreBowsAndArrows.identifier(Constants.MOD_ID), new ResourceReloadListener());
    });

    if (FMLLoader.getCurrent().getDist() == Dist.CLIENT) {
      new MoreBowsAndArrowsClientNeoForge(EVENT_BUS);
    }

    NeoForge.EVENT_BUS.addListener((Consumer<EntityTickEvent.Pre>) event -> {
      if (!(event.getEntity() instanceof ItemEntity itemEntity)) return;
      if (!itemEntity.getItem().has(ModDataComponents.BOW_LIMB_TYPE)) return;
      if (!(event.getEntity().level() instanceof ServerLevel level)) return;

      List<Entity> entities = level.getEntities(event.getEntity(), event.getEntity().getBoundingBox().inflate(1.0D));
      if (entities.isEmpty()) return;

      Entity entity = entities.getFirst();
      if (!(entity instanceof ItemEntity otherItemEntity)) return;
      if (!otherItemEntity.getItem().has(ModDataComponents.BOW_STRING_TYPE)) return;

      ResourceKey<BowPartDefinition> limbKey   = itemEntity.getItem().get(ModDataComponents.BOW_LIMB_TYPE);
      ResourceKey<BowPartDefinition> stringKey = otherItemEntity.getItem().get(ModDataComponents.BOW_STRING_TYPE);

      int limbDur = level.registryAccess().lookup(ModRegistries.BOW_LIMB_TYPE_KEY)
          .flatMap(reg -> reg.get(limbKey)).map(h -> h.value().addedDurability()).orElse(0);
      int stringDur = level.registryAccess().lookup(ModRegistries.BOW_STRING_TYPE_KEY)
          .flatMap(reg -> reg.get(stringKey)).map(h -> h.value().addedDurability()).orElse(0);

      ItemStack stack = new ItemStack(ModItems.STRUNG_BOW);
      stack.set(ModDataComponents.BOW_LIMB_TYPE,   limbKey);
      stack.set(ModDataComponents.BOW_STRING_TYPE,  stringKey);
      // Keep default riser/rest — already set on STRUNG_BOW item via Properties.component(...)
      stack.set(DataComponents.MAX_DAMAGE, stack.get(DataComponents.MAX_DAMAGE) + limbDur + stringDur);

      event.getEntity().discard();
      entity.discard();
      Containers.dropItemStack(level, entity.position().x, entity.position().y, entity.position().z, stack);
    });
  }

  public <T> void bind(ResourceKey<Registry<T>> registryKey, Consumer<BiConsumer<T, Identifier>> source) {
    EVENT_BUS.addListener((Consumer<RegisterEvent>) event -> {
      if (registryKey.equals(event.getRegistryKey())) {
        source.accept((t, rl) -> event.register(registryKey, rl, () -> t));
      }
    });
  }

  public static class ResourceReloadListener extends SimplePreparableReloadListener<Void> {

    @Override
    public String getName() {
      return MoreBowsAndArrows.identifier(Constants.MOD_ID).toString();
    }

    @Override
    protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      // ModConfig.load(Services.PLATFORM.getConfigDirectory());
    }

    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      return null;
    }
  }
}
