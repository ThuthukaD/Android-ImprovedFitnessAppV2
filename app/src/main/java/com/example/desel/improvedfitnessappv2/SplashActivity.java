package com.example.desel.improvedfitnessappv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity
{
    // Variables
    public ProgressBar progressBar;
    public int progressStatus = 0;
    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);

        // Progress Bar Code
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (progressStatus < 100)
                {
                    progressStatus++;
                    android.os.SystemClock.sleep(20);
                    progressBar.setProgress(progressStatus);
                }

                // Auto Login Code
                SharedPreferences prefs = getSharedPreferences("login",
                        MODE_PRIVATE);
                final Boolean isLogged = prefs.getBoolean("isLogged",
                        false);
                Thread timer = new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            sleep(10);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        finally
                        {
                            if (isLogged)
                            {
                                Intent intent = new Intent
                                        (SplashActivity.this,
                                                LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Intent intent = new Intent
                                        (SplashActivity.this,
                                                RegisterActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                };
                timer.start();
            }
        }).start();
    }
}

