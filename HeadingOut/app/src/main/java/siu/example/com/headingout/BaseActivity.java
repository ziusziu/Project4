package siu.example.com.headingout;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import siu.example.com.headingout.mainactivity.MainActivity;
import siu.example.com.headingout.util.Utilities;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        Utilities.hideKeyboard(this);

        mToolBar = (Toolbar)findViewById(getToolBarResource());
        setSupportActionBar(mToolBar);
        initNavDrawer();
    }

    protected abstract int getLayoutResource();

    protected abstract int getDrawerLayoutResource();

    protected abstract int getToolBarResource();

    protected abstract int getNavViewResource();

    private void initNavDrawer(){
        DrawerLayout drawer = (DrawerLayout)findViewById(getDrawerLayoutResource());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.removeDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(getNavViewResource());
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(getDrawerLayoutResource());
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
            MainFragment mainFragment = new MainFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_fragment_container, mainFragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(getDrawerLayoutResource());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
