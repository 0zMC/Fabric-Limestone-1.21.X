package com.ozzi.limestonemod.datagen;

import com.ozzi.limestonemod.LimestoneMod;
import com.ozzi.limestonemod.block.ModBlocks;
import com.ozzi.limestonemod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {

        offerReversibleCompactingRecipes(exporter, RecipeCategory.REDSTONE, ModItems.LIMESTONE, RecipeCategory.REDSTONE, ModBlocks.LIMESTONE_BLOCK);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModItems.LIMESTONE, 1)
                .input(Items.REDSTONE).input(Items.SLIME_BALL)
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .offerTo(exporter, Identifier.of(LimestoneMod.MOD_ID, "limestone_from_slime"));

    }
}
