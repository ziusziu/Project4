package siu.example.com.headingout.inputactivity;


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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;


import siu.example.com.headingout.BaseActivity;
import siu.example.com.headingout.detailactivity.DetailActivity;
import siu.example.com.headingout.R;
import siu.example.com.headingout.mainactivity.MainActivity;
import siu.example.com.headingout.util.Utilities;

public class InputActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = InputActivity.class.getSimpleName();
    private static Toolbar mToolBar;
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;
    private FloatingActionButton mInputContinueFabButton;
    private static EditText mFlightEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        Utilities.hideKeyboard(InputActivity.this);

        initializeViews();
        initToolBar();
        initNavDrawer();
        initViewPager();
        initFab();

        onFabContinueButtonClick();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_input;
    }

    private void initializeViews(){
        mFlightEditText = (EditText)findViewById(R.id.input_flight_editText);
        mToolBar = (Toolbar)findViewById(R.id.input_toolBar);
        mInputContinueFabButton = (FloatingActionButton)findViewById(R.id.input_continue_fab);
    }

    private void initToolBar(){
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Input");
    }

    private void initViewPager(){
        mViewPager = (ViewPager)findViewById(R.id.input_viewPager);
        mViewPager.setAdapter(new InputTabsFragmentPagerAdapter(getSupportFragmentManager()));
        mTabLayout = (TabLayout)findViewById(R.id.input_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initFab(){
        setFabIconColor(mInputContinueFabButton, Utilities.FAB_BUTTON_COLOR);
    }


    // Save and restore last known tab position
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Utilities.POSITION, mTabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mViewPager.setCurrentItem(savedInstanceState.getInt(Utilities.POSITION));
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
            Intent home = new Intent(InputActivity.this, MainActivity.class); //
            startActivity(home);
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
                        .putString(Utilities.SHARED_PREFERENCES_FLIGHTTERM, searchTerms[0])
                        .apply();

                Intent mFlightResultsIntent = new Intent(InputActivity.this,DetailActivity.class);
                mFlightResultsIntent.putExtra(Utilities.INTENT_FLIGHT_KEY, searchTerms);
                startActivity(mFlightResultsIntent);
            }
        });
    }

}
