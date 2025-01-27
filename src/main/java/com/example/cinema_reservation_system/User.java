package com.example.cinema_reservation_system;

abstract class User implements Printable {
    protected String name;
    protected String email;
    protected String username;
    private String password; // In real applications, store this securely (hashed)
    private int id;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(String name, String email,  String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public User(String name, int id) {
        this.name = name;
        this.id = id;
    }
    public User(String name) {
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public abstract void login();
    public abstract void logout();
    @Override
    public void printDetails() {
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
    }
}