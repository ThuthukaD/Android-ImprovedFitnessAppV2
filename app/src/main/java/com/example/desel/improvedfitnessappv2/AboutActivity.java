package com.example.desel.improvedfitnessappv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AboutActivity extends AppCompatActivity
{
    // VARIABLES
    // Debugging
    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Log.d(TAG, "onCreate: About activity launched");
    }
}
