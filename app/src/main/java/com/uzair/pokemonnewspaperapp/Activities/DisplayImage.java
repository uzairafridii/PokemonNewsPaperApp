package com.uzair.pokemonnewspaperapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.uzair.pokemonnewspaperapp.R;

public class DisplayImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_display_image);

        Intent inetent = getIntent();

        String image  =  inetent.getStringExtra("image");

        ImageView imageView = findViewById(R.id.displayImage);

        Glide.with(this)
                .load(image)
                .into(imageView);

    }
}
