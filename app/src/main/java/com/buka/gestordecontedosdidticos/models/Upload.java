package com.buka.gestordecontedosdidticos.models;

public class Upload {

    private String Subject;
    private String Theme;
    private String Course;
    private String Year;
    private String ImageUrl;

    public Upload() {

    }

    public Upload(String subject, String theme, String course, String year, String imageUrl) {

        Subject = subject;
        Theme = theme;
        Course = course;
        Year = year;
        ImageUrl = imageUrl;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;

    }

    public String getTheme() {
        return Theme;
    }

    public void setTheme(String theme) {
        this.Theme = theme;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
