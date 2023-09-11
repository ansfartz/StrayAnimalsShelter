package com.bcs.andy.strayanimalsshelter.ui;

import android.app.Dialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    // Firebase vars
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    // Widgets
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;   // the 3Lines button at the left side of the toolbar
    private NavigationView navigationView;

    private View headerView;
    private TextView navigationViewUsername;
    private TextView navigationViewEmail;

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.default_toolbar_main_activity);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.myDrawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // nav_view is inside activity_main.xml
        // which contains navdrawer_header and navdrawer_menu
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);
        navigationViewUsername = (TextView) headerView.findViewById(R.id.nav_username);
        navigationViewEmail = (TextView) headerView.findViewById(R.id.nav_email);
    }

    /**
     * Uploading and updating a user's name that has just registered is an Async task,
     * therefore the name will not be updated yet upon calling setText() in {@link #navigationViewEmail} inside this method
     * If {@link MainActivity} has been started from {@link com.bcs.andy.strayanimalsshelter.ui.register.MyRegisterActivity}, this will correctly set the navigation username using the Bundle.
     */
    private void setNavigationUserDetails() {
        if (user.getDisplayName() == null)
            navigationViewUsername.setText(getIntent().getExtras().getString("displayName"));
        else
            navigationViewUsername.setText(user.getDisplayName());
        navigationViewEmail.setText(user.getEmail());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isServicesOK()) {
            initFirebase();
            init();
            setNavigationUserDetails();
        }


        if (savedInstanceState == null) {
            // default fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MyAnimalsFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new MyAnimalsFragment())
                        .commit();

                break;
            case R.id.nav_map:
                Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(mapIntent);
                finish();
                break;
            case R.id.nav_markers:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new MyMarkersFragment())
                        .commit();
                break;
            case R.id.nav_discover:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new DiscoverAnimalsFragment())
                        .commit();
                navigationView.setCheckedItem(R.id.nav_discover);
                break;
            case R.id.nav_feedback:
                String deviceInformation = "\n\n\n\n\n\nOS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")" +
                        "\n OS API Level: " + android.os.Build.VERSION.SDK_INT +
                        "\n Device: " + android.os.Build.DEVICE +
                        "\n Model: " + android.os.Build.MODEL;

                String[] sendTo = new String[]{"carolsfartz@gmail.com"};
                Intent emailIntent = new Intent(Intent.ACTION_SEND)
                        .setType("plain/text")
                        .putExtra(Intent.EXTRA_EMAIL, sendTo)
                        .putExtra(Intent.EXTRA_SUBJECT, "Stray Animals Shelter Feedback")
                        .putExtra(Intent.EXTRA_TEXT, deviceInformation);
                startActivity(Intent.createChooser(emailIntent, ""));
                break;

            case R.id.nav_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Check if user's device has the required Google Play Services version installed
     *
     * @return true if required version is available and installed, otherwise false and  prompts the user to update the current version of Google Play Services if possible on device
     */
    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking Google Services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {

            //everything is fine, and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred, but you can fix it");

            //we get a dialog right from google for this error
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();

        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}
