package com.rcreddy.byjusandroidassignment.Room;

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

    @Query("Update News set stuFirstName = :stuName where id = :id")
    void updateNews(String stuName, int id);

    @Query("Delete from News")
    void deleteNews();

}
