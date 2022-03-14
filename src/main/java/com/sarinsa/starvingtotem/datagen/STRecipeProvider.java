package com.sarinsa.starvingtotem.datagen;

import com.sarinsa.starvingtotem.common.core.registry.STItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class STRecipeProvider extends RecipeProvider {

    public STRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(STItems.FAMILY_ALTAR.get())
                .pattern("T")
                .pattern("S")
                .define('T', Items.TOTEM_OF_UNDYING)
                .define('S', Items.SMOOTH_STONE_SLAB)
                .unlockedBy("has_totem_of_undying", has(Items.TOTEM_OF_UNDYING))
                .unlockedBy("has_smooth_stone_slab", has(Items.SMOOTH_STONE_SLAB))
                .save(consumer);
    }
}
