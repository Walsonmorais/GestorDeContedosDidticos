package com.buka.gestordecontedosdidticos.models;

public class Upload {

    private String subject;
    private String theme;
    private String course;
    private String year;
    private String filesUrl;

    public Upload() {
    }

    public Upload(String subject, String theme, String course, String year, String filesUrl) {
        this.subject = subject;
        this.theme = theme;
        this.course = course;
        this.year = year;
        this.filesUrl = filesUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFilesUrl() {
        return filesUrl;
    }

    public void setFilesUrl(String filesUrl) {
        this.filesUrl = filesUrl;
    }
}