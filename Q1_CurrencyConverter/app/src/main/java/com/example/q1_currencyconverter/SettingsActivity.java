package com.example.q1_currencyconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch themeSwitch = findViewById(R.id.themeSwitch);

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean dark = prefs.getBoolean("dark", false);

        themeSwitch.setChecked(dark);

        themeSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
            prefs.edit().putBoolean("dark", isChecked).apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES :
                            AppCompatDelegate.MODE_NIGHT_NO);
        });
    }
}
