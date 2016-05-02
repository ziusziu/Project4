package siu.example.com.headingout;

import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
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
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class InputActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Toolbar mToolBar;
    public static String POSITION = "POSITION";
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;
    private FloatingActionButton mInputContinueFabButton;
    protected static final String FAB_BUTTON_COLOR = "#00C853"; //"#558B2F"
    protected static final String INTENT_FLIGHT_KEY = "location_terms";
    protected static final String SHARED_PREFERENCES_FLIGHTTERM = "shared_pref_location_term";
    private static EditText mFlightEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        mFlightEditText = (EditText)findViewById(R.id.input_flight_editText);

        mToolBar = (Toolbar)findViewById(R.id.input_toolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Input");
        initNavDrawer();

        mViewPager = (ViewPager)findViewById(R.id.input_viewPager);
        mViewPager.setAdapter(new InputTabsFragmentPagerAdapter(getSupportFragmentManager()));

        mTabLayout = (TabLayout)findViewById(R.id.input_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);


        mInputContinueFabButton = (FloatingActionButton)findViewById(R.id.input_continue_fab);
        setFabIconColor(mInputContinueFabButton, FAB_BUTTON_COLOR);
        onFabContinueButtonClick();

    }

    // Save and restore last known tab position
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mTabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mViewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



    private void initNavDrawer(){
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.input_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.removeDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.input_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.input_drawer_layout);
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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.input_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected static void setFabIconColor(FloatingActionButton searchFab, String fabColor){
        int color = Color.parseColor(fabColor);
        searchFab.setImageResource(R.drawable.icon_search);
        searchFab.setColorFilter(color);
    }

    private void onFabContinueButtonClick(){
        mInputContinueFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] searchTerms = {
                        "Hello"//mFlightEditText.getText().toString(),
                };

                PreferenceManager.getDefaultSharedPreferences(InputActivity.this)
                        .edit()
                        .putString(SHARED_PREFERENCES_FLIGHTTERM, searchTerms[0])
                        .apply();

                Intent mFlightResultsIntent = new Intent(InputActivity.this,DetailActivity.class);
                mFlightResultsIntent.putExtra(INTENT_FLIGHT_KEY, searchTerms);
                startActivity(mFlightResultsIntent);
            }
        });
    }

}
