package com.example.lab_mtr;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class FourthActivity extends AppCompatActivity {

    private TextView math;
    private TextView sigma;
    private ArrayList<float []> gyroDataList;

    private float [] mathExpectations;
    private double [] standardDeviation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fourth);

        math = findViewById(R.id.math);
        sigma = findViewById(R.id.sigma);

        gyroDataList = (ArrayList<float[]>) getIntent().getSerializableExtra("gyData");

        mathExpectations = getMathExpectations();
        standardDeviation = getStandardDeviation();

        String resultMath = String.format("X = %.2f\nY = %.2f\nZ = %.2f",
                mathExpectations[0], mathExpectations[1], mathExpectations[2]);

        String resultSigma = String.format("X = %.2f\nY = %.2f\nZ = %.2f",
                standardDeviation[0], standardDeviation[1], standardDeviation[2]);

        math.setTextColor(Color.rgb(10, 115, 185));
        sigma.setTextColor(Color.rgb(138,10,185));

        math.setText(resultMath);
        sigma.setText(resultSigma);

    }

    private float[] getMathExpectations(){
        mathExpectations = new float[3];
        for(int i = 0; i < 3; i++){
            float sum = 0;
            for(int j = 0; j < gyroDataList.size(); j++){
                float [] data = gyroDataList.get(j);
                sum += data[i];
            }
            mathExpectations[i] = sum/gyroDataList.size();
        }
        return mathExpectations;
    }

    private double[] getStandardDeviation(){
        standardDeviation = new double[3];

        for(int i = 0; i < 3; i++){
            float sum = 0;
            for(int j = 0; j < gyroDataList.size(); j++){
                float [] data = gyroDataList.get(j);
                sum += (data[i] - mathExpectations[i]) * (data[i] - mathExpectations[i]);
            }
            standardDeviation[i] = Math.sqrt((double) sum/gyroDataList.size());
        }
        return standardDeviation;
    }

}