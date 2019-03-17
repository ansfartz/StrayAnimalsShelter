package com.bcs.andy.strayanimalsshelter.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.MapsActivity;
import com.bcs.andy.strayanimalsshelter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Firebase vars
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    // Widgets
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
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
        else navigationViewUsername.setText(user.getDisplayName());
        navigationViewEmail.setText(user.getEmail());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        init();

        setNavigationUserDetails();

        if (savedInstanceState == null) {
            // this will open our MessageFragment upon creation of the activity, before clicking anything
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyAnimalsFragment()).commit();
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
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyAnimalsFragment()).commit();
                break;
            case R.id.nav_map:
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                break;
            case R.id.nav_markers:
                Toast.makeText(this, "MARKERS", Toast.LENGTH_SHORT).show();
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
}
