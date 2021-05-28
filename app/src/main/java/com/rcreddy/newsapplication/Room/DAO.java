package com.rcreddy.newsapplication.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO {

    @Insert
    public void newsInsertion(News news);

    @Query("Select * from News")
    List<News> getNews();

    @Query("Update News set name = :name, title = :title, urlToImage = :urlToImage," +
            " publishedAt = :publishedAt, description = :description")
    void updateNews(String name, String title, String urlToImage, String publishedAt, String description);

    @Query("Delete from News")
    void deleteNews();

}
