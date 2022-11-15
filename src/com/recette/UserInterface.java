package com.recette;

public interface UserInterface<E> {
    void login(String email, String password);

    E signup(String firstName, String lastName, String email, String password);


}
