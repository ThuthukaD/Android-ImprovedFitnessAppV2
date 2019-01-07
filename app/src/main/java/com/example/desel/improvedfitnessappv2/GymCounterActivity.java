package com.example.desel.improvedfitnessappv2;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;

public class GymCounterActivity extends AppCompatActivity
{
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_counter);

        chronometer = findViewById(R.id.chronometer);
        // Adds a new custom text format
        chronometer.setFormat("Time: %s");
        // Makes the format stick from the start
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

    public void startChronometer(View v)
    {
        if (!running){
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(View v)
    {
        if (running){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer(View v)
    {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }
}
