package com.rcreddy.byjusandroidassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rcreddy.byjusandroidassignment.Room.News;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewDescriptionActivity extends AppCompatActivity {

    ImageView news_background_image_ll;
    TextView news_title_tv, news_channel_name_tv, published_date_tv, news_description_tv;

    CircleImageView back_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_description);

        initializeViews();

        //get Intent data
        String article =  getIntent().getStringExtra("Article");

        //converting JsonString to Gson
        News articleObj = new Gson().fromJson(article, News.class);

        news_channel_name_tv.setText(articleObj.getName());
        news_description_tv.setText(articleObj.getDescription());
        news_title_tv.setText(articleObj.getTitle());

        //Using Picasso library to load image
        Picasso.with(getApplicationContext())
                .load(articleObj.getUrlToImage())
                .placeholder(R.drawable.noimagefound)
                .into( news_background_image_ll);

        String publishedDateStr = articleObj.getPublishedAt();
        String[] dateArray = publishedDateStr.split("T");
        String publishedDate = "";
        for (int i = 0; i< dateArray.length ; i++) {
            publishedDate = dateArray[0];
            break;
        }
        published_date_tv.setText(publishedDate);

    }

    private void initializeViews() {
        news_background_image_ll = findViewById(R.id.news_background_image_ll);
        news_title_tv = findViewById(R.id.news_title_tv);
        news_channel_name_tv = findViewById(R.id.news_channel_name_tv);
        published_date_tv = findViewById(R.id.published_date_tv);
        news_description_tv = findViewById(R.id.news_description_tv);
        back_image = findViewById(R.id.back_image);

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}