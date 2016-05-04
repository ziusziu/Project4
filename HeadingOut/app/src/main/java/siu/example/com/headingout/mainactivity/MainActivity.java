package siu.example.com.headingout.mainactivity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import siu.example.com.headingout.BaseActivity;
import siu.example.com.headingout.inputactivity.InputActivity;
import siu.example.com.headingout.R;
import siu.example.com.headingout.model.Trip;
import siu.example.com.headingout.util.Utilities;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static String TAG = MainActivity.class.getSimpleName();
    private static Toolbar mToolBar;
    private static EditText mLocEditText;
    private static Button mAddButton;
    private static RecyclerView mTripRecyclerView;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide keyboard after activity loads


        initializeViews();
        setSupportActionBar(mToolBar);
        initNavDrawer();
        setAddButtonListener();

        recyclerViewSetup();

    }

    private void recyclerViewSetup(){
        List<Trip> tripList = new ArrayList<>();

        // Dummy Data
        Trip trip = new Trip("San Francisco");
        Trip trip1 = new Trip("San Francisco");
        Trip trip2 = new Trip("San Francisco");
        Trip trip3 = new Trip("San Francisco");
        Trip trip4 = new Trip("San Francisco");
        Trip trip5 = new Trip("San Francisco");
        Trip trip6 = new Trip("San Francisco");
        tripList.add(trip);
        tripList.add(trip1);
        tripList.add(trip2);
        tripList.add(trip3);
        tripList.add(trip4);
        tripList.add(trip5);
        tripList.add(trip6);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mTripRecyclerView.setLayoutManager(linearLayoutManager);
        mTripRecyclerView.setHasFixedSize(true);
        MainTripRVAdapter recyclerViewAdapter = new MainTripRVAdapter(tripList);
        mTripRecyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void setAddButtonListener(){
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent input = new Intent(MainActivity.this, InputActivity.class);
                startActivity(input);
            }
        });
    }


    private void initializeViews(){
        mToolBar = (Toolbar)findViewById(R.id.main_toolBar);
        mLocEditText = (EditText)findViewById(R.id.main_locationInput_edittext);
        mAddButton = (Button)findViewById(R.id.main_addLocation_button);
        mTripRecyclerView = (RecyclerView)findViewById(R.id.main_recyclerView);
    }

    private void initNavDrawer(){
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.removeDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_home){
            Intent home = new Intent(MainActivity.this, MainActivity.class);
            startActivity(home);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
