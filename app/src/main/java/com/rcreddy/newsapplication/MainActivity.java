package com.rcreddy.newsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.widget.Toast;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.SourcesRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.kwabenaberko.newsapilib.models.response.SourcesResponse;
import com.rcreddy.newsapplication.Room.MyDatabase;
import com.rcreddy.newsapplication.Room.News;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.headlines_list_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        setUpDB();

        if (isInternetConnected()){
            //if internet connected making a service call
            newsApi();
        }else {
            //if internet not connected Read data from database.
            List<News> NewsArticlesList =  myDatabase.dao().getNews();

            //setting up adapter to recyclerview.
            NewsHeadLinesAdapter adapter = new NewsHeadLinesAdapter(getApplicationContext(), NewsArticlesList );
            recyclerView.setAdapter(adapter);
        }

    }

    private boolean isInternetConnected(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else{
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            connected = false;
        }
        return connected;
    }

    private void setUpDB(){
        myDatabase = Room.databaseBuilder(MainActivity.this , MyDatabase.class , "NewsDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations().build();
    }

    void newsApi(){
        NewsApiClient newsApiClient = new NewsApiClient("f289f26634e34d5ea11bcbc70f1cdb02");

        // /v2/top-headlines
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .q("india")
                        .language("en")
                        .pageSize(35)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {

                        List<Article> articlesList = response.getArticles();

                        //on success response deleting Existing data from database
                        myDatabase.dao().deleteNews();

                        News news = null;
                        for (Article article : articlesList){
                            news = new News(article.getSource().getName(),article.getTitle(),
                                    article.getUrlToImage(), article.getPublishedAt(),
                                    article.getDescription());

                            //Inserting data into database
                            myDatabase.dao().newsInsertion(news);
                        }

                        //Read data from database.
                        List<News> NewsArticlesList =  myDatabase.dao().getNews();

                        //setting up adapter to recyclerview.
                        NewsHeadLinesAdapter adapter = new NewsHeadLinesAdapter(getApplicationContext(), NewsArticlesList );
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );

        // /v2/everything
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        /*System.out.println(response.getArticles().get(0).getTitle());*/
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                  }
        );

        // /v2/sources
        newsApiClient.getSources(
                new SourcesRequest.Builder()
                        .language("en")
                        .country("us")
                        .build(),
                new NewsApiClient.SourcesCallback() {
                    @Override
                    public void onSuccess(SourcesResponse response) {
                        /*System.out.println(response.getSources().get(0).getName());*/
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        /*System.out.println(throwable.getMessage());*/
                    }
                }
        );
    }

}