package com.recette;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
private static Connection CONNECTION_INSTANCE;
private ConnectionManager(){}

    public static Connection getConnectionInstance() {
        if(CONNECTION_INSTANCE == null){
            try{
                loadDriver();
                CONNECTION_INSTANCE = DriverManager.getConnection("jdbc:mysql://localhost:3306/cooking_app?serverTimezone=UTC", "root", "root");
                CONNECTION_INSTANCE.setAutoCommit(false);
                System.out.println("Connection OK");
            }catch(SQLException e) {
                System.err.println("Connection impossible");
            }
        }
        return CONNECTION_INSTANCE;
    }

    private static void loadDriver(){
        try {
            DriverManager.registerDriver(new Driver());
            System.out.println("Connection OK");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Driver MySQL introuvable");
        }
    }


    public static void closeConnection(){
        try {
            CONNECTION_INSTANCE.close();
        } catch (Exception e) {
            System.err.println("Fermeture dela connexion impossible");
        }

    }
}
