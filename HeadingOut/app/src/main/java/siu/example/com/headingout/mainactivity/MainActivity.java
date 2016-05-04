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

public class MainActivity extends BaseActivity {

    private static String TAG = MainActivity.class.getSimpleName();
    private static EditText mLocEditText;
    private static Button mAddButton;
    private static RecyclerView mTripRecyclerView;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected int getDrawerLayoutResource() {
        return R.id.main_drawer_layout;
    }

    @Override
    protected int getToolBarResource() {
        return R.id.main_toolBar;
    }

    @Override
    protected int getNavViewResource() {
        return R.id.main_nav_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide keyboard after activity loads
        initializeViews();
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
        mLocEditText = (EditText)findViewById(R.id.main_locationInput_edittext);
        mAddButton = (Button)findViewById(R.id.main_addLocation_button);
        mTripRecyclerView = (RecyclerView)findViewById(R.id.main_recyclerView);
    }

}
