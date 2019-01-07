package com.example.desel.improvedfitnessappv2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavMapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationEngineListener, PermissionsListener, MapboxMap.OnMapClickListener
{
    // VARIABLES
    // Default
    private MapView mapView;

    // Location
    private MapboxMap mapboxMap;

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
    private Button startButton;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG = "CardioMainActivity";

    // Search
    private CarmenFeature home;
    private CarmenFeature work;
    private String symbolIconId = "symbolIconId";
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_nav_maps);
        mapView = findViewById(R.id.mapView);
        startButton = findViewById(R.id.startButton);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        startButton.setOnClickListener(v -> {
            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                    .directionsRoute(currentRoute)
                    .origin(originPosition)
                    .destination(destinationPosition)
                    //.enableOffRouteDetection(true)
                    //.snapToRoute(true)
                    .shouldSimulateRoute(true)
                    .build();
            NavigationLauncher.startNavigation(NavMapsActivity.this, options);
        });
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap)
    {
        map = mapboxMap;
        map.addOnMapClickListener(this);

        enableLocation();

        NavMapsActivity.this.mapboxMap = mapboxMap;
        initSearchFab();
        addUserLocations();

        // Add the symbol layer icon to map for future use
        Bitmap icon = BitmapFactory.decodeResource
                (NavMapsActivity.this.getResources(),
                        R.drawable.mapbox_marker_icon_default);
        mapboxMap.addImage(symbolIconId, icon);

        // Create an empty GeoJSON source using the empty feature collection
        setUpSource();

        // Set up symbol layer for displaying searched location's feature coordinates
        setupLayer();
    }

    // Enables user location and checks for granted permissions
    private void enableLocation()
    {
        // If user permissions are granted
        if (PermissionsManager.areLocationPermissionsGranted(this))
        {
            initializeLocationEngine();
            initializeLocationLayer();
        }
        // If user has not granted permissions
        else
        {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
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
        locationLayerPlugin.setRenderMode(RenderMode.GPS);
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
            startButton.setEnabled(true);
            startButton.setBackgroundResource(R.color.mapbox_blue);
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
                            Toast.makeText(NavMapsActivity.this,
                                    "Permissions Error, try again later",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (response.body().routes().size() == 0)
                        {
                            Log.e(TAG, "No route found");
                            Toast.makeText(NavMapsActivity.this,
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

    private void initSearchFab()
    {
        FloatingActionButton searchFab = findViewById(R.id.fab);
        searchFab.setOnClickListener((View view) -> {
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken())
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(Color.parseColor("#EEEEEE"))
                            .limit(10)
                            .addInjectedFeature(home)
                            .addInjectedFeature(work)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(NavMapsActivity.this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        });
    }

    private void addUserLocations()
    {
        home = CarmenFeature.builder().text("Mapbox SF Office")
                .geometry(Point.fromLngLat(-73.99155, 40.73581))
                .placeName("31 Lyngarth Road, Durban, Kloof")
                .id("mapbox-sf")
                .properties(new JsonObject())
                .build();

        work = CarmenFeature.builder().text("Mapbox DC Office")
                .placeName("740 15th Street NW, Washington DC")
                .geometry(Point.fromLngLat(-77.0338348, 38.899750))
                .id("mapbox-dc")
                .properties(new JsonObject())
                .build();
    }

    private void setUpSource()
    {
        GeoJsonSource geoJsonSource = new GeoJsonSource(geojsonSourceLayerId);
        mapboxMap.addSource(geoJsonSource);
    }

    private void setupLayer()
    {
        SymbolLayer selectedLocationSymbolLayer = new SymbolLayer
                ("SYMBOL_LAYER_ID", geojsonSourceLayerId);
        selectedLocationSymbolLayer.withProperties(PropertyFactory
                .iconImage(symbolIconId));
        mapboxMap.addLayer(selectedLocationSymbolLayer);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE)
        {
            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            // Create a new FeatureCollection and add a new Feature to it using
            // selectedCarmenFeature above
            FeatureCollection featureCollection = FeatureCollection.fromFeatures
                    (new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())});

            // Retrieve and update the source designated for showing a selected
            // location's symbol layer icon
            GeoJsonSource source = mapboxMap.getSourceAs(geojsonSourceLayerId);
            if (source != null)
            {
                source.setGeoJson(featureCollection);
            }

            // Move map camera to the selected location
            CameraPosition newCameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(((Point)
                            selectedCarmenFeature.geometry()).latitude(),
                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                    .zoom(14)
                    .build();
            mapboxMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(newCameraPosition), 4000);
        }
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
