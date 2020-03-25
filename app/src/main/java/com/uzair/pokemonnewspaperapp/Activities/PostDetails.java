package com.uzair.pokemonnewspaperapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uzair.pokemonnewspaperapp.Adapters.AdapterForHorizontallRecycler;
import com.uzair.pokemonnewspaperapp.Models.MyNewsPaperPosts;
import com.uzair.pokemonnewspaperapp.R;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class PostDetails extends AppCompatActivity {

    private RecyclerView relatedPostList;
    private List<String> postsList;
    private LinearLayoutManager layoutManager;
    private AdapterForHorizontallRecycler adapterForHorizontallRecycler;
    private TextView title , description;
    private ImageView postImage , videoView , playButton;
    private Intent detailsIntent;
    private String imageUrl ,  postTitle , postDescription , videoUrl;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        initViews();

        setImageAndText();




    }

    private void initViews()
    {

        mToolbar = findViewById(R.id.postDetailsToolbar);
        setSupportActionBar(mToolbar);

        detailsIntent = getIntent();

        videoView = findViewById(R.id.videoView);
        playButton = findViewById(R.id.imageView);


        title = findViewById(R.id.postTitleInDetails);
        description = findViewById(R.id.descriptionOfPost);

        postImage = findViewById(R.id.postImageInDetails);


    }

    private void inVisiableImages(String postImageUrl)
    {
        playButton.setVisibility(View.INVISIBLE);
        playButton.getLayoutParams().height = 0;

        videoView.setVisibility(View.VISIBLE);
        videoView.getLayoutParams().height = 350;
        Glide.with(this)
                .load(postImageUrl)
                .placeholder(R.drawable.giphyloading)
                .into(videoView);
    }

    private void setImageAndText()
    {

        try {

            postsList = detailsIntent.getStringArrayListExtra("imageList");

            imageUrl = detailsIntent.getStringExtra("imageUrl");
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.giphyloading)
                    .into(postImage);


            postTitle = detailsIntent.getStringExtra("title");
            title.setText(postTitle);


            postDescription = detailsIntent.getStringExtra("desc");
            description.setText(postDescription);


            videoUrl = detailsIntent.getStringExtra("videoLink");
            if (videoUrl != null && !videoUrl.isEmpty() && imageUrl != null) {

                videoView.getLayoutParams().height = 350;
                playButton.getLayoutParams().height = 100;
                playButton.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.giphyloading)
                        .into(videoView);

                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(videoUrl)));
                    }
                });

            } else {
                inVisiableImages(imageUrl);
                setRecyclerView();
            }


        }
        catch (Exception ex)
        {
            Log.d("errorMessage", "onCreate: "+ex.getMessage());
        }



    }


    private void setRecyclerView()
    {

        relatedPostList = findViewById(R.id.relatedPostList);
        layoutManager = new LinearLayoutManager(PostDetails.this , LinearLayoutManager.HORIZONTAL , false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        relatedPostList.setLayoutManager(layoutManager);

        postsList = new ArrayList<>();
        postsList = detailsIntent.getStringArrayListExtra("imageList");

        if(postsList.isEmpty())
        {
            postsList.add(imageUrl);
        }

        Log.d("listOfImages", "setRecyclerView: "+postsList);
        adapterForHorizontallRecycler = new AdapterForHorizontallRecycler(postsList , this);

        relatedPostList.setAdapter(adapterForHorizontallRecycler);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playButton.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.INVISIBLE);
        videoView.getLayoutParams().height = 0;
        playButton.getLayoutParams().height = 0;

    }

}
