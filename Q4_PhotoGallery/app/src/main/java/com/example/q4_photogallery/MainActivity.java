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
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editFolderName;
    TextView txtFolderPath;
    Button btnCreateFolder, btnChooseFolder, btnTakePhoto, btnViewGallery;

    File chosenFolder;
    File currentPhotoFile;

    private static final int CAMERA_PERMISSION_CODE = 100;

    ActivityResultLauncher<Uri> cameraLauncher;
    Uri photoUri;

    // folder picker launcher
    ActivityResultLauncher<Uri> folderPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editFolderName = findViewById(R.id.edit_folder_name);
        txtFolderPath = findViewById(R.id.txt_folder_path);
        btnCreateFolder = findViewById(R.id.btn_create_folder);
        btnChooseFolder = findViewById(R.id.btn_choose_folder);
        btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnViewGallery = findViewById(R.id.btn_view_gallery);

        // camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        Toast.makeText(this, "Photo saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Photo cancelled", Toast.LENGTH_SHORT).show();
                        if (currentPhotoFile != null && currentPhotoFile.exists()) {
                            currentPhotoFile.delete();
                        }
                    }
                }
        );

        // folder picker launcher using document tree
        folderPicker = registerForActivityResult(
                new ActivityResultContracts.OpenDocumentTree(),
                uri -> {
                    if (uri != null) {
                        // get folder path from URI
                        DocumentFile pickedDir = DocumentFile.fromTreeUri(this, uri);
                        if (pickedDir != null) {
                            String folderName = pickedDir.getName();
                            // create a local copy folder with same name
                            chosenFolder = new File(getExternalFilesDir(null), folderName);
                            if (!chosenFolder.exists()) {
                                chosenFolder.mkdirs();
                            }
                            txtFolderPath.setText("Folder: " + chosenFolder.getAbsolutePath());
                            Toast.makeText(this, "Folder selected: " + folderName,
                                    Toast.LENGTH_SHORT).show();
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

        // choose existing folder button
        btnChooseFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folderPicker.launch(null);
            }
        });

        // take photo button
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenFolder == null) {
                    Toast.makeText(MainActivity.this,
                            "Select or create a folder first", Toast.LENGTH_SHORT).show();
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
                            "Select or create a folder first", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                intent.putExtra("folder_path", chosenFolder.getAbsolutePath());
                startActivity(intent);
            }
        });
    }

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

    private void takePhoto() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String fileName = "IMG_" + timeStamp + ".jpg";

        currentPhotoFile = new File(chosenFolder, fileName);

        photoUri = FileProvider.getUriForFile(this,
                getApplicationContext().getPackageName() + ".fileprovider",
                currentPhotoFile);

        cameraLauncher.launch(photoUri);
    }
}