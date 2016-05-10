package siu.example.com.headingout.inputfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import siu.example.com.headingout.R;
import siu.example.com.headingout.detailfragment.DetailFragment;

import siu.example.com.headingout.util.FragmentUtil;
import siu.example.com.headingout.util.Utilities;

/**
 * Created by samsiu on 5/4/16.
 */
public class InputFragment extends Fragment implements OnMapReadyCallback{

    private static final String TAG = InputFragment.class.getSimpleName();
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;
    private FloatingActionButton mInputContinueFabButton;
    private static EditText mFlightEditText;
    private static String forecastApiKey;
    private static String flightStatsApiKey;
    private static String flightStatsAppId;


    private static String mLatitude;
    private static String mLongitude;
    private static String mStartDay;
    private static String mStartMonth;
    private static String mStartYear;
    private static String mEndDay;
    private static String mEndMonth;
    private static String mEndYear;

    public static final String PLACESPREFERENCES = "placesPreferences";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String STARTDAY = "startDay";
    public static final String STARTMONTH = "startMonth";
    public static final String STARTYEAR = "startYear";
    public static final String ENDDAY = "endDay";
    public static final String ENDMONTH = "endMonth";
    public static final String ENDYEAR = "endYear";

    public static InputTabsFragmentPagerAdapter mInputTabsFragmentPagerAdapter;

    private GoogleMap mMap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null){
            int tabPagePosition = savedInstanceState.getInt(Utilities.POSITION);
            mViewPager.setCurrentItem(tabPagePosition);
        }
        View view = inflater.inflate(R.layout.input_content, container, false);
        initializeViews(view);
        initViewPager(view);
        initFab();
        onFabContinueButtonClick();

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        mLatitude = sharedPref.getString(LATITUDE, "Default");
        mLongitude = sharedPref.getString(LONGITUDE, "Default");
        mStartDay = sharedPref.getString(STARTDAY, "Default");
        mStartMonth = sharedPref.getString(STARTMONTH, "Default");
        mStartYear = sharedPref.getString(STARTYEAR, "Default");
        mEndDay = sharedPref.getString(ENDDAY, "Default");
        mEndMonth = sharedPref.getString(ENDMONTH, "Default");
        mEndYear = sharedPref.getString(ENDYEAR, "Default");

        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mLatitude);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mLongitude);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mStartDay);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mStartMonth);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mStartYear);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mEndDay);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mEndMonth);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mEndYear);

        forecastApiKey = getResources().getString(R.string.forecast_api_key);
        ApiCaller.getWeatherApi(forecastApiKey, mLatitude, mLongitude, mInputTabsFragmentPagerAdapter);
        //makeApiCall();

        return view;

    }

    private void makeApiCall(){

        Log.d(TAG, "onCreateView: INPUTFRAGMENT ====>>> makeApiCall");

        flightStatsApiKey = getResources().getString(R.string.flightStats_api_key);
        flightStatsAppId = getResources().getString(R.string.flightStats_app_id);
        ApiCaller.getAirportsApi(flightStatsApiKey, flightStatsAppId);


        forecastApiKey = getResources().getString(R.string.forecast_api_key);
        ApiCaller.getWeatherApi(forecastApiKey, mLatitude, mLongitude, mInputTabsFragmentPagerAdapter);

        String googlePlacesApiKey = getResources().getString(R.string.google_places_key);
        ApiCaller.getQPExpressApi(googlePlacesApiKey);

        String hotwireApiKey = getResources().getString(R.string.hotwire_api_key);
        ApiCaller.getHotWireApi(hotwireApiKey);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Utilities.POSITION, mTabLayout.getSelectedTabPosition());
    }

    private void initializeViews(View view){
        mFlightEditText = (EditText)view.findViewById(R.id.input_flight_editText);
        mInputContinueFabButton = (FloatingActionButton)view.findViewById(R.id.input_continue_fab);
    }

    private void initViewPager(View view){
        mViewPager = (ViewPager)view.findViewById(R.id.input_viewPager);
        mInputTabsFragmentPagerAdapter = new InputTabsFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mInputTabsFragmentPagerAdapter);
        mTabLayout = (TabLayout)view.findViewById(R.id.input_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
        //mTabLayout.setScrollbarFadingEnabled(true);

        // adapter.setMyValue()
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "INPUTFRAGMENT View Destroyed");
        super.onDestroyView();
    }

    private void initFab(){
        setFabIconColor(mInputContinueFabButton, Utilities.FAB_BUTTON_COLOR);
    }

    protected static void setFabIconColor(FloatingActionButton searchFab, String fabColor){
        int color = Color.parseColor(fabColor);
        searchFab.setImageResource(R.drawable.icon_search);
        searchFab.setColorFilter(color);
    }

    private void onFabContinueButtonClick() {
        mInputContinueFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] searchTerms = {
                        "Hello"//mFlightEditText.getText().toString(),
                };

                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(Utilities.SHARED_PREFERENCES_FLIGHTTERM, searchTerms[0])
                        .apply();

                DetailFragment detailFragment = new DetailFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_fragment_container, detailFragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentUtil fragInfo = (FragmentUtil)getActivity();
        fragInfo.setFragmentToolBar(InputFragment.class.getSimpleName());


        Log.d(TAG, "onResume: ===>>>>  InputFragment On RESUME");

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
