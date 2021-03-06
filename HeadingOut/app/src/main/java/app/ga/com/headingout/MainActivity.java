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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import app.ga.com.headingout.inputfragment.InputFragment;
import app.ga.com.headingout.mainfragment.MainFragment;
import app.ga.com.headingout.util.FragmentUtil;
import app.ga.com.headingout.util.Utilities;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentUtil {

    private static final String MAIN_FRAGMENT = "MainFragment";
    private static final String INPUT_FRAGMENT = "InputFragment";
    private static final String DETAIL_FRAGMENT = "DetailFragment";

    private static ActionBarDrawerToggle drawerToggle;
    private static FragmentManager fragmentManager;
    private static MainFragment mainFragment;
    private static InputFragment inputFragment;
    private static ActionBar actionBar;

    @BindView(R.id.home_nav_view) NavigationView navigationView;
    @BindView(R.id.home_drawer_layout) DrawerLayout drawer;
    @BindView(R.id.home_toolBar) Toolbar toolbar;

    //TODO Butterknife icons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        ButterKnife.bind(this);

        Utilities.hideKeyboard(this);

        initActionBar();
        initNavDrawer();

        loadMainFragment();

        Timber.d("Number of Cores: " + Utilities.NUMBER_OF_CORES);
    }

    private void initActionBar(){
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(MAIN_FRAGMENT);
    }

    /**
     * Initialize Navigation Drawer and ActionBarDrawerToggle for open drawers
     */
    private void initNavDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(drawerToggle);
        drawer.removeDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Load MainFragment into container when Activity is Created
     */
    private void loadMainFragment() {
        Timber.d("loadMainFragment: ===>>> Loading MainFragment in MainActivity");
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
        Timber.d("setActionBarIcon: ===>> MainActivity changing ActionBar Icon");
        int color = Color.parseColor("#FFFFFF");
        Drawable iconDrawable = ResourcesCompat.getDrawable(getResources(), iconResource, null);
        iconDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        toolbar.setNavigationIcon(iconDrawable);
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
        Timber.d("onBackPressed: ===>> MainActivity BackPressed");

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_fragment_container);
        String fragmentName = fragment.getClass().getSimpleName();
        Timber.d("onBackPressed: ====>>>" + fragment.getClass().getSimpleName());

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (fragmentName) {
                case MAIN_FRAGMENT:
                    Timber.d("onBackPressed: ===>>> On MainFragment when back pressed, leave app");
                    super.onBackPressed();
                case INPUT_FRAGMENT:
                    mainFragment = new MainFragment();
                    Timber.d("onBackPressed: ====>>> On InputFragment when back pressed, go to Main");
                    fragmentTransaction.replace(R.id.home_fragment_container, mainFragment);
                    break;
                case DETAIL_FRAGMENT:
                    inputFragment = new InputFragment();
                    Timber.d("onBackPressed: ===>>> On DetailFragment when back pressed, go to Input");
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
                Timber.d("onNavigationItemSelected: ===>>> Drawer Home Clicked");
                setActionBarIcon(R.drawable.ic_menu_24dp);
                fragmentTransaction.replace(R.id.home_fragment_container, mainFragment);
                break;
            case R.id.nav_saved:
                Timber.d("onNavigationItemSelected: ===>>> Drawer saved Clicked");
                break;
            case R.id.nav_send:
                Timber.d("onNavigationItemSelected: ===>>> Drawer Send Clicked");
                break;
            default:
                setActionBarIcon(R.drawable.ic_menu_24dp);
                break;
        }
        fragmentTransaction.commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//TODO Currently not using DetailFragment
    /**
     * FragmentUtil is an interface that uses setFragmentToolBar() to return
     * a FragmentName when the fragment goes to OnResume(). When the Fragment is
     * resumed, the actionBar title and icons are set.
     *
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
                break;
            case INPUT_FRAGMENT:
                actionBar.setTitle("Results");
                setActionBarIcon(R.drawable.ic_menu_24dp);
                break;
            case DETAIL_FRAGMENT:
                drawerToggle.setDrawerIndicatorEnabled(false);
                setActionBarIcon(R.drawable.ic_arrow_back_24dp);
                actionBar.setTitle(fragmentName);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO Fix MainFragment Icon becomes back arrow when Home clicked in InputFragment
                        Timber.d("onClick: ActionBarArrow was clicked");
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

    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

}
