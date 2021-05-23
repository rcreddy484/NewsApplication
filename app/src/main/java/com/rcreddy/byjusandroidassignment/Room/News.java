package com.rcreddy.byjusandroidassignment.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class News {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String title;
    private String urlToImage;
    private String publishedAt;
    private String description;

    public News(String name, String title, String urlToImage, String publishedAt, String description) {
        this.name = name;
        this.title = title;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public int getStuId() {
        return id;
    }

    public void setStuId(int stuId) {
        this.id = stuId;
    }

}
