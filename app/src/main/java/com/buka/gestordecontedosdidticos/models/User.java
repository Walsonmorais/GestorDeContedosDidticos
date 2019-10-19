package com.buka.gestordecontedosdidticos.models;

public class User {

    String id;
    String username;
    String department;


    public User() {
    }

    public User(String id, String username, String department) {
        this.id = id;
        this.username = username;
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
