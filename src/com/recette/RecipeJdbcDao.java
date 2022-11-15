package com.recette;

import com.recette.user.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

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


        //Clone liste ingrédient fourni par l'utilisateur
        HashMap ingredientsTemps = element.getRecipeIngredients();


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



            // Recupération de la liste de tout les ingrédients
            String getAllingredients = "SELECT * FROM `ingredients`";

            try(Statement st = ConnectionManager.getConnectionInstance().createStatement()){
                ResultSet resultSet = st.executeQuery(getAllingredients);
                while(resultSet.next()){
                    int idOfIngredient =0;
                    // Si ingrédient fourni par utilisateur déja dans notre table
                    if(element.getRecipeIngredients().containsKey(resultSet.getString("name"))){
                        idOfIngredient = resultSet.getInt("Id_ingredients");

                        String setTableRecipeAndIngredients = "INSERT INTO `recipes_ingredients` (`Id_recipe`, `Id_ingredients`, `quantity`) VALUES (?, ?, ?);";
                        try(PreparedStatement pst1 = ConnectionManager.getConnectionInstance().prepareStatement(setTableRecipeAndIngredients)){
                            pst1.setLong(1,autoIncreKey );
                            pst1.setLong(2,idOfIngredient);
                            pst1.setInt(3, (Integer) element.getRecipeIngredients().get(resultSet.getString("name")));
                            pst1.executeUpdate();
                        }catch (SQLException e){
                            throw new RuntimeException(e);
                        }

                        // Mise à jour du clone de la liste
                        ingredientsTemps.remove(resultSet.getString("name"));
                    }
                }

                // Si notre clone d'ingrédient contient encore des ingrédients, ils ne sont pas dans notre table. Alors on les crée.

                if(ingredientsTemps.size() > 0){
                    Iterator iterator = ingredientsTemps.entrySet().iterator();
                    while(iterator.hasNext()){
                        Map.Entry mapentry = (Map.Entry) iterator.next();
                        String saveIngredient = "INSERT INTO `ingredients` (`name`) VALUES (?)";
                        try(PreparedStatement pstSaveIngredient = ConnectionManager.getConnectionInstance().prepareStatement(saveIngredient, Statement.RETURN_GENERATED_KEYS)) {
                            pstSaveIngredient.setString(1, (String) mapentry.getKey());
                            Long idOfCreateIngredient = -1L;
                            pstSaveIngredient.executeUpdate();
                            ResultSet resultSetOfCreateIngredient = pstSaveIngredient.getGeneratedKeys();
                            if(resultSetOfCreateIngredient.next()){
                                idOfCreateIngredient = resultSetOfCreateIngredient.getLong(1);
                            }
                            String setTableRecipeAndIngredients = "INSERT INTO `recipes_ingredients` (`Id_recipe`, `Id_ingredients`, `quantity`) VALUES (?, ?, ?);";
                            try(PreparedStatement pstInsert = ConnectionManager.getConnectionInstance().prepareStatement(setTableRecipeAndIngredients)){
                                pstInsert.setLong(1,autoIncreKey );
                                pstInsert.setLong(2,idOfCreateIngredient);
                                pstInsert.setInt(3, (Integer) mapentry.getValue());
                                pstInsert.executeUpdate();
                            }catch (SQLException e){
                                throw new RuntimeException(e);
                            }
                        }catch (SQLException e){
                            throw new RuntimeException(e);
                        }
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

    @Override
    public void cookedAt(int id, LocalDate date) throws SQLException {
        String query = "INSERT INTO `users_recipes` (`Id_users`, `Id_recipe`, `cookedAt`) VALUES (?, ?, ?)";
        try(PreparedStatement pst = ConnectionManager.getConnectionInstance().prepareStatement(query)) {
            pst.setInt(1, User.getId());
            pst.setInt(2, id);
            pst.setDate(3, Date.valueOf(date));
            pst.executeUpdate();
            ConnectionManager.getConnectionInstance().commit();
        }catch (SQLException e){
            ConnectionManager.getConnectionInstance().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Recipe findRandomRecipe() {
        String query = "SELECT recipes.Id_recipe, recipes.name, recipes.description, categories.name AS categories_name FROM `recipes` JOIN categories ON recipes.Id_category = categories.Id_category LEFT JOIN users_recipes ON recipes.Id_recipe = users_recipes.Id_recipe WHERE users_recipes.Id_users IS NULL OR (users_recipes.Id_users = ? AND users_recipes.cookedAt >= ?) ORDER BY RAND() LIMIT 1;";
        Recipe recipe = null;
        try(PreparedStatement pst = ConnectionManager.getConnectionInstance().prepareStatement(query)) {
            pst.setInt(1,User.getId());
            pst.setDate(2, Date.valueOf(LocalDate.now().minusDays(6)));
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()){

                HashMap ingredientList = getIngredientByRecipe(resultSet.getInt("Id_recipe"));
                recipe = new Recipe(
                        resultSet.getLong("Id_recipe"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("categories_name"),
                        ingredientList
                );

            }
        } catch (SQLException e) {
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
