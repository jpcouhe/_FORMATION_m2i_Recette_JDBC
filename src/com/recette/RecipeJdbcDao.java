package com.recette;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class RecipeJdbcDao implements CrudDAO<Recipe> {
    @Override
    public List<Recipe> findAll() {
        List<Recipe> recipesList = new ArrayList<>();
        String query = "SELECT `Id_recipe`, recipes.name, description, categories.name AS categories_name FROM `recipes` JOIN categories ON recipes.Id_category = categories.Id_category";
        try (Statement st = ConnectionManager.getConnectionInstance().createStatement()) {
            ResultSet resultSet = st.executeQuery(query);
            while (resultSet.next()) {

                HashMap ingredientList = getIngredientByRecipe(resultSet.getInt("Id_recipe"));

                Recipe recipe = new Recipe(
                        resultSet.getLong("Id_recipe"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("categories_name"),
                        ingredientList
                );
                recipesList.add(recipe);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return recipesList;
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Recipe create(Recipe element) {
        return null;
    }

    public HashMap getIngredientByRecipe(int id){

        String query = "SELECT ingredients.name, recipes_ingredients.quantity FROM recipes_ingredients JOIN ingredients ON recipes_ingredients.Id_ingredients = ingredients.Id_ingredients WHERE  recipes_ingredients.Id_recipe = ?;";
        HashMap ingredientList = new HashMap<>();
        try(PreparedStatement pst = ConnectionManager.getConnectionInstance().prepareStatement(query)){
            pst.setInt(1, id);
            ResultSet resultSet = pst.executeQuery();
            while(resultSet.next()){
                ingredientList.put(resultSet.getString("name"), resultSet.getInt("quantity"));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return  ingredientList;
    }
}
