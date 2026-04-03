package com.example.q3_sensordata;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;

    private Sensor accelerometerSensor;
    private Sensor lightSensor;
    private Sensor proximitySensor;

    private TextView tvAccX, tvAccY, tvAccZ;
    private TextView tvLight;
    private TextView tvProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize views

        tvAccX = findViewById(R.id.tv_acc_x);
        tvAccY = findViewById(R.id.tv_acc_y);
        tvAccZ = findViewById(R.id.tv_acc_z);
        tvLight = findViewById(R.id.tv_light);
        tvProximity = findViewById(R.id.tv_proximity);
        // Get SensorManager

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Get sensors

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        // Show message if any sensor is not available

        if (accelerometerSensor == null) {
            Toast.makeText(this, "Accelerometer not available", Toast.LENGTH_SHORT).show();
        }
        if (lightSensor == null) {
            Toast.makeText(this, "Light sensor not available", Toast.LENGTH_SHORT).show();
        }
        if (proximitySensor == null) {
            Toast.makeText(this, "Proximity sensor not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            tvAccX.setText("X: " + String.format("%.2f", event.values[0]));
            tvAccY.setText("Y: " + String.format("%.2f", event.values[1]));
            tvAccZ.setText("Z: " + String.format("%.2f", event.values[2]));
        }

        else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            tvLight.setText("Light: " + String.format("%.2f", event.values[0]) + " lux");
        }

        else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float proximityValue = event.values[0];

            if (proximityValue < proximitySensor.getMaximumRange()) {
                tvProximity.setText("Proximity: " + proximityValue + " (Near)");
            } else {
                tvProximity.setText("Proximity: " + proximityValue + " (Far)");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used here
    }
}