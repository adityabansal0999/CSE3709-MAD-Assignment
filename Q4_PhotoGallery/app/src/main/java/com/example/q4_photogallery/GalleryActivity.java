package com.example.q4_photogallery;

import android.os.Bundle;
import android.view.MenuItem;
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

        // enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gallery");
        }

        recyclerGallery = findViewById(R.id.recycler_gallery);
        txtNoImages = findViewById(R.id.txt_no_images);
        txtGalleryTitle = findViewById(R.id.txt_gallery_title);

        folderPath = getIntent().getStringExtra("folder_path");

        // use GridLayoutManager with 2 columns for gallery look
        recyclerGallery.setLayoutManager(new GridLayoutManager(this, 2));

        loadImages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImages();
    }

    private void loadImages() {
        imageList = new ArrayList<>();
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
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

    private boolean isImageFile(String name) {
        String lower = name.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") ||
                lower.endsWith(".png") || lower.endsWith(".bmp");
    }

    // handle back button press in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}