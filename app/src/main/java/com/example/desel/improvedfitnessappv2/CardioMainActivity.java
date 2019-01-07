package com.example.desel.improvedfitnessappv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class CardioMainActivity extends AppCompatActivity
{
    // VARIABLES
    // Debugging
    private static final String TAG = "CardioMainActivity";

    // Array Lists - Horizontal
    private ArrayList<String> mNamesHorizontal = new ArrayList();
    private ArrayList<String> mImageUrlsHorizontal = new ArrayList();

    // Array Lists - Vertical
    private ArrayList<String> mNamesVertical = new ArrayList();
    private ArrayList<String> mImageUrlsVertical = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio_main);
        Log.d(TAG, "onCreate: Cardio activity launched - CMA 30");

        // WORK

        getImagesHorizontal();
        Log.d(TAG, "onCreate: Horizontal images loaded - CMA 35");

        getImagesVertical();
        Log.d(TAG, "onCreate: Vertical images loaded - CMA 38");
    }

    private void getImagesHorizontal()
    {
        // Log for debugging
        Log.d(TAG, "getImagesHorizontal: Loading horizontal images - CMA 44");

        // Images from online
        mImageUrlsHorizontal.add("https://cdn.arstechnica.net/wp-content" +
                "/uploads/2017/03/HD-LIVE-Map-2-980x663.png");
        mNamesHorizontal.add("Map");

        mImageUrlsHorizontal.add("https://ak7.picdn.net/shutterstock/" +
                "videos/28448287/thumb/5.jpg");
        mNamesHorizontal.add("BMI Calculator");

        mImageUrlsHorizontal.add("https://thumbs-prod.si-cdn.com" +
                "/ERHepwQvEieCSVfCOW37pI5ceio=/800x600/filters:no_upscale()" +
                "/https://public-media.smithsonianmag.com" +
                "/filer/81/bd/81bd7854-7a8f-4266-8f46-fa6baa13386a" +
                "/centimeter-measurement-meter-feet-millimeter-foot-1476919.jpg");
        mNamesHorizontal.add("Imperial/Metric");

        mImageUrlsHorizontal.add("https://i.pinimg.com/474x/31/06/4b/" +
                "31064b95558727047c7a4a3b39dc2dc7.jpg");
        mNamesHorizontal.add("Diet");

        mImageUrlsHorizontal.add("https://wallpapercave.com/wp/wp2474807.jpg");
        mNamesHorizontal.add("Settings");

        initRecyclerViewHorizontal();
    }

    private void initRecyclerViewHorizontal()
    {
        Log.d(TAG, "initRecyclerView: Starting recyclerViewHorizontal - CMA 84");

        LinearLayoutManager layoutManager = new LinearLayoutManager
                // Make sure set to the  right orientation
                (this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView recyclerViewHorizontal = findViewById(R.id.rvHorizontal);
        recyclerViewHorizontal.setLayoutManager(layoutManager);

        HorizontalRecyclerViewAdapter adapter = new HorizontalRecyclerViewAdapter
                (this, mNamesHorizontal, mImageUrlsHorizontal);
        recyclerViewHorizontal.setAdapter(adapter);
    }

    private void getImagesVertical()
    {
        // Log for debugging
        Log.d(TAG, "getImagesVertical: Loading vertical images - CMA 101");

        // Images from online
        mImageUrlsVertical.add("https://c1.staticflickr.com/5" +
                "/4636/25316407448_de5fbf183d_o.jpg");
        mNamesVertical.add("Walking Outdoors");

        mImageUrlsVertical.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNamesVertical.add("Walking Treadmill");

        mImageUrlsVertical.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNamesVertical.add("Jogging Outdoors");

        mImageUrlsVertical.add("https://i.redd.it/j6myfqglup501.jpg");
        mNamesVertical.add("Jogging Treadmill");

        mImageUrlsVertical.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNamesVertical.add("Running Outdoors");

        mImageUrlsVertical.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNamesVertical.add("Running Treadmill");

        mImageUrlsVertical.add("https://i.redd.it/glin0nwndo501.jpg");
        mNamesVertical.add("Marathon Outdoors");

        mImageUrlsVertical.add("https://i.redd.it/obx4zydshg601.jpg");
        mNamesVertical.add("Sprint Treadmill");

        initRecyclerViewVertical();
    }

    private void initRecyclerViewVertical()
    {
        // Log for debugging
        Log.d(TAG, "initrecyclerView: Starting recyclerViewVertical - CMA 135");

        LinearLayoutManager layoutManager = new LinearLayoutManager
                // Make sure set to the  right orientation
                (this, LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerViewVertical = findViewById(R.id.rvVertical);
        recyclerViewVertical.setLayoutManager(layoutManager);

        VerticalRecyclerViewAdapter adapter
                = new VerticalRecyclerViewAdapter(this, mNamesVertical,
                mImageUrlsVertical);
        recyclerViewVertical.setAdapter(adapter);
    }
}

