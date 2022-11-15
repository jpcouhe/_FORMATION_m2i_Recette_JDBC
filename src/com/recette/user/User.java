package com.recette.user;

import com.recette.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User implements UserInterface<User> {
    static int id;
    String lastName;
    String firstName;
    String email;
    boolean isAuth = false;


    public User(){}
    @Override
    public void login(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?;";
        try(PreparedStatement pst = ConnectionManager.getConnectionInstance().prepareStatement(query);){
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet resultSet = pst.executeQuery();
            if(resultSet.next()){
                this.id = resultSet.getInt("Id_users");
                this.lastName = resultSet.getString("lastName");
                this.email = resultSet.getString("email");
                this.firstName = resultSet.getString("firstName");
                this.isAuth = true;
            }else{
                System.out.println("Utilisateur non trouvé");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Select where email And password
        // Resulset
        // this.id = resulset.getId("id")
        // ......
    }

    @Override
    public void signup(String firstName, String lastName, String email, String password) throws SQLException {
        String query = "INSERT INTO `users` (`lastName`, `email`, `firstName`, `password`) VALUES (?,?,?,?)";
        try(PreparedStatement pst = ConnectionManager.getConnectionInstance().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);){
            pst.setString(1, email);
            pst.setString(2, password);
            pst.setString(3, email);
            pst.setString(4, password);
            pst.executeUpdate();
            ConnectionManager.getConnectionInstance().commit();
        } catch (SQLException e) {
            ConnectionManager.getConnectionInstance().rollback();
            throw new RuntimeException(e);
        }
    }

    public static int getId() {
        return id;
    }

    public boolean isAuth() {
        return isAuth;
    }
}
