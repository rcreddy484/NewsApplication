package com.rcreddy.byjusandroidassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.SourcesRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.kwabenaberko.newsapilib.models.response.SourcesResponse;
import com.rcreddy.byjusandroidassignment.Room.MyDatabase;
import com.rcreddy.byjusandroidassignment.Room.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyDatabase myDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.headlines_list_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_SHORT).show();
            connected = true;
        } else{
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            connected = false;
        }

        setUpDB();

        if (connected){
            newsApi();
        }else {
            List<News> stuData =  myDatabase.dao().getNews();

            for (int i =0; i<stuData.size(); i++){
                Log.d("NEWS DATA" , String.valueOf(stuData.get(i).getStuId() +": "+
                        stuData.get(i).getName()+": "+ stuData.get(i).getPublishedAt()+": "+
                        stuData.get(i).getTitle()));
            }

            NewsHeadLinesAdapter adapter = new NewsHeadLinesAdapter(getApplicationContext(), stuData );
            recyclerView.setAdapter(adapter);
        }

    }

    private void setUpDB(){

        myDatabase = Room.databaseBuilder(MainActivity.this , MyDatabase.class , "NewsDB")
                .allowMainThreadQueries().build();
    }

    void newsApi(){
        NewsApiClient newsApiClient = new NewsApiClient("f289f26634e34d5ea11bcbc70f1cdb02");

        // /v2/everything
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        System.out.println(response.getArticles().get(0).getTitle());
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );

        // /v2/top-headlines
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .q("")
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        System.out.println(response.getArticles());
                        List<Article> articlesList = response.getArticles();

                        /*List<News> deleteNewsData =  myDatabase.dao().getNews();
                        for (News NewsData : deleteNewsData){
                            myDatabase.dao().deleteNews();
                        }*/
                        myDatabase.dao().deleteNews();
                        List<News> insertNewsDatatst =  myDatabase.dao().getNews();
                        for (int i =0; i<insertNewsDatatst.size(); i++){

                            Log.d("STUDENT_DATA" , String.valueOf(insertNewsDatatst.get(i).getStuId() +": "+
                                    insertNewsDatatst.get(i).getName()+": "+ insertNewsDatatst.get(i).getPublishedAt()+": "+
                                    insertNewsDatatst.get(i).getTitle()));
                        }


                        News news = null;
                        for (Article article : articlesList){
                            news = new News(article.getSource().getName(),article.getTitle(),
                                    article.getUrlToImage(), article.getPublishedAt(),
                                    article.getDescription());
                            myDatabase.dao().newsInsertion(news);
                        }

                        List<News> insertNewsData =  myDatabase.dao().getNews();
                        /*for (int i =0; i<insertNewsData.size(); i++){

                            Log.d("STUDENT_DATA" , String.valueOf(insertNewsData.get(i).getStuId() +": "+
                                    insertNewsData.get(i).getName()+": "+ insertNewsData.get(i).getPublishedAt()+": "+
                                    insertNewsData.get(i).getTitle()));

                        }*/

                        NewsHeadLinesAdapter adapter = new NewsHeadLinesAdapter(getApplicationContext(), insertNewsData );
                        recyclerView.setAdapter(adapter);
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
                        System.out.println(response.getSources().get(0).getName());
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );
    }

}