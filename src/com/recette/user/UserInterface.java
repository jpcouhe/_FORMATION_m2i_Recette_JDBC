package com.recette.user;

import java.sql.SQLException;

public interface UserInterface<E> {
    void login(String email, String password);

    void signup(String firstName, String lastName, String email, String password) throws SQLException;


}
