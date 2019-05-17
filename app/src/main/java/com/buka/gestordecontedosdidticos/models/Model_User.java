package com.buka.gestordecontedosdidticos.models;

public class Model_User {

    String id;
    String username;
    String teacher_occupations;
    String student_occupations;
    String image;

    public Model_User() {
    }

    public Model_User(String id, String username, String teacher_occupations, String student_occupations, String image) {
        this.id = id;
        this.username = username;
        this.teacher_occupations = teacher_occupations;
        this.student_occupations = student_occupations;
        this.image = image;
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

    public String getTeacher_occupations() {
        return teacher_occupations;
    }

    public void setTeacher_occupations(String teacher_occupations) {
        this.teacher_occupations = teacher_occupations;
    }

    public String getStudent_occupations() {
        return student_occupations;
    }

    public void setStudent_occupations(String student_occupations) {
        this.student_occupations = student_occupations;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
