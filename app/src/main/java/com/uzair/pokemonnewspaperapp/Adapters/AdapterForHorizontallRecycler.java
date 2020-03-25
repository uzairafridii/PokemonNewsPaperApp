package com.uzair.pokemonnewspaperapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uzair.pokemonnewspaperapp.Activities.DisplayImage;
import com.uzair.pokemonnewspaperapp.R;

import java.util.List;

public class AdapterForHorizontallRecycler extends RecyclerView.Adapter<AdapterForHorizontallRecycler.ViewHolder>
{

    private List<String> imageList;
    private Context context;

    public AdapterForHorizontallRecycler(List<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View myView = LayoutInflater.from(context).inflate(R.layout.design_for_images_recycler , null);
        ViewHolder holder = new ViewHolder(myView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final String result = imageList.get(position);

        holder.setImageView(result);
        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context , DisplayImage.class);
                intent.putExtra("image", result);
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;
        private ImageView imageView;
        private CardView mCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            mCard = mView.findViewById(R.id.displayImageCardView);
        }

        private void setImageView(String imageUrl)
        {
            imageView = mView.findViewById(R.id.horizontal_image);
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.giphyloading)
                    .into(imageView);
        }

    }
}
