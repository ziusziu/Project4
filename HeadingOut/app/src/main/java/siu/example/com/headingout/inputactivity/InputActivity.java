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
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        initializeViews();
        initViewPager();
        initFab();

        onFabContinueButtonClick();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_input;
    }

    @Override
    protected int getDrawerLayoutResource() {
        return R.id.input_drawer_layout;
    }

    @Override
    protected int getToolBarResource() {
        return R.id.input_toolBar;
    }

    @Override
    protected int getNavViewResource() {
        return R.id.input_nav_view;
    }

    private void initializeViews(){
        mFlightEditText = (EditText)findViewById(R.id.input_flight_editText);
        mInputContinueFabButton = (FloatingActionButton)findViewById(R.id.input_continue_fab);
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
