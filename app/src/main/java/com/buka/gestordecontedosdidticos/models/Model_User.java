package com.buka.gestordecontedosdidticos.models;

public class Model_User {

    String id;
    String username;
    String departments;
    String image;


    public Model_User() {
    }

    public Model_User(String id, String username, String teacher_occupations, String departments, String image) {
        this.id = id;
        this.username = username;
        this.image = image;
        this.departments = departments;
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

    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


}
