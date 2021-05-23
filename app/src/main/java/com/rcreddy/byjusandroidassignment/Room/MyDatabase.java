package com.rcreddy.byjusandroidassignment.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities =  {News.class} , version = 2 , exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract DAO dao();
}
