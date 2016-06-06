package app.ga.com.headingout;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import app.ga.com.headingout.inputfragment.InputFragment;
import app.ga.com.headingout.mainfragment.MainFragment;
import app.ga.com.headingout.util.FragmentUtil;
import app.ga.com.headingout.util.Utilities;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentUtil {

    private static final String TAG = "BaseActivity";
    private static Toolbar toolBar;
    private static ActionBarDrawerToggle drawerToggle;
    private static FragmentManager fragmentManager;
    private static MainFragment mainFragment;
    private static InputFragment inputFragment;
    private static ActionBar actionBar;

    private static final String MAIN_FRAGMENT = "MainFragment";
    private static final String INPUT_FRAGMENT = "InputFragment";
    private static final String DETAIL_FRAGMENT = "DetailFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        Utilities.hideKeyboard(this);

        loadMainFragment();
        initActionBar();
        initNavDrawer();
    }


    /**
     * Special Actions for ActionBar when DetailFragment is loaded
     * ActionBarIcon is a return arrow and creates a new InputFragment
     * @param fragmentName
     */
    @Override
    public void setFragmentToolBar(String fragmentName) {
        switch (fragmentName) {
            case MAIN_FRAGMENT:
                actionBar.setTitle("Search");
                setActionBarIcon(R.drawable.ic_menu_24dp);
                initNavDrawer();
                break;
            case INPUT_FRAGMENT:
                actionBar.setTitle("Results");
                setActionBarIcon(R.drawable.ic_menu_24dp);
                initNavDrawer();
                break;
            case DETAIL_FRAGMENT:
                drawerToggle.setDrawerIndicatorEnabled(false);
                setActionBarIcon(R.drawable.ic_arrow_back_24dp);
                actionBar.setTitle(fragmentName);
                toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO Fix MainFragment Icon becomes back arrow when Home clicked in InputFragment
                        Log.d(TAG, "onClick: ActionBarArrow was clicked");
                        setActionBarIcon(R.drawable.ic_menu_24dp);
                        drawerToggle.setDrawerIndicatorEnabled(true);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        InputFragment inputFragment = new InputFragment();
                        fragmentTransaction.replace(R.id.home_fragment_container, inputFragment);
                        fragmentTransaction.commit();
                    }
                });
                break;
            default:
                initNavDrawer();
                setActionBarIcon(R.drawable.ic_menu_24dp);
        }
    }

    /**
     * Initialize Navigation Drawer and ActionBarDrawerToggle for open drawers
     */
    private void initNavDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(getDrawerLayoutResource());
        drawerToggle = new ActionBarDrawerToggle(this, drawer,
                toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(drawerToggle);
        drawer.removeDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(getNavViewResource());
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Handle Fragments with OnBackPressed Button instead of adding Fragments to BackStack
     * Don't want to add same fragment ot backstack multiple times.
     * If Nav Drawer is opened and Home is clicked 15 times.
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ===>> MainActivity BackPressed");
        DrawerLayout drawer = (DrawerLayout) findViewById(getDrawerLayoutResource());

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_fragment_container);
        String fragmentName = fragment.getClass().getSimpleName();
        Log.d(TAG, "onBackPressed: ====>>>" + fragment.getClass().getSimpleName());

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (fragmentName) {
                case MAIN_FRAGMENT:
                    Log.d(TAG, "onBackPressed: ===>>> On MainFragment when back pressed, leave app");
                    super.onBackPressed();
                case INPUT_FRAGMENT:
                    mainFragment = new MainFragment();
                    Log.d(TAG, "onBackPressed: ====>>> On InputFragment when back pressed, go to Main");
                    fragmentTransaction.replace(R.id.home_fragment_container, mainFragment);
                    break;
                case DETAIL_FRAGMENT:
                    inputFragment = new InputFragment();
                    Log.d(TAG, "onBackPressed: ===>>> On DetailFragment when back pressed, go to Input");
                    fragmentTransaction.replace(R.id.home_fragment_container, inputFragment);
                    break;
                default:
                    super.onBackPressed();
            }
            fragmentTransaction.commit();
        }
    }


    /**
     * On Navigation Item Drawer Click, replace fragment in Framelayout container
     * and close drawer.
     * @param item
     * @return
     */
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (id) {
            case R.id.nav_home:
                Log.d(TAG, "onNavigationItemSelected: ===>>> Drawer Home Clicked");
                setActionBarIcon(R.drawable.ic_menu_24dp);
                fragmentTransaction.replace(R.id.home_fragment_container, mainFragment);
                break;
            case R.id.nav_saved:
                Log.d(TAG, "onNavigationItemSelected: ===>>> Drawer saved Clicked");
                break;
            case R.id.nav_send:
                Log.d(TAG, "onNavigationItemSelected: ===>>> Drawer Send Clicked");
                break;
            default:
                setActionBarIcon(R.drawable.ic_menu_24dp);
                break;
        }
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(getDrawerLayoutResource());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void initActionBar(){
        toolBar = (Toolbar) findViewById(getToolBarResource());
        setSupportActionBar(toolBar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(MAIN_FRAGMENT);
    }

    /**
     * Load MainFragment into container when Activity is Created
     */
    private void loadMainFragment() {
        Log.d(TAG, "loadMainFragment: ===>>> Loading MainFragment in MainActivity");
        mainFragment = new MainFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_container, mainFragment);
        fragmentTransaction.commit();
    }

    /**
     * Changes the color of icon resource and sets it to the Action Bar
     * @param iconResource
     */
    protected void setActionBarIcon(int iconResource) {
        Log.d(TAG, "setActionBarIcon: ===>> MainActivity changing ActionBar Icon");
        int color = Color.parseColor("#FFFFFF");
        Drawable iconDrawable = ResourcesCompat.getDrawable(getResources(), iconResource, null);
        iconDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        toolBar.setNavigationIcon(iconDrawable);
    }

    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    protected int getDrawerLayoutResource() {
        return R.id.home_drawer_layout;
    }

    protected int getToolBarResource() {
        return R.id.home_toolBar;
    }

    protected int getNavViewResource() {
        return R.id.home_nav_view;
    }


}
