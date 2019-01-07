package com.example.desel.improvedfitnessappv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MetricActivity extends AppCompatActivity
{
    // VARIABLE
    // Debugging
    private static final String TAG = "MetricActivity";

    // Radio Group
    private RadioGroup rg;

    // Radio Button
    private RadioButton rb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metric);

        rg = findViewById(R.id.rgroup);
    }

    public void rbclick(View view)
    {
        int radiobuttonid = rg.getCheckedRadioButtonId();

        // Storing
        rb = findViewById(radiobuttonid);

        Toast.makeText(this, "Set to " + rb.getText(), Toast.LENGTH_LONG).show();
    }
}

