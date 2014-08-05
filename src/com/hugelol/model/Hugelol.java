package com.hugelol.model;

import java.io.Serializable;
import java.util.Calendar;

public class Hugelol implements Serializable{  
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private Calendar date;
    private Integer userID;
    private String userName;
    private Integer type;
    private String url;
    private byte[] image;
    private String title;
    private Integer likes;
    private Integer dislikes;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Calendar getDate() {
        return date;
    }
    public void setDate(Calendar date) {
        this.date = date;
    }
    public Integer getUserID() {
        return userID;
    }
    public void setUserID(Integer userID) {
        this.userID = userID;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getLikes() {
        return likes;
    }
    public void setLikes(Integer likes) {
        this.likes = likes;
    }
    public Integer getDislikes() {
        return dislikes;
    }
    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

}
