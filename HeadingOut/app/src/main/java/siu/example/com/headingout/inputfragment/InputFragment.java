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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.List;

import siu.example.com.headingout.HeadingOutApplication;
import siu.example.com.headingout.R;
import siu.example.com.headingout.detailfragment.DetailFragment;

import siu.example.com.headingout.model.flights.Flights;

import siu.example.com.headingout.model.forecast.Weather;
import siu.example.com.headingout.model.hotels.HWAmenities;
import siu.example.com.headingout.model.hotels.HWNeighborhoods;
import siu.example.com.headingout.model.hotels.HotWireHotels;
import siu.example.com.headingout.util.FragmentUtil;
import siu.example.com.headingout.util.Utilities;

/**
 * Created by samsiu on 5/4/16.
 */
public class InputFragment extends Fragment{

    private static final String TAG = InputFragment.class.getSimpleName();
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;
    private FloatingActionButton mInputContinueFabButton;
    private static EditText mFlightEditText;
    private static String forecastApiKey;
    private static String mDestinationAirportCode;


    private static String mLatitude;
    private static String mLongitude;
    private static String mStartDay;
    private static String mStartMonth;
    private static String mStartYear;
    private static String mEndDay;
    private static String mEndMonth;
    private static String mEndYear;

    public static final String PLACESPREFERENCES = "placesPreferences";
    public static final String DESTINATIONAIRPORTCODE = "destinationAirportCode";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String STARTDAY = "startDay";
    public static final String STARTMONTH = "startMonth";
    public static final String STARTYEAR = "startYear";
    public static final String ENDDAY = "endDay";
    public static final String ENDMONTH = "endMonth";
    public static final String ENDYEAR = "endYear";

    public static InputTabsFragmentPagerAdapter mInputTabsFragmentPagerAdapter;

    MapView mMapView;
    private GoogleMap mMap;
    private GoogleMap googleMap;
    private HotWireHotels hotWireHotels;
    private Weather weather;
    private Flights flights;

    private int size;
    private Bus bus;

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

        getSharedPreferences();
        createBus();

        initGoogleMaps(view, savedInstanceState);
        googleMap = mMapView.getMap();
        onFabContinueButtonClick();

      //  makeApiCall();

        return view;

//        flightStatsApiKey = getResources().getString(R.string.flightStats_api_key);
//        flightStatsAppId = getResources().getString(R.string.flightStats_app_id);
//        String distance = "5";
//        ApiCaller.getAirportsApi(bus, googlePlacesApiKey, mLatitude, mLongitude, distance, flightStatsApiKey, flightStatsAppId, startDate, mDestinationAirportCode);
    }

    @Subscribe
    public void onHotelData(HotWireHotels hotWireHotels) {
        this.hotWireHotels = hotWireHotels;

        List<HWAmenities> hwAmenities = hotWireHotels.getMetaData().getHotelMetaData().getAmenities();
        for(HWAmenities amenities: hwAmenities){
            Log.d(TAG, "onHotelData: " + amenities.getName());
            //TODO put id into HashMap, for easy find
        }


        // Gets the Lat Longs from API Data for plots
        List<HWNeighborhoods> hwNeighborHoods = hotWireHotels.getMetaData().getHotelMetaData().getNeighborhoods();
        for(HWNeighborhoods neighborhoods : hwNeighborHoods){
            Log.d(TAG, "onHotelData: " + neighborhoods.getName());
            String centroid = neighborhoods.getCentroid();
            String[] centroidList = centroid.split("," , 2);
            double latitude = Double.parseDouble(centroidList[0]);
            double longitude = Double.parseDouble(centroidList[1]);
            Log.d(TAG, "onHotelData: " + latitude);
            Log.d(TAG, "onHotelData: " + longitude);
            int i = 0;
            String hwRefNum = hotWireHotels.getResult().get(i).getHWRefNumber();
            i++;

            plotGoogleMaps(latitude, longitude, hwRefNum);
        }
    }

    @Produce
    public HotWireHotels produceHotwireHotels() {
        return hotWireHotels;
    }

    @Subscribe
    public void onWeatherData(Weather weather){
        this.weather = weather;
    }

    @Produce
    public Weather produceWeather(){
        return weather;
    }

    @Subscribe
    public void onFlightData(Flights flights){
        this.flights = flights;
    }

    @Produce
    public Flights produceFlights(){
        return flights;
    }

    private void makeApiCall(){

        Log.d(TAG, "onCreateView: ====>>> InputFragment - makeApiCall");

        String googlePlacesApiKey = getResources().getString(R.string.google_places_key);
        String startDate = mStartYear + "-" + mStartMonth + "-" + mStartDay;

        ApiManager.getQPExpressApi(bus, googlePlacesApiKey, "SFO", mDestinationAirportCode, startDate);

        forecastApiKey = getResources().getString(R.string.forecast_api_key);
        ApiManager.getWeatherApi(bus, forecastApiKey, mLatitude, mLongitude);

        String hotwireApiKey = getResources().getString(R.string.hotwire_api_key);
        ApiManager.getHotWireApi(bus, hotwireApiKey);

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

    private void getSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        mLatitude = sharedPref.getString(LATITUDE, "Default");
        mLongitude = sharedPref.getString(LONGITUDE, "Default");
        mStartDay = sharedPref.getString(STARTDAY, "Default");
        mStartMonth = sharedPref.getString(STARTMONTH, "Default");
        mStartYear = sharedPref.getString(STARTYEAR, "Default");
        mEndDay = sharedPref.getString(ENDDAY, "Default");
        mEndMonth = sharedPref.getString(ENDMONTH, "Default");
        mEndYear = sharedPref.getString(ENDYEAR, "Default");
        mDestinationAirportCode = sharedPref.getString(DESTINATIONAIRPORTCODE, "JFK");

        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mLatitude);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mLongitude);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mStartDay);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mStartMonth);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mStartYear);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mEndDay);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mEndMonth);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mEndYear);

    }

    private void createBus(){
        HeadingOutApplication headingOutApplication = (HeadingOutApplication)getActivity().getApplication();
        bus = headingOutApplication.provideBus();
        bus.register(this);
    }

    private void initGoogleMaps(View view, Bundle savedInstanceState){
        mMapView = (MapView) view.findViewById(R.id.input_fragment_mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "INPUTFRAGMENT View Destroyed");
        bus.unregister(this);
        super.onDestroyView();
    }

    private void initFab(){
        setFabIconColor(mInputContinueFabButton, Utilities.FAB_BUTTON_COLOR);
    }

    protected static void setFabIconColor(FloatingActionButton searchFab, String fabColor){
        int color = Color.parseColor(fabColor);
        searchFab.setImageResource(R.drawable.ic_arrow_forward_24dp);
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

        mMapView.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Subscribe
    public void onSizeData(Integer size){
        this.size = size;
        Log.d(TAG, "onSizeData: === Posting Data from adapter" + size);
    }

    private void plotGoogleMaps(double latitude, double longitude, String HWRefNum){

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("HotWire Ref Num: " + HWRefNum);

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

    }

}
