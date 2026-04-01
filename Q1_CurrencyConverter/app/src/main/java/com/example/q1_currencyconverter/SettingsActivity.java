package com.example.q1_currencyconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * SettingsActivity:
 * Allows user to toggle between Light and Dark theme.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Enable back button in Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize switch
        Switch themeSwitch = findViewById(R.id.themeSwitch);

        // Access saved preferences
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark", false);

        // Set switch state based on saved value
        themeSwitch.setChecked(isDarkMode);

        // Listener for toggle
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            // Save preference
            prefs.edit().putBoolean("dark", isChecked).apply();

            // Apply theme
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
        });
    }

    /**
     * Handles back button press in Action Bar
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Go back to MainActivity
        return true;
    }
}