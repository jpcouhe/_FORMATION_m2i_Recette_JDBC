package com.recette;

import java.util.HashMap;
import java.util.List;

public class Recipe {
    public Long recipeId;
    public String recipeName;
    public String description;
    public String categoryName;
    public HashMap recipeIngredients;


    public Recipe(Long recipeId, String recipeName, String description, String categoryName, HashMap recipeIngredients) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.description = description;
        this.categoryName = categoryName;
        this.recipeIngredients = recipeIngredients;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getDescription() {
        return description;
    }

    public HashMap getRecipeIngredients() {
        return recipeIngredients;
    }

    @Override
    public String toString() {
        return "Recette numéro: " + recipeId + "\n"
                + " nom : " + recipeName + "\n"
                + " description : " + description + "\n"
                + " Catégorie : " + categoryName + "\n"
                + " Ingrédients : " + recipeIngredients ;
    }
}
