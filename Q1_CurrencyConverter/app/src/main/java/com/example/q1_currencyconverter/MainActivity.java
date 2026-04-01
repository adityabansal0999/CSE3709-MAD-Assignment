package com.example.q1_currencyconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * MainActivity:
 * Handles user input and currency conversion logic.
 */
public class MainActivity extends AppCompatActivity {

    EditText amount;
    Spinner fromCurrency, toCurrency;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        loadTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amount = findViewById(R.id.amount);
        fromCurrency = findViewById(R.id.fromCurrency);
        toCurrency = findViewById(R.id.toCurrency);
        result = findViewById(R.id.result);

        Button convertBtn = findViewById(R.id.convertBtn);
        Button settingsBtn = findViewById(R.id.settingsBtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.currency_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromCurrency.setAdapter(adapter);
        toCurrency.setAdapter(adapter);

        convertBtn.setOnClickListener(v -> convert());

        settingsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void convert() {

        String input = amount.getText().toString();

        if (input.isEmpty()) {
            Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amt = Double.parseDouble(input);

        String from = fromCurrency.getSelectedItem().toString();
        String to = toCurrency.getSelectedItem().toString();

        double inr = convertToINR(amt, from);
        double finalValue = convertFromINR(inr, to);

        result.setText("Converted: " + String.format("%.2f", finalValue));
    }

    private double convertToINR(double amt, String from) {
        switch (from) {
            case "USD": return amt * 83;
            case "EUR": return amt * 90;
            case "JPY": return amt * 0.55;
            default: return amt;
        }
    }

    private double convertFromINR(double amt, String to) {
        switch (to) {
            case "USD": return amt / 83;
            case "EUR": return amt / 90;
            case "JPY": return amt / 0.55;
            default: return amt;
        }
    }

    private void loadTheme() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean dark = prefs.getBoolean("dark", false);

        AppCompatDelegate.setDefaultNightMode(
                dark ? AppCompatDelegate.MODE_NIGHT_YES :
                        AppCompatDelegate.MODE_NIGHT_NO);
    }
}