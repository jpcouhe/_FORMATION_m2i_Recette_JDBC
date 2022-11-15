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
    public Optional<Recipe> findById(int id) {
        String query = "SELECT `Id_recipe`, recipes.name, description, categories.name AS categories_name FROM `recipes` JOIN categories ON recipes.Id_category = categories.Id_category WHERE id = ?";
        try(PreparedStatement pst = ConnectionManager.getConnectionInstance().prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {

                HashMap ingredientList = getIngredientByRecipe(resultSet.getInt("Id_recipe"));

                Recipe recipe = new Recipe(
                        resultSet.getLong("Id_recipe"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("categories_name"),
                        ingredientList
                );

                return Optional.of(recipe);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Recipe> findByKeyword(String keyword) {
        List<Recipe> recipesList = new ArrayList<>();
        String query = "SELECT `Id_recipe`, recipes.name, description, categories.name AS categories_name FROM `recipes` JOIN categories ON recipes.Id_category = categories.Id_category WHERE `description` LIKE '%' ? '%' OR recipes.name LIKE '%' ? '%'";
        try (PreparedStatement pst = ConnectionManager.getConnectionInstance().prepareStatement(query)) {
            pst.setString(1, keyword);
            pst.setString(2, keyword);
            ResultSet resultSet = pst.executeQuery();
            while(resultSet.next()){
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
    public Recipe create(Recipe element) throws SQLException {
        String query = "INSERT INTO `recipes` (`name`, `description`, `Id_category`) VALUES (?,?,?);";
        Recipe recipe = null;
        try(PreparedStatement pst = ConnectionManager.getConnectionInstance().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            pst.setString(1, element.getRecipeName());
            pst.setString(2, element.getDescription());
            pst.setInt(3, 2);
            Long autoIncreKey = -1L;
            pst.executeUpdate();
            ResultSet key = pst.getGeneratedKeys();

            if(key.next()){
                autoIncreKey = key.getLong(1);
            }


            String getAllingredients = "SELECT * FROM `ingredients`";

            try(Statement st = ConnectionManager.getConnectionInstance().createStatement()){
                ResultSet resultSet = st.executeQuery(getAllingredients);
                while(resultSet.next()){
                    int idOfIngredient =0;

                    String name = resultSet.getString("name");




                    if(element.getRecipeIngredients().containsKey(resultSet.getString("name"))){
                        idOfIngredient = resultSet.getInt("Id_ingredients");
                        /*keyOfIngredient = resultSet.getString("name");*/

                    }else{
                        System.out.println("Nous sommes ici");
                       /* idOfIngredient = 1;*/
                    }

                    String setTableRecipeAndIngredients = "INSERT INTO `recipes_ingredients` (`Id_recipe`, `Id_ingredients`, `quantity`) VALUES (?, ?, ?);";
                    try(PreparedStatement pst1 = ConnectionManager.getConnectionInstance().prepareStatement(setTableRecipeAndIngredients)){
                        pst1.setLong(1,autoIncreKey );
                        pst1.setLong(2,idOfIngredient);
                        pst1.setInt(3, 3);
                        pst1.executeUpdate();
                    }catch (SQLException e){
                        throw new RuntimeException(e);
                    }
                }

            }catch (SQLException e){
                throw new RuntimeException(e);
            }


            recipe = new Recipe(
                autoIncreKey,
                element.getRecipeName(),
                element.getDescription(),
                null,
                element.getRecipeIngredients()
            );




            ConnectionManager.getConnectionInstance().commit();
        }catch (SQLException e){
            ConnectionManager.getConnectionInstance().rollback();
            throw new RuntimeException(e);
        }
        return recipe;
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
