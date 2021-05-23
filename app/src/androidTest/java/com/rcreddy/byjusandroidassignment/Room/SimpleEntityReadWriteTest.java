package com.rcreddy.byjusandroidassignment.Room;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private List<News> userDao;
    private MyDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MyDatabase.class).build();
        userDao = db.dao().getNews();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        News news = new News("NDTV", "HEADLINES", "", "123456","Testing News Headlines");
        db.dao().newsInsertion(news);
        List<News> news1 = db.dao().getNews();
        assertThat("",news1.get(0).getName(), is(equalTo(news.getName())));

    }

    @Test
    public void deleteTable(){
        db.dao().deleteNews();
        assertThat("",db.dao().getNews().size(), is(equalTo(0)));
    }
}