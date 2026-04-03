package com.example.q4_photogallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editFolderName;
    TextView txtFolderPath;
    Button btnCreateFolder, btnTakePhoto, btnViewGallery;

    File chosenFolder;
    File currentPhotoFile;

    private static final int CAMERA_PERMISSION_CODE = 100;

    // launcher for camera
    ActivityResultLauncher<Uri> cameraLauncher;
    Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editFolderName = findViewById(R.id.edit_folder_name);
        txtFolderPath = findViewById(R.id.txt_folder_path);
        btnCreateFolder = findViewById(R.id.btn_create_folder);
        btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnViewGallery = findViewById(R.id.btn_view_gallery);

        // setup camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        Toast.makeText(this, "Photo saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Photo cancelled", Toast.LENGTH_SHORT).show();
                        // delete empty file if photo was cancelled
                        if (currentPhotoFile != null && currentPhotoFile.exists()) {
                            currentPhotoFile.delete();
                        }
                    }
                }
        );

        // create folder button
        btnCreateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFolder();
            }
        });

        // take photo button
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenFolder == null) {
                    Toast.makeText(MainActivity.this,
                            "Create a folder first", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkCameraPermissionAndTakePhoto();
            }
        });

        // view gallery button
        btnViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenFolder == null) {
                    Toast.makeText(MainActivity.this,
                            "Create a folder first", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                intent.putExtra("folder_path", chosenFolder.getAbsolutePath());
                startActivity(intent);
            }
        });
    }

    // creates a folder in app's external files directory
    private void createFolder() {
        String folderName = editFolderName.getText().toString().trim();
        if (folderName.isEmpty()) {
            Toast.makeText(this, "Enter a folder name", Toast.LENGTH_SHORT).show();
            return;
        }

        chosenFolder = new File(getExternalFilesDir(null), folderName);
        if (!chosenFolder.exists()) {
            boolean created = chosenFolder.mkdirs();
            if (created) {
                Toast.makeText(this, "Folder created!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to create folder", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "Folder already exists", Toast.LENGTH_SHORT).show();
        }
        txtFolderPath.setText("Folder: " + chosenFolder.getAbsolutePath());
    }

    // checks camera permission and takes photo
    private void checkCameraPermissionAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        } else {
            takePhoto();
        }
    }

    // handles permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, "Camera permission denied",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // opens camera and saves photo to chosen folder
    private void takePhoto() {
        // create image file with timestamp name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String fileName = "IMG_" + timeStamp + ".jpg";

        currentPhotoFile = new File(chosenFolder, fileName);

        // get URI using FileProvider
        photoUri = FileProvider.getUriForFile(this,
                getApplicationContext().getPackageName() + ".fileprovider",
                currentPhotoFile);

        // launch camera
        cameraLauncher.launch(photoUri);
    }
}