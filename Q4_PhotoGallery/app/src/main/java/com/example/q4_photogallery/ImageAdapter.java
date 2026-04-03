package com.example.q4_photogallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    ArrayList<ImageItem> imageList;
    Context context;

    public ImageAdapter(ArrayList<ImageItem> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_item);
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageItem item = imageList.get(position);

        // load image from file path into ImageView
        holder.imgItem.setImageBitmap(BitmapFactory.decodeFile(item.getPath()));

        // on click open ImageDetailsActivity
        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDetailsActivity.class);
                intent.putExtra("img_name", item.getName());
                intent.putExtra("img_path", item.getPath());
                intent.putExtra("img_size", item.getSize());
                intent.putExtra("img_date", item.getDateModified());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}