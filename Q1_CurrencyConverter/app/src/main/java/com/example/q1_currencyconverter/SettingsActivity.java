package com.example.q1_currencyconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * SettingsActivity:
 * This activity allows the user to switch between Light and Dark themes.
 * It uses SharedPreferences to save user preference.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set title of this screen
        setTitle(R.string.settings);

        // Enable back button in Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize theme switch
        Switch themeSwitch = findViewById(R.id.themeSwitch);

        // Access SharedPreferences
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);

        // Get saved theme value (default = false → Light Mode)
        boolean isDarkMode = prefs.getBoolean("dark", false);

        // Set switch state
        themeSwitch.setChecked(isDarkMode);

        // Listener for switch toggle
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            // Save preference
            prefs.edit().putBoolean("dark", isChecked).apply();

            // Apply theme dynamically
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    /**
     * Handles Action Bar back button click
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close SettingsActivity and go back
        return true;
    }
}