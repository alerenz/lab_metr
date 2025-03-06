package com.example.lab_mtr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class GraphicActivity extends AppCompatActivity {

    private ArrayList<float []> gyroDataList;
    private ArrayList<Coordinates> coordinates;
    private GraphView graph;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_graphic);

        graph = findViewById(R.id.graph1);
        btnNext = findViewById(R.id.butNext2);

        gyroDataList = (ArrayList<float[]>) getIntent().getSerializableExtra("gyData");
        coordinates = new ArrayList<>();
        for(int i = 0; i < gyroDataList.size(); i++){
            float [] data = gyroDataList.get(i);
            coordinates.add(new Coordinates(data[0], data[1], data[2]));
        }

        drawGraphs();

        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphicActivity.this, Graphic2Activity.class);
                intent.putExtra("gyData", gyroDataList);
                startActivity(intent);
            }
        });

    }

    private void drawGraphs() {
        LineGraphSeries<DataPoint> probabilitySeries = new LineGraphSeries<>();

        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;

        for (Coordinates coord : coordinates) {
            minX = Math.min(minX, coord.getX());
            maxX = Math.max(maxX, coord.getX());
        }

        int numberOfBins = 50;
        float binWidth = (maxX - minX) / numberOfBins;
        double[] bins = new double[numberOfBins];

        for (Coordinates coord : coordinates) {
            int binIndex = (int)((coord.getX() - minX) / binWidth);
            if (binIndex >= 0 && binIndex < numberOfBins) {
                bins[binIndex]++;
            }
        }

        double totalPoints = coordinates.size();
        for (int i = 0; i < numberOfBins; i++) {
            double probability = bins[i] / totalPoints;
            float xValue = minX + (i * binWidth) + (binWidth / 2);

            probabilitySeries.appendData(new DataPoint(xValue, probability), false, numberOfBins);
        }

        probabilitySeries.setColor(Color.BLUE);
        probabilitySeries.setTitle("Значение вероятностей");

        graph.removeAllSeries();
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(minX);
        graph.getViewport().setMaxX(maxX);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(0.5);

        graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(true);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Значение X");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Вероятность");

        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLACK);
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLACK);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graph.addSeries(probabilitySeries);
    }
}

