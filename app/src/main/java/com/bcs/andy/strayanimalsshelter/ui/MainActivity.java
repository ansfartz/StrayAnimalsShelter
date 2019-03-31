package com.bcs.andy.strayanimalsshelter.ui;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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
        toolbar = (Toolbar) findViewById(R.id.default_toolbar);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle toolbar item clicks here.

        switch (item.getItemId()) {
            case R.id.supportField:
                Toast.makeText(this, "Support", Toast.LENGTH_SHORT).show();
                break;
            case R.id.helpField:
                Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logoutField:
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
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
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_markers:
                Toast.makeText(this, "MARKERS", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new MyMarkersFragment())
                        .commit();
                break;
            case R.id.nav_friends:
                Toast.makeText(this, "FRIENDS", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_shelter:
                Toast.makeText(this, "SHELTERS", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Check if user's device has the required Google Play Services version installed
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
