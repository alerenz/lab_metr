package com.example.lab_mtr;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorGr;
    private EditText txtEd;
    private TextView txtData;
    private Button btn;
    private Button btnNext;

    private float [] gyroData;
    private ArrayList<float []> gyroDataList = new ArrayList<>();
    private boolean isMeasuring = false;
    private int count = 0;
    private long lastTime;
    private float interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        txtEd = findViewById(R.id.timeEd);
        txtData = findViewById(R.id.txtData1);
        btn = findViewById(R.id.butGet1);
        btnNext = findViewById(R.id.butNext1);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorGr = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMeasurement();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, GraphicActivity.class);
                intent.putExtra("gyData", gyroDataList);
                startActivity(intent);
            }
        });

        sensorManager.registerListener(this, sensorGr, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void startMeasurement() {
        gyroDataList.clear();
        if (!isMeasuring && !txtEd.getText().toString().isEmpty()) {
            interval = Long.parseLong(txtEd.getText().toString()) * 1000;
            count = 0;
            lastTime = System.currentTimeMillis();
            isMeasuring = true;

            txtData.setText("Начало измерений...");
        } else {
            txtData.setText("Введите интервал или завершите текущие измерения");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isMeasuring) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastTime) >= interval) {
                gyroData = event.values.clone();
                count++;
                lastTime = currentTime;

                String result = String.format("%d: X: %.2f Y: %.2f Z: %.2f",
                        count, gyroData[0], gyroData[1], gyroData[2]
                );

                txtData.append("\n" + result);
                gyroDataList.add(gyroData);
                if (count >= 10) {
                    finishMeasurements();
                }
            }
        }
    }

    private void finishMeasurements() {
        isMeasuring = false;
        txtData.append("\nЗавершено!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}