package com.example.q4_photogallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

        imgPreview = findViewById(R.id.img_preview);
        txtName = findViewById(R.id.txt_img_name);
        txtPath = findViewById(R.id.txt_img_path);
        txtSize = findViewById(R.id.txt_img_size);
        txtDate = findViewById(R.id.txt_img_date);
        btnDelete = findViewById(R.id.btn_delete);

        // get data from intent
        Intent intent = getIntent();
        imgName = intent.getStringExtra("img_name");
        imgPath = intent.getStringExtra("img_path");
        imgSize = intent.getLongExtra("img_size", 0);
        imgDate = intent.getLongExtra("img_date", 0);

        // show image preview
        imgPreview.setImageBitmap(BitmapFactory.decodeFile(imgPath));

        // show image details
        txtName.setText("Name: " + imgName);
        txtPath.setText("Path: " + imgPath);

        // convert size to KB
        double sizeKB = imgSize / 1024.0;
        txtSize.setText("Size: " + String.format(Locale.getDefault(), "%.2f KB", sizeKB));

        // convert date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                Locale.getDefault());
        String dateStr = sdf.format(new Date(imgDate));
        txtDate.setText("Date: " + dateStr);

        // delete button with confirmation dialog
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
    }

    // shows alert dialog asking user to confirm deletion
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

    // deletes the image file and goes back to gallery
    private void deleteImage() {
        File file = new File(imgPath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                Toast.makeText(this, "Image deleted",
                        Toast.LENGTH_SHORT).show();
                // go back to gallery
                finish();
            } else {
                Toast.makeText(this, "Failed to delete image",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}