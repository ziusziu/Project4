package siu.example.com.headingout;

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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import siu.example.com.headingout.detailfragment.DetailFragment;
import siu.example.com.headingout.inputfragment.InputFragment;
import siu.example.com.headingout.mainfragment.MainFragment;
import siu.example.com.headingout.util.FragmentUtil;
import siu.example.com.headingout.util.Utilities;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentUtil {

    private static final String TAG = "BaseActivity";
    private static Toolbar mToolBar;
    private static ActionBarDrawerToggle mDrawerToggle;
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

        createFragments();
        loadMainFragment();

        mToolBar = (Toolbar)findViewById(getToolBarResource());
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(getToolBarTitle());
        initNavDrawer();

    }





    @Override
    public void setFragmentToolBar(String fragmentName) {
        switch(fragmentName){
            case MAIN_FRAGMENT:
                getSupportActionBar().setTitle("Main");
                setActionBarIcon(R.drawable.ic_menu_24dp);
                initNavDrawer();
                break;
            case INPUT_FRAGMENT:
                getSupportActionBar().setTitle("Input");
                setActionBarIcon(R.drawable.ic_menu_24dp);
                initNavDrawer();
                break;
            case DETAIL_FRAGMENT:
                Log.d(TAG, "setFragmentToolBar: lasdjflsakfjklsdfj");
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                setActionBarIcon(R.drawable.ic_arrow_back_24dp);
                getSupportActionBar().setTitle("Detail");
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: ToolBar was clicked");
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

    private void createFragments(){
        Log.d(TAG, "createFragments: ===>>> Creating new Fragments in MainActvity");
        mainFragment = new MainFragment();
        inputFragment = new InputFragment();
        detailFragment = new DetailFragment();
    }

    private void loadMainFragment(){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_container, mainFragment);
        fragmentTransaction.commit();
    }


    protected String getToolBarTitle() {
        Fragment fragment = new MainFragment();
        return fragment.getClass().getSimpleName();
    }

    private void initNavDrawer(){
        DrawerLayout drawer = (DrawerLayout)findViewById(getDrawerLayoutResource());
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(mDrawerToggle);
        drawer.removeDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(getNavViewResource());
        navigationView.setNavigationItemSelectedListener(this);
    }

    protected int getLayoutResource() {
        return R.layout.activity_home;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(getDrawerLayoutResource());

        Log.d(TAG, "onBackPressed: ===>> BACKPRESSED");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_fragment_container);
        Log.d(TAG, "onBackPressed: ====>>>" + fragment.getClass().getSimpleName());

        String fragmentName = fragment.getClass().getSimpleName();

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch(fragmentName) {
                case MAIN_FRAGMENT:
                    Log.d(TAG, "onCreate:==== MAIN FRAGMENT");
                    super.onBackPressed();
                case INPUT_FRAGMENT:
                    mainFragment = new MainFragment();
                    Log.d(TAG, "onCreate:==== INPUT FRAGMENT");
                    fragmentTransaction.replace(R.id.home_fragment_container, mainFragment);
                    break;
                case DETAIL_FRAGMENT:
                    inputFragment = new InputFragment();
                    Log.d(TAG, "onCreate:==== DETAIL FRAGMENT");
                    fragmentTransaction.replace(R.id.home_fragment_container, inputFragment);
                    break;
                default:
                    super.onBackPressed();
            }
            fragmentTransaction.commit();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch(id){
            case R.id.nav_home:
                Log.d(TAG, "onNavigationItemSelected: ===>>> Drawer Home Clicked");
                setActionBarIcon(R.drawable.ic_menu_24dp);
                fragmentTransaction.replace(R.id.home_fragment_container, mainFragment);
                break;
            case R.id.nav_share:
                Log.d(TAG, "onNavigationItemSelected: ===>>> Drawer Share Clicked");
                break;
            case R.id.nav_send:
                Log.d(TAG, "onNavigationItemSelected: ==>>> Drawer Send Clicked");
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

    protected void setActionBarIcon(int iconResource){
        int color = Color.parseColor("#FFFFFF");
        Drawable iconDrawable = ResourcesCompat.getDrawable(getResources(), iconResource, null);
        iconDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        mToolBar.setNavigationIcon(iconDrawable);
    }

}
