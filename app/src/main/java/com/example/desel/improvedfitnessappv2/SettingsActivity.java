package com.example.desel.improvedfitnessappv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SettingsActivity extends AppCompatActivity
{
    // Log for debugging
    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        Log.d(TAG, "onCreate: Settings opened");
    }

    public void onClick(View view)
    {
        Log.d(TAG, "onClick: Opening metric activity...");
        // Will try open the metric page
        Intent intent = new Intent(SettingsActivity.this,
                MetricActivity.class);
        startActivity(intent);
    }
}