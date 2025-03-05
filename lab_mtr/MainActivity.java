package com.example.lab_mtr;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView txtData;
    private Button btn;
    private Button btnNext;

    private SensorManager mSensorManager;
    private Sensor sensorGr;

    private float [] gyroData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.butGet);
        txtData = findViewById(R.id.txtData);
        btnNext = findViewById(R.id.butNext);

        txtData.setTextColor(Color.rgb(138,10,185));

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorGr = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

//        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
//        StringBuilder strBuilder = new StringBuilder();
//        for(Sensor s: sensorList){
//            strBuilder.append(s.getName()+"\n");
//        }
//        txtView.setText(strBuilder);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorGr != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("X = ").append(String.format("%.2f", gyroData[0])).append("\n");
                    sb.append("Y = ").append(String.format("%.2f", gyroData[1])).append("\n");
                    sb.append("Z = ").append(String.format("%.2f", gyroData[2]));
                    txtData.setText(sb);
                } else {
                    txtData.setText("Датчик не доступен");
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorGr != null) {
            mSensorManager.registerListener(this, sensorGr, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroData = event.values.clone();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}