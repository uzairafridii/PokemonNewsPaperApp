package com.uzair.pokemonnewspaperapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uzair.pokemonnewspaperapp.Activities.PostDetails;
import com.uzair.pokemonnewspaperapp.Models.MyNewsPaperPosts;
import com.uzair.pokemonnewspaperapp.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class AdapterForPostRecyclerView  extends RecyclerView.Adapter<AdapterForPostRecyclerView.MyPostViewHolder>
{
    private Context context;
    private List<MyNewsPaperPosts> list;
    private List<String> images = new ArrayList<>();

    public AdapterForPostRecyclerView(Context context, List<MyNewsPaperPosts> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView = LayoutInflater.from(context).inflate(R.layout.desgin_for_newspaper_recycler , null);
        MyPostViewHolder holder = new MyPostViewHolder(myView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {

        images.clear();
      final MyNewsPaperPosts papers = list.get(position);
      holder.setTitle(papers.getTitle().getRendered());
      holder.setDate(papers.getDate());
      holder.setDescription(papers.getExcerpt().getRendered());
      holder.setPostImage(papers.getJetpackFeaturedMediaUrl());

      final Document document = Jsoup.parse(papers.getContent().getRendered());
      final Document doc = Jsoup.parse(papers.getContent().getRendered());
      final String result = document.text();

      holder.postCard.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              Element iframe = document.select("iframe").first();
              Elements elements = doc.select("img");


              for(int i=0; i<elements.size(); i++)
              {
                  String img = elements.get(i).attr("src");
                  images.add(img);
              }




              if(iframe != null)
              {
                  String iframeSrc = iframe.attr("src");
                  Log.d("linkYout", "onBindViewHolder: "+iframeSrc);


                  Intent dataIntent = new Intent(context , PostDetails.class);
                  dataIntent.putExtra("imageUrl", papers.getJetpackFeaturedMediaUrl());
                  dataIntent.putExtra("title", papers.getTitle().getRendered());
                  dataIntent.putExtra("desc" , result);
                  dataIntent.putExtra("videoLink" , iframeSrc);
                  context.startActivity(dataIntent);
              }
              else
              {
                  Intent dataIntent = new Intent(context , PostDetails.class);
                  dataIntent.putExtra("imageUrl", papers.getJetpackFeaturedMediaUrl());
                  dataIntent.putExtra("title", papers.getTitle().getRendered());
                  dataIntent.putExtra("desc" , result);
                  dataIntent.putStringArrayListExtra("imageList", (ArrayList<String>) images);
                  context.startActivity(dataIntent);
              }






          }
      });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyPostViewHolder extends RecyclerView.ViewHolder {

        private View myView;
        private TextView date , title , description;
        private ImageView postImage;
        private CardView postCard;


        public MyPostViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
            postCard = myView.findViewById(R.id.postCard);
        }

        private void setDate(String postDate)
        {
            date = myView.findViewById(R.id.dateOfPosting);
            date.setText(postDate);
        }

        private void setTitle(String postTitle)
        {
            title = myView.findViewById(R.id.titleOfNewsPaper);
            title.setText(postTitle);
        }

        private void setDescription(String postDescription)
        {
            description = myView.findViewById(R.id.expertOfNewPapers);
            Document document = Jsoup.parse(postDescription);
            String result = document.text();
            description.setText(result);
        }

        private void setPostImage(String imageUrl)
        {
            postImage = myView.findViewById(R.id.newsPaperImage);

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.giphyloading)
                    .into(postImage);
        }
    }
}
