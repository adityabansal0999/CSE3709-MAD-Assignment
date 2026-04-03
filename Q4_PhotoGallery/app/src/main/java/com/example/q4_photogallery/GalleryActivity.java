package com.example.q4_photogallery;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    RecyclerView recyclerGallery;
    TextView txtNoImages, txtGalleryTitle;
    ArrayList<ImageItem> imageList;
    ImageAdapter imageAdapter;
    String folderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        recyclerGallery = findViewById(R.id.recycler_gallery);
        txtNoImages = findViewById(R.id.txt_no_images);
        txtGalleryTitle = findViewById(R.id.txt_gallery_title);

        // get folder path from intent
        folderPath = getIntent().getStringExtra("folder_path");

        txtGalleryTitle.setText("Gallery");

        // use GridLayoutManager with 2 columns
        recyclerGallery.setLayoutManager(new GridLayoutManager(this, 2));

        // load images
        loadImages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // reload images when coming back from details (after delete)
        loadImages();
    }

    // reads all image files from the folder and shows in grid
    private void loadImages() {
        imageList = new ArrayList<>();
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    // only add image files
                    if (file.isFile() && isImageFile(file.getName())) {
                        ImageItem item = new ImageItem(
                                file.getName(),
                                file.getAbsolutePath(),
                                file.length(),
                                file.lastModified()
                        );
                        imageList.add(item);
                    }
                }
            }
        }

        // show or hide "no images" message
        if (imageList.isEmpty()) {
            txtNoImages.setVisibility(View.VISIBLE);
            recyclerGallery.setVisibility(View.GONE);
        } else {
            txtNoImages.setVisibility(View.GONE);
            recyclerGallery.setVisibility(View.VISIBLE);
        }

        imageAdapter = new ImageAdapter(imageList, this);
        recyclerGallery.setAdapter(imageAdapter);
    }

    // checks if file is an image based on extension
    private boolean isImageFile(String name) {
        String lower = name.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") ||
                lower.endsWith(".png") || lower.endsWith(".bmp");
    }
}