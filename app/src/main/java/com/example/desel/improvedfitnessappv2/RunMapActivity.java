package com.example.desel.improvedfitnessappv2;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RunMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationEngineListener, PermissionsListener, MapboxMap.OnMapClickListener
{
    // VARIABLES
    // Debugging
    private static final String TAG = "MapsActivity";

    // Default
    private MapView mapView;

    // Location
    private MapboxMap map;
    private Location originLocation;
    private LocationEngine locationEngine;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationLayerPlugin;

    // Markers
    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;

    // Navigation
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;

    // Timer
    private Chronometer chronometer;
    private Button btnStart;
    private Button btnStop;
    private Button btnReset;
    private boolean running;
    private long pauseOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_run_map);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        Log.d(TAG, "onCreate: Map Opening...");

        // TYPE CASTING
        chronometer = findViewById(R.id.chronometer);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnReset = findViewById(R.id.btnReset);

        btnStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!running){
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (running){
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
            }
        });
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap)
    {
        map = mapboxMap;
        map.addOnMapClickListener(this);

        // Log for debugging
        Log.d(TAG, "onMapReady: Map is ready, awaiting location data - MpA 106");

        enableLocation();
    }

    // Enables user location and checks for granted permissions
    private void enableLocation()
    {
        // Log for debugging
        Log.d(TAG, "enableLocation: Attempting o find location - MpA 115");

        // If user permissions are granted
        if (PermissionsManager.areLocationPermissionsGranted(this))
        {
            initializeLocationEngine();
            initializeLocationLayer();

            // Log for debugging
            Log.d(TAG, "enableLocation: User location found");
        }
        // If user has not granted permissions
        else
        {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);

            // Log for debugging
            Log.d(TAG, "enableLocation: Permissions no given - MpA 133");
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine()
    {
        locationEngine = new LocationEngineProvider(this)
                .obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null)
        {
            originLocation = lastLocation;
        }
        else
        {
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressLint("WrongConstant")
    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer()
    {
        locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);

        // Shows the location icon
        locationLayerPlugin.setLocationLayerEnabled(true);
        // How the app follows the user - changeable
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        // How the user icon looks - changeable
        locationLayerPlugin.setRenderMode(RenderMode.COMPASS);
    }

    private void setCameraPosition(Location location)
    {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng
                (location.getLatitude(), location.getLongitude()), 13.0));
    }

    @Override
    public void onMapClick(@NonNull LatLng point)
    {
        // First stage
        try
        {
            // Removes existing marker
            if (destinationMarker != null)
            {
                map.removeMarker(destinationMarker);
            }

            // Adds new marker - can change its look
            destinationMarker = map.addMarker(new MarkerOptions().position(point));

            destinationPosition = Point.fromLngLat(point.getLongitude(),
                    point.getLatitude());
            originPosition = Point.fromLngLat(originLocation.getLongitude(),
                    originLocation.getLatitude());
            getRoute(originPosition, destinationPosition);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Unexpected error, try again",
                    Toast.LENGTH_SHORT).show();
        }

        // Second stage
        try
        {
            // Button activates
            btnStart.setEnabled(true);
            btnStart.setBackgroundResource(R.color.softGrey);

            btnStop.setEnabled(true);
            btnStop.setBackgroundResource(R.color.softGrey);

            btnReset.setEnabled(true);
            btnReset.setBackgroundResource(R.color.softGrey);
        }
        catch (Exception e)
        {
            Toast.makeText(this,
                    "Button activation unexpectedly failed, " +
                            "Restart app or try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void getRoute(Point origin, Point destination)
    {
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>()
                {
                    @SuppressLint("LogNotTimber")
                    @Override
                    public void onResponse(Call<DirectionsResponse> call,
                                           Response<DirectionsResponse> response)
                    {
                        if (response.body() == null)
                        {
                            Log.e(TAG, "No routes found, check right user " +
                                    "and access token");
                            Toast.makeText(RunMapActivity.this,
                                    "Permissions Error, try again later",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (response.body().routes().size() == 0)
                        {
                            Log.e(TAG, "No route found");
                            Toast.makeText(RunMapActivity.this,
                                    "No Routes detected",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Do not put DirectionsRoute
                        currentRoute = response.body().routes().get(0);

                        if (navigationMapRoute != null)
                        {
                            navigationMapRoute.removeRoute();
                        }
                        else
                        {
                            navigationMapRoute = new NavigationMapRoute
                                    (null, mapView, map);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call,
                                          Throwable t)
                    {
                        Log.e(TAG, "Error: " + t.getMessage());
                    }
                });
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected()
    {
        // Used to be this code - change if there is an issue
        //locationEngine.removeLocationUpdates();
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (location != null)
        {
            originLocation = location;
            setCameraPosition(location);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain)
    {
        // Toast or Dialog explaining why permissions are required
    }

    @Override
    public void onPermissionResult(boolean granted)
    {
        if (granted)
        {
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults)
    {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onStart()
    {
        super.onStart();
        if (locationEngine != null)
        {
            // Old code is below, change beck to it if there are errors + remove warn
            //locationEngine.removeLocationUpdates();
            locationEngine.requestLocationUpdates();
        }
        if (locationLayerPlugin != null)
        {
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (locationEngine != null)
        {
            locationEngine.removeLocationUpdates();
        }
        if(locationLayerPlugin != null)
        {
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (locationEngine != null)
        {
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}

