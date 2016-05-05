package siu.example.com.headingout.util;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import siu.example.com.headingout.DetailFragment;
import siu.example.com.headingout.InputFragment;
import siu.example.com.headingout.MainFragment;
import siu.example.com.headingout.R;
import siu.example.com.headingout.util.Utilities;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BaseActivity";
    private static Toolbar mToolBar;
    private static FragmentManager fragmentManager;
    private static MainFragment mainFragment;
    private static InputFragment inputFragment;
    private static DetailFragment detailFragment;

    private static final String MAIN_FRAGMENT = "MainFragment";
    private static final String INPUT_FRAGMENT = "InputFragment";
    private static final String DETAIL_FRAGMENT = "DetailFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        Utilities.hideKeyboard(this);

        mainFragment = new MainFragment();
        inputFragment = new InputFragment();
        detailFragment = new DetailFragment();

        fragmentManager = getSupportFragmentManager();

        mToolBar = (Toolbar)findViewById(getToolBarResource());
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(getToolBarTitle());
        initNavDrawer();



    }

    protected abstract String getToolBarTitle();

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

        Log.d(TAG, "onBackPressed: ===>> BACKPRESSED" );
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_fragment_container);
        Log.d(TAG, "onBackPressed: ====>>>"+fragment.getClass().getSimpleName());

        String fragmentName = fragment.getClass().getSimpleName();

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch(fragmentName) {
                case MAIN_FRAGMENT:
                    Log.d(TAG, "onCreate:==== MAIN FRAGMENT");
                case INPUT_FRAGMENT:
                    Log.d(TAG, "onCreate:==== INPUT FRAGMENT");
                    fragmentTransaction.replace(R.id.home_fragment_container, mainFragment);
                    break;
                case DETAIL_FRAGMENT:
                    Log.d(TAG, "onCreate:==== DETAIL FRAGMENT");
                    fragmentTransaction.replace(R.id.home_fragment_container, inputFragment);
                    break;
                default:
                    super.onBackPressed();
            }
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        // TODO: Ask about fragmentManager one manager okay? Never close?

        if (id == R.id.nav_share) {
            Log.d(TAG, "onNavigationItemSelected: ===>>> Drawer Share Clicked");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_fragment_container, detailFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_send) {
            Log.d(TAG, "onNavigationItemSelected: ==>>> Drawer Send Clicked");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_fragment_container, inputFragment);
            fragmentTransaction.commit();

        }else if (id == R.id.nav_home){
            Log.d(TAG, "onNavigationItemSelected: ===>>> Drawer Home Clicked");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_fragment_container, mainFragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(getDrawerLayoutResource());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
