package com.buka.gestordecontedosdidticos.models;

public class Post {
    String postId;
    String postImage;
    String postSubject;
    String postDescription;


    public Post(String postid, String postimage, String postsubject, String postdescription) {
        this.postId = postid;
        this.postImage = postimage;
        this.postSubject = postsubject;
        this.postDescription = postdescription;
    }

    public Post() {

    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostSubject() {
        return postSubject;
    }

    public void setPostSubject(String postSubject) {
        this.postSubject = postSubject;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }
}
