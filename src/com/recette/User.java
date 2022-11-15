package com.recette;

public class User implements UserInterface<User>{
    Long id;
    String lastName;
    String firstName;
    String email;
    boolean isAuth;


    private User(){}
    @Override
    public void login(String email, String password) {
        // Select where email And password
        // Resulset
        // this.id = resulset.getId("id")
        // ......
    }

    @Override
    public User signup(String firstName, String lastName, String email, String password) {
        return null;
    }
}
