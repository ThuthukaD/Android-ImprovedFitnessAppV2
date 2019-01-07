package com.example.desel.improvedfitnessappv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    // VARIABLES
    // Debugging
    private static final String TAG = "MainActivity";

    // Other
    private static final int NUM_COLUMNS = 1;

    // Array Lists
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    // Text View
    TextView tvUser;
    TextView tvEmail;

    // Strings
    String stLogin;
    String stEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation Drawer default code
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation View default code
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // The navigation header view
        View header = navigationView.getHeaderView(0);
        tvUser = header.findViewById(R.id.tvUser);
        tvEmail = header.findViewById(R.id.tvEmail);

        Log.d(TAG, "onCreate: Main activity launched - MA 58");

        initImageBitmaps();
        Log.d(TAG, "onCreate: Staggered images loaded - MA 61");

        // Text Views
        // tvUser = findViewById(R.id.tvUser);

        try
        {
            Text();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        // Default code
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);

            // Log for debugging
            Log.d(TAG, "onBackPressed: Back button pressed - executed - MA 74");
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Default code
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Default code
        // Handle action bar item clicks
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this,
                    SettingsActivity.class);
            startActivity(intent);

            Log.d(TAG, "onOptionsItemSelected: " + id + "selected - MA 101");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Log for debugging
        Log.d(TAG, "onNavigationItemSelected: NavView item" + id
                + " clicked - MA 116");

        // Camera
        if (id == R.id.nav_camera)
        {
            try
            {
                // Opens the camera and takes pictures
                Camera();

                // Log for debugging
                Log.d(TAG, "onNavigationItemSelected: Opening Camera...");
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Error opening Camera",
                        Toast.LENGTH_SHORT).show();

                // Log for debugging
                Log.d(TAG, "onNavigationItemSelected: Camera error - MA 131");
            }
            // Helpful toast for the user
            Toast.makeText(getApplicationContext(), "Image will be saved in " +
                            "Gallery under Camera",
                    Toast.LENGTH_LONG).show();
        }
        // Gallery
        else if (id == R.id.nav_gallery)
        {
            // Will open the set goals page in the future
            Toast.makeText(getApplicationContext(), "This feature is locked",
                    Toast.LENGTH_SHORT).show();
        }

        // Maps
        else if (id == R.id.nav_maps)
        {
            try
            {
                // Will try open the maps page
                Intent intent = new Intent(MainActivity.this,
                        NavMapsActivity.class);
                startActivity(intent);

                // Log for debugging
                Log.d(TAG, "onNavigationItemSelected: Opening MapActivity...");
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), "Error opening " +
                                "activity_nav_maps",
                        Toast.LENGTH_SHORT).show();

                // Log for debugging
                Log.d(TAG, "onNavigationItemSelected: NavMap error - MA 163");
            }
        }

        // About
        else if (id == R.id.nav_about)
        {
            try
            {
                //Will try open the about page
                Intent intent = new Intent(MainActivity.this,
                        AboutActivity.class);
                startActivity(intent);
                Log.d(TAG, "onNavigationItemSelected: Opening AboutActivity...");
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), "Error opening " +
                                "activity_about",
                        Toast.LENGTH_SHORT).show();

                // Log for debugging
                Log.d(TAG, "onNavigationItemSelected: About error - MA 184");
            }
        }
        // Tools
        else if (id == R.id.nav_tools)
        {
            // Will open the set goals page in the future
            Toast.makeText(getApplicationContext(), "This feature is locked",
                    Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // Log for debugging
        Log.d(TAG, "onNavigationItemSelected: Drawer Closed - MA 208");
        return true;
    }

    private void initImageBitmaps()
    {
        // Log for debugging
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        // Images being pulled from the internet
        mImageUrls.add("http://hdqwalls.com/wallpapers/gym-girl-8k-image.jpg");
        mNames.add("Home Exercise Sets");

        mImageUrls.add("https://www.vactualpapers.com/web/wallpapers/a-woman-" +
                "working-out-in-the-gym-health-hd-wallpaper-11/thumbnail/lg.jpg");
        mNames.add("Gym Exercise Sets");

        mImageUrls.add("https://images.onlymyhealth.com/imported/images/" +
                "2017/October/16_Oct_2017/morning-walk-650x380.jpg");
        mNames.add("Cardio");

        initRecyclerView();
    }

    private void initRecyclerView()
    {
        // Log for debugging
        Log.d(TAG, "initRecyclerView: Starting staggered recyclerView - MA 226");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter
                = new StaggeredRecyclerViewAdapter(this,mNames, mImageUrls);

        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager
                (NUM_COLUMNS, LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
    }

    public void Camera ()
    {
        try
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,0);

            // Log for debugging
            Log.d(TAG, "Camera: Camera Opened");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(TAG, "Camera: Carmera Issue - MA 250");
        }
    }

    protected void CameraFunction (int requestCode, int resultCode, Intent data)
    {
        try
        {
            super.onActivityResult(requestCode, resultCode, data);
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            // Log for debugging
            Log.d(TAG, "CameraFunction: Camera Functioning");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // Log for debugging
            Log.d(TAG, "CameraFunction: Camrea functionality erreo - MA 280");
        }
    }

    // Calls prefs value from LoginActivity
    public void Text()
    {
        try
        {
            // Passed string for user
            stLogin = getIntent().getExtras().getString("UserLogin", " ifaTest01");
            tvUser.setText(stLogin);

            stEmail = getIntent().getExtras().getString("EmailLogin", " Email");
            tvEmail.setText(stEmail);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Closes page but not the app. Should return to Login page
    public void Cancel(View view)
    {
        finish();
    }
}
