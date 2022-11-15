package com.recette;

import java.util.Scanner;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        displayMenu();
     /*   crudDAO<Recipe> recipeDao = DaoFactory.getRecipeDao();
        recipeDao.findAll();
        System.out.println(recipeDao.findAll());
        System.out.println("Hello world!");*/
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
                           System.out.println(recipe);
                           System.out.println("--------------");
                       }
                   });
                    break;
                case 2 :
                    System.out.println("S'inscrire");
                    break;
                case 3 :
                   /* Recipe recipeToCreate = getInfoRecipe();*/
                    System.out.println("Se connecter");
                    displayMenuConnected();
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
            System.out.println("1 - Editer son profil \n" + "2 - Se déconnecter \n" + "3 - Visualiser les recettes \n" + "4 - Rechercher par critère \n" + "5 - Ajouter une recette \n" + "6 - Récupérer une recette aléatoirement \n" + "7 - Quitter" );
            choice = sc.nextInt();
            switch (choice){
                case 1 :
                    System.out.println(" Editer son profil");
                    break;
                case 2 :
                    System.out.println("Se déconnecter");
                    break;
                case 3 :
                    System.out.println(recipeDao.findAll());
                    break;
                case 4 :
                    System.out.println("Rechercher par critère");
                    break;
                case 5 :
                    System.out.println("Ajouter une recette");
                    break;
                case 6 :
                    System.out.println("Récupérer une recette aléatoirement");
                    break;
                case 7 :
                    System.out.println("Quiter");
                    break;

            }
        }
    }
}