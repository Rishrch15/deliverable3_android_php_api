package com.example.deliverable3_android_php_api.model;

public class user {
    private int id;
    private String name;
    private String email;
    private String password;

    public user(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

}