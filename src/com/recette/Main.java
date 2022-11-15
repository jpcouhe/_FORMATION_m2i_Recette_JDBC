package com.recette;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        displayMenu();
    }

    public static void displayMenu(){

        Scanner sc = new Scanner(System.in);
        int choice = 0;
        while (choice != 3){
            System.out.println("Bonjour, que voulez vous faire? \n" + "1 - Ne pas se connecter \n" + "2 - Se connecter \n" + "3 - Quitter");
            choice = sc.nextInt();
            switch (choice){
                case 1 :
                    displayMenuNotConnected();
                    break;
                case 2 :
                    displayMenuConnected();
                    break;
                case 3 :
                    System.out.println("Fermeture du programme");
                    break;

            }

        }
    }

    public static void displayMenuNotConnected(){
        CrudDAO<Recipe> recipeDao = DaoFactory.getRecipeDao();
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        while (choice != 4){
            System.out.println("1 - Visualiser les recettes \n" + "2 - S'inscrire \n" + "3 - Se connecter \n" + "4 - Quitter");
            choice = sc.nextInt();
            switch (choice){
                case 1 :

                    recipeDao.findAll().forEach(new Consumer<Recipe>() {
                       @Override
                       public void accept(Recipe recipe) {
                           System.out.println(recipe.toString());
                           System.out.println("--------------");
                       }
                   });
                    break;
                case 2 :
                    System.out.println("S'inscrire");
                    break;
                case 3 :
                    User user = new User();
                    user.login("jp.couhe@gmail.com", "1234");
                    if(user.isAuth()){
                        displayMenuConnected();
                    }else{
                        System.out.println("Connection impossible");
                    }
                    break;
                case 4 :
                    System.out.println("Quitter");
                    break;

            }
        }
    }

    public static void displayMenuConnected(){
        CrudDAO<Recipe> recipeDao = DaoFactory.getRecipeDao();
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        while (choice != 7){
            System.out.println("1 - Editer son profil \n" + "2 - Se déconnecter \n" + "3 - Visualiser les recettes \n" + "4 - Rechercher par mot-clé \n" + "5 - Ajouter une recette \n" + "6 - Récupérer une recette aléatoirement \n" + "7 - Quitter" );
            choice = sc.nextInt();
            switch (choice){
                case 1 :
                    System.out.println(" Editer son profil");
                    break;
                case 2 :
                    System.out.println("Se déconnecter");
                    break;
                case 3 :
                    recipeDao.findAll().forEach(recipe -> {
                        System.out.println(recipe.toString());
                        System.out.println("---------------");
                    });
                    break;
                case 4 :
                    System.out.println("Veuillez renseigner un mot clé");
                    String keyword = sc.next();
                    List<Recipe> listRecipe = recipeDao.findByKeyword(keyword);
                    if(listRecipe.size() > 0){
                        listRecipe.forEach(recipe -> System.out.println(recipe.toString()));
                    }else{
                        System.out.println("Aucune recette trouvée !");
                    }
                    break;
                case 5 :

                    HashMap ingredientsForNewRecipe = new HashMap();
                    ingredientsForNewRecipe.put("beurre", 10);
                    ingredientsForNewRecipe.put("steack", 10);
                    Recipe newRecipeToAdd = new Recipe(null, "Steack", "Cuire les Patates", "Plat", ingredientsForNewRecipe);
                    try {
                        newRecipeToAdd = recipeDao.create(newRecipeToAdd);
                        System.out.println(newRecipeToAdd);
                        System.out.println("-----------------------");
                    } catch (SQLException e) {
                        System.out.println("Impossible de creer la recette");
                        throw new RuntimeException(e);
                    }
                    break;
                case 6 :
                    System.out.println("Récupérer une recette aléatoirement");
                    break;
                case 7 :
                    System.out.println("Quitter");
                    break;

            }
        }
    }
}