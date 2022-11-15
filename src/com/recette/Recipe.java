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

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public HashMap getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(HashMap recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    @Override
    public String toString() {
        return "Recette numéro: " + recipeId + " nom : " + recipeName + " description : " + description + " Catégorie : " + categoryName + " Ingrédients : " + recipeIngredients ;
    }
}
