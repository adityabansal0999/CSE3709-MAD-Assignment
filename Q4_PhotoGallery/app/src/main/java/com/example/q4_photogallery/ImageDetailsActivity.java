package com.example.q4_photogallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageDetailsActivity extends AppCompatActivity {

    ImageView imgPreview;
    TextView txtName, txtPath, txtSize, txtDate;
    Button btnDelete;

    String imgName, imgPath;
    long imgSize, imgDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        // enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Image Details");
        }

        imgPreview = findViewById(R.id.img_preview);
        txtName = findViewById(R.id.txt_img_name);
        txtPath = findViewById(R.id.txt_img_path);
        txtSize = findViewById(R.id.txt_img_size);
        txtDate = findViewById(R.id.txt_img_date);
        btnDelete = findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        imgName = intent.getStringExtra("img_name");
        imgPath = intent.getStringExtra("img_path");
        imgSize = intent.getLongExtra("img_size", 0);
        imgDate = intent.getLongExtra("img_date", 0);

        // show image
        imgPreview.setImageBitmap(BitmapFactory.decodeFile(imgPath));

        // show details
        txtName.setText("Name: " + imgName);
        txtPath.setText("Path: " + imgPath);

        double sizeKB = imgSize / 1024.0;
        txtSize.setText("Size: " + String.format(Locale.getDefault(), "%.2f KB", sizeKB));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                Locale.getDefault());
        String dateStr = sdf.format(new Date(imgDate));
        txtDate.setText("Date: " + dateStr);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
    }

    private void showDeleteDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ImageDetailsActivity.this);
        alert.setTitle(R.string.delete_title);
        alert.setMessage(R.string.delete_message);
        alert.setCancelable(false);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteImage();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void deleteImage() {
        File file = new File(imgPath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                Toast.makeText(this, "Image deleted",
                        Toast.LENGTH_SHORT).show();
                // goes back to gallery view
                finish();
            } else {
                Toast.makeText(this, "Failed to delete image",
                        Toast.LENGTH_SHORT).show();
            }
        }
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