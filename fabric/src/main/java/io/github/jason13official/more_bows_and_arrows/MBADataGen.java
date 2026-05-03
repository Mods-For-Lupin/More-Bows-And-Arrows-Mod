package io.github.jason13official.more_bows_and_arrows;

import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowPartDefinition;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStatEffect;
import io.github.jason13official.more_bows_and_arrows.impl.common.component.bow.BowStatOperation;
import io.github.jason13official.more_bows_and_arrows.impl.common.registry.ModRegistries;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class MBADataGen implements DataGeneratorEntrypoint {

  @Override
  public void onInitializeDataGenerator(FabricDataGenerator generator) {
    FabricDataGenerator.Pack pack = generator.createPack();
    pack.addProvider(BowLimbProvider::new);
    pack.addProvider(BowStringProvider::new);
    pack.addProvider(BowRiserProvider::new);
    pack.addProvider(BowRestProvider::new);
  }

  // ---- shared base --------------------------------------------------------

  abstract static class BowPartProvider extends FabricCodecDataProvider<BowPartDefinition> {

    BowPartProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future,
                    ResourceKey<? extends Registry<BowPartDefinition>> key) {
      super(output, future, PackOutput.Target.DATA_PACK,
          key.identifier().getNamespace() + "/" + key.identifier().getPath(),
          BowPartDefinition.DIRECT_CODEC);
    }

    @Override
    protected final void configure(BiConsumer<Identifier, BowPartDefinition> out,
                                   HolderLookup.Provider lookup) {
      addEntries(out);
    }

    protected abstract void addEntries(BiConsumer<Identifier, BowPartDefinition> out);

    static Identifier id(String name) {
      return MoreBowsAndArrows.identifier(name);
    }

    static BowPartDefinition part(String descKey, int color, int durability,
                                  BowStatEffect... effects) {
      return new BowPartDefinition(
          Component.translatable(descKey),
          List.of(effects),
          durability,
          color);
    }

    static BowStatEffect set(String stat, float value) {
      return new BowStatEffect(MoreBowsAndArrows.identifier(stat), BowStatOperation.SET, value);
    }

    static BowStatEffect add(String stat, float value) {
      return new BowStatEffect(MoreBowsAndArrows.identifier(stat), BowStatOperation.ADD, value);
    }

    static BowStatEffect mul(String stat, float value) {
      return new BowStatEffect(MoreBowsAndArrows.identifier(stat), BowStatOperation.MULTIPLY, value);
    }
  }

  // ---- limb types ---------------------------------------------------------

  public static class BowLimbProvider extends BowPartProvider {
    public BowLimbProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
      super(output, future, ModRegistries.BOW_LIMB_TYPE_KEY);
    }

    @Override
    protected void addEntries(BiConsumer<Identifier, BowPartDefinition> out) {
      // wood — balanced baseline
      out.accept(id("default"), part("bow_limb_type.more_bows_and_arrows.default", 0xFF0000, 50,
          set("draw_speed", 1.0f), set("velocity", 1.0f), add("crit_chance", 0.0f)));

      // bone — lighter; faster draw, trades velocity and durability for crit
      out.accept(id("bone"), part("bow_limb_type.more_bows_and_arrows.bone", 0xF0EAD2, 35,
          set("draw_speed", 1.3f), set("velocity", 0.9f), add("crit_chance", 0.05f)));

      // bamboo — very fast draw, low velocity; great for rapid fire
      out.accept(id("bamboo"), part("bow_limb_type.more_bows_and_arrows.bamboo", 0x8B9F35, 25,
          set("draw_speed", 1.6f), set("velocity", 0.75f), add("crit_chance", 0.0f)));

      // iron — heavy and powerful; high velocity and damage at the cost of draw speed
      out.accept(id("iron"), part("bow_limb_type.more_bows_and_arrows.iron", 0xAAAAAA, 120,
          set("draw_speed", 0.7f), set("velocity", 1.5f),
          add("damage", 0.25f), add("crit_chance", 0.0f)));

      // obsidian — extreme power; maximum velocity and damage, very slow to draw
      out.accept(id("obsidian"), part("bow_limb_type.more_bows_and_arrows.obsidian", 0x1F0A2E, 200,
          set("draw_speed", 0.45f), set("velocity", 2.0f),
          add("damage", 0.5f), add("crit_chance", 0.15f)));
    }

    @Override
    public String getName() { return "More Bows - Bow Limb Types"; }
  }

  // ---- string types -------------------------------------------------------

  public static class BowStringProvider extends BowPartProvider {
    public BowStringProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
      super(output, future, ModRegistries.BOW_STRING_TYPE_KEY);
    }

    @Override
    protected void addEntries(BiConsumer<Identifier, BowPartDefinition> out) {
      // plant fiber — neutral multipliers; balanced baseline
      out.accept(id("default"), part("bow_string_type.more_bows_and_arrows.default", 0x00FF00, 50,
          mul("velocity", 1.0f), mul("draw_speed", 1.0f), set("accuracy", 1.0f)));

      // sinew — transfers more power but strains draw; higher velocity, slower pull
      out.accept(id("sinew"), part("bow_string_type.more_bows_and_arrows.sinew", 0xC89060, 40,
          mul("velocity", 1.2f), mul("draw_speed", 0.88f), set("accuracy", 0.9f)));

      // spider silk — harvested from cave spiders; fast draw, precise, adds stealth
      out.accept(id("spider_silk"), part("bow_string_type.more_bows_and_arrows.spider_silk", 0x2A1A1A, 30,
          mul("velocity", 0.92f), mul("draw_speed", 1.35f),
          set("accuracy", 1.2f), add("stealth", 0.15f)));

      // iron wire — rigid wire string; maximum velocity but stiff and inaccurate
      out.accept(id("iron_wire"), part("bow_string_type.more_bows_and_arrows.iron_wire", 0x909090, 80,
          mul("velocity", 1.4f), mul("draw_speed", 0.75f), set("accuracy", 0.8f)));
    }

    @Override
    public String getName() { return "More Bows - Bow String Types"; }
  }

  // ---- riser types --------------------------------------------------------

  public static class BowRiserProvider extends BowPartProvider {
    public BowRiserProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
      super(output, future, ModRegistries.BOW_RISER_TYPE_KEY);
    }

    @Override
    protected void addEntries(BiConsumer<Identifier, BowPartDefinition> out) {
      // wood — standard riser; moderate accuracy, slight draw penalty
      out.accept(id("default"), part("bow_riser_type.more_bows_and_arrows.default", 0x8B6A13, 75,
          add("accuracy", 0.2f), mul("draw_speed", 0.95f)));

      // bone — lightweight; minimal draw penalty at the cost of some accuracy
      out.accept(id("bone"), part("bow_riser_type.more_bows_and_arrows.bone", 0xF0EAD2, 60,
          add("accuracy", 0.15f), mul("draw_speed", 0.98f)));

      // iron — heavy precision riser; high accuracy and slight damage, draw penalty
      out.accept(id("iron"), part("bow_riser_type.more_bows_and_arrows.iron", 0x8C8C8C, 110,
          add("accuracy", 0.35f), mul("draw_speed", 0.87f), add("damage", 0.1f)));

      // gemstone — inlaid gem riser; best accuracy and crit bonus, moderate draw cost
      out.accept(id("gemstone"), part("bow_riser_type.more_bows_and_arrows.gemstone", 0x4466DD, 90,
          add("accuracy", 0.45f), mul("draw_speed", 0.92f), add("crit_chance", 0.1f)));
    }

    @Override
    public String getName() { return "More Bows - Bow Riser Types"; }
  }

  // ---- rest types ---------------------------------------------------------

  public static class BowRestProvider extends BowPartProvider {
    public BowRestProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
      super(output, future, ModRegistries.BOW_REST_TYPE_KEY);
    }

    @Override
    protected void addEntries(BiConsumer<Identifier, BowPartDefinition> out) {
      // simple — basic plastic or wood rest; small accuracy and stealth bonus
      out.accept(id("default"), part("bow_rest_type.more_bows_and_arrows.default", 0xA69666, 25,
          add("accuracy", 0.1f), add("stealth", 0.1f)));

      // feather — soft feather rest; trades accuracy for substantial stealth
      out.accept(id("feather"), part("bow_rest_type.more_bows_and_arrows.feather", 0xF5F5E8, 15,
          add("accuracy", 0.05f), add("stealth", 0.3f)));

      // bone — rigid bone rest; more accurate, no stealth benefit
      out.accept(id("bone"), part("bow_rest_type.more_bows_and_arrows.bone", 0xE8E0C8, 20,
          add("accuracy", 0.2f)));

      // crystal — precision crystal rest; highest accuracy with a small crit bonus
      out.accept(id("crystal"), part("bow_rest_type.more_bows_and_arrows.crystal", 0x77BBFF, 20,
          add("accuracy", 0.25f), add("crit_chance", 0.05f)));
    }

    @Override
    public String getName() { return "More Bows - Bow Rest Types"; }
  }
}
