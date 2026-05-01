package io.github.jason13official.more_bows_and_arrows;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class MBADataGen implements DataGeneratorEntrypoint {

  @Override
  public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
    pack.addProvider(ExampleModItemTagProvider::new);
  }

  public static class ExampleModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public static final TagKey<Item> SMELLY_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "smelly_items"));

    public ExampleModItemTagProvider(FabricPackOutput output, CompletableFuture<Provider> registriesFuture) {
      super(output, registriesFuture);


    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {

      valueLookupBuilder(SMELLY_ITEMS).add(Items.SLIME_BALL);
    }
  }
}
