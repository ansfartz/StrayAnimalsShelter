package com.bcs.andy.strayanimalsshelter.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.database.MarkersDatabase;
import com.bcs.andy.strayanimalsshelter.database.MarkersDatabaseListener;
import com.bcs.andy.strayanimalsshelter.model.AnimalMarker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    // Firebase
    private MarkersDatabase markersDatabase;
    private FirebaseAuth firebaseAuth;

    // widgets
    private AutoCompleteTextView mSearchText;
    private ImageView gpsImageView;
    private ImageView toogleMyMarkersImageView;

    // vars
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private Geocoder geocoder;

    private List<Marker> myMarkersList;
    private List<Marker> allMarkersList;
    private Boolean myMarkersVisible = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search_ET);
        gpsImageView = (ImageView) findViewById(R.id.ic_gps);
        toogleMyMarkersImageView = (ImageView) findViewById(R.id.ic_eye);



        getLocationPermission();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p>
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment.
     * <p>
     * This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: MAP IS READY");

        mMap = googleMap;


        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            initListeners();
            startMarkersListener();

            // TODO: move drawMarkers inside getDeviceLocation, and implement it so that only markers in a certain range will be displayed( maybe? )

        }
    }


    private void initListeners() {
        Log.d(TAG, "initListeners: initializing SearchText");

        firebaseAuth = FirebaseAuth.getInstance();
        geocoder = new Geocoder(MapActivity.this);
        markersDatabase = new MarkersDatabase();
        myMarkersList = new ArrayList<>();
        allMarkersList = new ArrayList<>();

        gpsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked GPS icon");
                getDeviceLocation();
            }
        });


        toogleMyMarkersImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked myMarkers icon");
                toggleMyMarkers();
            }
        });


        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                // if ENTER key is pressed
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }
                return false;
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String locationTitle = addresses.get(0).getAddressLine(0);

                AnimalMarker marker = new AnimalMarker(latLng.latitude, latLng.longitude, locationTitle, firebaseAuth.getCurrentUser().getUid());
                markersDatabase.addMarker(marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getLatitude(), marker.getLongitude()), DEFAULT_ZOOM));

            }
        });


    }

    /**
     *   Find LatLng from locationName, and move camera on it
     */
    private void geoLocate() {
        Log.d(TAG, "geoLocate: geoLocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.d(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found at location: " + address.toString());


            LatLng searchedPlace = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchedPlace, DEFAULT_ZOOM));

        }

    }

    /**
     *   Checks if App Permissions are allowed.
     *   <p>
     *   If not, Prompts the user for them using {@link ActivityCompat#requestPermissions(Activity, String[], int)}
     *   </p>
     */
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: GETTING LOCATION PERMISSIONS");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }



    /**
     *   Finds device's last location using {@link LocationServices#getFusedLocationProviderClient(Context)} and centers camera on it's coordinates
     */
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {

            if (mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            Log.d(TAG, "onComplete: my current location is: " + currentLatLng.latitude + ", long: " + currentLatLng.longitude);
                            moveCameraAndAddMarker(currentLatLng, DEFAULT_ZOOM, "My Location");
//                            mMap.addMarker(new MarkerOptions().position(currentLatLng).title("You are here"));


                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }




    /**
     * Moves camera and adds a marker on specified coordinates
     * @param latLng contains coordinates where the map will be centered on
     * @param zoom the desired zoom level, in the range of 2.0 to 21.0. Values below this range are set to 2.0, and values above it are set to 21.0.
     * @param title the title to be set on the marker
     */
    private void moveCameraAndAddMarker(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCameraAndAddMarker: moving the camera to: lat: " + latLng.latitude + ", long: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions();
            options.position(latLng).title(title);
            mMap.addMarker(options);
        }

    }



    private void startMarkersListener() {
        markersDatabase.readCurrentUserMarkers(new MarkersDatabaseListener() {
            @Override
            public void onCurrentUserMarkersCallBack(List<AnimalMarker> list) {
                Log.d(TAG, "onCurrentUserMarkersCallBack: myMarkersList updated");

                clearAllMyMarkers();
                for (AnimalMarker animalMarker : list) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(animalMarker.getLatitude(), animalMarker.getLongitude()))
                            .title(animalMarker.getLocation())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .visible(myMarkersVisible));

                    Log.d(TAG, "onCurrentUserMarkersCallBack: created my marker at: " + marker.getTitle());
                    myMarkersList.add(marker);
                }
            }

            @Override
            public void onAllMarkersCallBack(List<AnimalMarker> list) {
                Log.d(TAG, "onCurrentUserMarkersCallBack ----> onAllMarkersCallBack: should never reach here");

            }
        });


        markersDatabase.readAllMarkers(new MarkersDatabaseListener() {
            @Override
            public void onCurrentUserMarkersCallBack(List<AnimalMarker> list) {
                Log.d(TAG, "onAllMarkersCallBack ----> onCurrentUserMarkersCallBack: should never reach here");
            }

            @Override
            public void onAllMarkersCallBack(List<AnimalMarker> list) {

                clearAllPublicMarkers();
                for(AnimalMarker animalMarker : list) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(animalMarker.getLatitude(), animalMarker.getLongitude()))
                            .title(animalMarker.getLocation())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .visible(true));

                    Log.d(TAG, "onAllMarkersCallBack: created public marker at: " + marker.getTitle());
                    allMarkersList.add(marker);
                }
                drawPublicMarkers();

            }
        });
    }




    public void toggleMyMarkers() {
        if (!myMarkersVisible) {
            Log.d(TAG, "toggleMyMarkers: VISIBLE");
            showAllMyMarkers();
            myMarkersVisible = true;
        } else {
            Log.d(TAG, "toggleMyMarkers: INVISIBLE");
            hideAllMyMarkers();
            myMarkersVisible = false;
        }
    }

    public void clearAllMyMarkers() {
        for (Marker marker : myMarkersList) {
            marker.remove();
        }
        myMarkersList.clear();
    }

    public void clearAllPublicMarkers() {
        for(Marker marker : allMarkersList) {
            marker.remove();
        }
        allMarkersList.clear();
    }

    public void hideAllMyMarkers() {
        for (Marker marker : myMarkersList) {
            marker.setVisible(false);
            Log.d(TAG, "showAllMyMarkers: making INVISIBLE:" + marker.getTitle());
        }
    }

    public void showAllMyMarkers() {
        for (Marker marker : myMarkersList) {
            marker.setVisible(true);
            Log.d(TAG, "showAllMyMarkers: making VISIBLE:" + marker.getTitle());
        }
    }

    private void drawPublicMarkers() {
        Log.d(TAG, "drawPublicMarkers: am here");
        for(Marker marker: allMarkersList) {
            Log.d(TAG, "drawPublicMarkers: drawing marker: lat:" + marker.getPosition().latitude + " long:" + marker.getPosition().longitude);
            marker.setVisible(true);
        }
    }


    // Callback for the result from requesting permissions.
    // This method is invoked for every call on ActivityCompat.requestPermissions(). that are called in getLocationPermission()
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: CHECKING PERMISSIONS RESULT");
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: PERMISSION FAILED");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: PERMISSION GRANTED");
                    mLocationPermissionGranted = true;
                    // initialize map
                }

            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MapActivity.this, MainActivity.class));
        finish();
    }


}
