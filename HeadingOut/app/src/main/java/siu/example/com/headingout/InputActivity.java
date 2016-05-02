package siu.example.com.headingout;

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

import java.util.ArrayList;
import java.util.List;

public class InputActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Toolbar mToolBar;
    public static String POSITION = "POSITION";
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);



        mToolBar = (Toolbar)findViewById(R.id.input_toolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Input");
        initNavDrawer();

        mViewPager = (ViewPager)findViewById(R.id.input_viewPager);
        mViewPager.setAdapter(new InputTabsFragmentPagerAdapter(getSupportFragmentManager()));

        mTabLayout = (TabLayout)findViewById(R.id.input_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

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
}
