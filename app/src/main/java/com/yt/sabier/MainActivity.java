package com.yt.sabier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SabierView sabierView;

    List<Point> points;

    ChartView chartView;
    List<ChartView.Point> chartPoints;
    String tipText = "3 hours/day on average";
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        chartPoints = new ArrayList<>();

        chartView = findViewById(R.id.cv_chart);
        chartView.init(30);

//        sabierView = findViewById(R.id.AnnotationView);
//
//        points = new ArrayList<>();
//
//        points.add(new Point(new Random().nextDouble(), new Random().nextDouble()));
//        points.add(new Point(new Random().nextDouble(), new Random().nextDouble()));
//        points.add(new Point(new Random().nextDouble(), new Random().nextDouble()));
        Message m = new Message();
        handler.sendMessageDelayed(m, 500);
    }

    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

//            points.add(new Point(new Random().nextDouble(), new Random().nextDouble()));
//            int size = points.size();
//            sabierView.next(points.get(size - 3), points.get(size - 2), points.get(size - 1));
            float f = (float) (Math.random() * 1);
            chartView.add(f);
            Message m = new Message();
            handler.sendMessageDelayed(m, 500);
        }
    };
}