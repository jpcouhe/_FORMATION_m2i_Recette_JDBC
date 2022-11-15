package com.recette;

import com.recette.user.User;

import java.sql.SQLException;
import java.time.LocalDate;
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
                   /* System.out.println("Quel est votre adresse mail?");
                    String email = readString();
                    System.out.println("Quel est votre mot de passe?");
                    String password = readString();*/

                    User user = new User();
                    user.login("jp.couhe@gmail.com", "1234");

                    /*user.login(email,password);*/
                    if(user.isAuth()){
                        displayMenuConnected();
                    }else{
                        System.out.println("Connection impossible");
                    }
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
                        /*  System.out.println("Quel est votre adresse mail?");
                    String email = readString();
                    System.out.println("Quel est votre mot de passe?");
                    String password = readString();*/

                    User user = new User();
                    /*   user.login(email,password);*/
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
            System.out.println("1 - Editer son profil \n" + "2 - Se déconnecter \n" + "3 - Visualiser les recettes \n" + "4 - Rechercher par mot-clé \n" + "5 - Ajouter une recette \n" + "6 - Récupérer une recette aléatoirement \n" + "7 - Cuisiner \n" + "8 - Quitter" );
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
                    System.out.println("Titre de la recette :");
                    String title = readString();
                    System.out.println("Description de la recette :");
                    String description = readString();
                    System.out.println("Quelle est la catégorie ?\n" + "1 - Entrée \n" + "2 - Plat \n" + "3 - Dessert");
                    int categorie = sc.nextInt();
                    String categorieName = "";
                    switch (categorie){
                        case 1 : categorieName = "entree";
                            break;
                        case 2 : categorieName = "plat";
                            break;
                        case 3 : categorieName = "dessert";
                            break;
                    }

                    System.out.println("Combien d'ingrédients?");
                    HashMap ingredientsForNewRecipe = new HashMap();
                    int nbOfIngredients = sc.nextInt();
                    while(nbOfIngredients > 0){
                        System.out.println("Quel est le nom de l'ingrédient ?");
                        String nameOfIngredient = readString();
                        System.out.println("Quelle est la quantité? ");
                        int quantityOfIngredient = sc.nextInt();
                        ingredientsForNewRecipe.put(nameOfIngredient, quantityOfIngredient);
                        nbOfIngredients--;
                    }

                    Recipe newRecipeToAdd = new Recipe(null, title, description, categorieName, ingredientsForNewRecipe);
                    try {
                        recipeDao.create(newRecipeToAdd);
                    } catch (SQLException e) {
                        System.out.println("Impossible de creer la recette");
                        throw new RuntimeException(e);
                    }
                    break;
                case 6 :
                    System.out.println(recipeDao.findRandomRecipe());
                    break;
                case 7 :
                    System.out.println("Quelle recette avez-vous cuisiné? ");
                    int idOfrecipe = sc.nextInt();
                    System.out.println("Quel date ? Format yyyy-mm-dd");
                    String cookedAt = readString();
                    LocalDate cookedAtDate = LocalDate.parse(cookedAt);

                    try {
                        recipeDao.cookedAt(idOfrecipe,cookedAtDate);
                    } catch (SQLException e) {
                        System.out.println("Action impossible");
                        throw new RuntimeException(e);
                    }
                    break;
                case 8 :
                    System.out.println("Quitter");
                    break;
            }
        }
    }

    public static String readString(){    Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}


