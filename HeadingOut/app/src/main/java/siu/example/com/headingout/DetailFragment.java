package siu.example.com.headingout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import siu.example.com.headingout.detailactivity.DetailTabsFragmentPagerAdapter;

/**
 * Created by samsiu on 5/4/16.
 */
public class DetailFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final String TAG = DetailFragment.class.getSimpleName();
    private static Toolbar mToolBar;
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_content, container, false);

      //  initToolBar(view);
        initViewPager(view);
        // initGoogleMaps();

        return view;
    }


//    private void initToolBar(View view){
//        mToolBar = (Toolbar)view.findViewById(R.id.detail_toolBar);
//        setSupportActionBar(mToolBar);
//        getSupportActionBar().setTitle("Detail");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }

    private void initViewPager(View view){
        mViewPager = (ViewPager)view.findViewById(R.id.detail_viewPager);
        mViewPager.setAdapter(new DetailTabsFragmentPagerAdapter(getActivity().getSupportFragmentManager()));

        mTabLayout = (TabLayout)view.findViewById(R.id.detail_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }


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
