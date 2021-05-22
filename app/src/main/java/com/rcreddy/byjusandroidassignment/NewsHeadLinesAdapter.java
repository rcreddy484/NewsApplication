package com.rcreddy.byjusandroidassignment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kwabenaberko.newsapilib.models.Article;
import com.rcreddy.byjusandroidassignment.Room.News;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsHeadLinesAdapter extends RecyclerView.Adapter<NewsHeadLinesAdapter.MyAdapter> {

    Context mContext;
    List<News> articlesList;

    public NewsHeadLinesAdapter(Context mContext, List<News> articlesList) {
        this.mContext = mContext;
        this.articlesList = articlesList;
    }

    @NonNull
    @Override
    public NewsHeadLinesAdapter.MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.headlines_list_items,parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHeadLinesAdapter.MyAdapter holder, final int position) {


        holder.news_title_tv.setText(articlesList.get(position).getTitle());
        holder.news_channel_name_tv.setText(articlesList.get(position).getName());

        String dateStr = articlesList.get(position).getPublishedAt();
        String[] dateArray = dateStr.split("T");

        String date = "";
        for (int i = 0; i< dateArray.length ; i++) {
            date = dateArray[0];
            break;
        }
        holder.published_date_tv.setText(date);

        Picasso.with(mContext)
                .load(articlesList.get(position).getUrlToImage())
                .placeholder(R.drawable.noimagefound)
                .into( holder.news_background_image_ll);

        holder.news_background_image_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                News article = articlesList.get(position);
                Gson gson = new Gson();
                String myJson = gson.toJson(article);
                Intent intent = new Intent(mContext, NewDescriptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Article", myJson);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    @Override
    public long getItemId(int position) {
        return getItemCount();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        ImageView news_background_image_ll;
        TextView news_title_tv, news_channel_name_tv, published_date_tv;
        public MyAdapter(@NonNull View itemView) {
            super(itemView);

            news_title_tv = itemView.findViewById(R.id.news_title_tv);
            news_channel_name_tv = itemView.findViewById(R.id.news_channel_name_tv);
            published_date_tv = itemView.findViewById(R.id.published_date_tv);
            news_background_image_ll = itemView.findViewById(R.id.news_background_image_ll);
        }
    }
}
