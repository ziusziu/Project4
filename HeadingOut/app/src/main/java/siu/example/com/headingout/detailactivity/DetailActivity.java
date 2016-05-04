package siu.example.com.headingout.detailactivity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import okhttp3.internal.Util;
import siu.example.com.headingout.BaseActivity;
import siu.example.com.headingout.R;
import siu.example.com.headingout.detailactivity.DetailTabsFragmentPagerAdapter;
import siu.example.com.headingout.util.Utilities;

public class DetailActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static Toolbar mToolBar;
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        initToolBar();
        initViewPager();
       // initGoogleMaps();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail;
    }

    private void initToolBar(){
        mToolBar = (Toolbar)findViewById(R.id.detail_toolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViewPager(){
        mViewPager = (ViewPager)findViewById(R.id.detail_viewPager);
        mViewPager.setAdapter(new DetailTabsFragmentPagerAdapter(getSupportFragmentManager()));

        mTabLayout = (TabLayout)findViewById(R.id.detail_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
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

//    private void initGoogleMaps(){
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        float zoomLevel = 13;
        double latitude = 37.785049;
        double longitude = -122.396387;

        // Add a marker to airport and move the camera
        LatLng airport = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(airport).title("Testing"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(airport, zoomLevel));
    }

}
