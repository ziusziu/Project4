package siu.example.com.headingout.inputfragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

import siu.example.com.headingout.inputfragment.rvadapter.InputTabHotelRVAdapter;
import siu.example.com.headingout.model.airports.Airport;
import siu.example.com.headingout.model.airports.AirportData;
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

    // region View Declarations
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;
    private static FloatingActionButton mInputContinueFabButton;
    private static EditText mFlightEditText;
    public static InputTabsFragmentPagerAdapter mInputTabsFragmentPagerAdapter;
    //endregion
    //region SharedPreferences Constants
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
    public static final String DESTINATION = "destination";
    //endregion
    //region SharedPreferences Variables
    private static String mLatitude;
    private static String mLongitude;
    private static String mStartDay;
    private static String mStartMonth;
    private static String mStartYear;
    private static String mEndDay;
    private static String mEndMonth;
    private static String mEndYear;
    private static String forecastApiKey;
    private static String mDestinationAirportCode;
    private static String mOriginAirportCode;
    private static String mDestination;
    //endregion
    //region API Objects
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private HotWireHotels hotWireHotels;
    private Weather weather;
    private Flights flights;
    private AirportData airport;
    //endregion

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

        createBus();

        initializeViews(view);
        initViewPager(view);
        initFab();
        initGoogleMaps(view, savedInstanceState);

        getSharedPreferences();

        onFabContinueButtonClick();

        makeApiCall();

        return view;
    }


    private void makeApiCall(){

        Log.d(TAG, "onCreateView: ====>>> InputFragment - makeApiCall");

        String googlePlacesApiKey = getResources().getString(R.string.google_places_key);
        String startDate = mStartYear + "-" + mStartMonth + "-" + mStartDay;


//        ApiManager.getQPExpressApi(bus, googlePlacesApiKey,
//                mOriginAirportCode, mDestinationAirportCode,
//                startDate);

        forecastApiKey = getResources().getString(R.string.forecast_api_key);
        ApiManager.getWeatherApi(bus, forecastApiKey, mLatitude, mLongitude);


        // API to search for airports near a specified lat long
        String flightStatsApiKey = getResources().getString(R.string.flightStats_api_key);
        String flightStatsAppId = getResources().getString(R.string.flightStats_app_id);
        ApiManager.getAirportLocation(bus, flightStatsApiKey, flightStatsAppId,
                mDestinationAirportCode, mStartYear, mStartMonth, mStartDay);

//        String distance = "5";
//        ApiManager.getAirportsApi(bus, googlePlacesApiKey, mLatitude, mLongitude, distance, flightStatsApiKey, flightStatsAppId, startDate, mDestinationAirportCode);

    }


    //--------------------------------------------------------------------------------------------
    // ApiManger Posts Data Objects to Event Bus, after response is received after Retrofit API call
    // InputFragment Subscribes to Objects
    //   - InputFragment holds api data for the case when AdapterFragments are not created
    //   - InputFragment holds api data to plot on google maps
    // InputFragment then Produces them for the Fragments inside the PagerStateAdapter RecyclerView
    //--------------------------------------------------------------------------------------------
    @Subscribe
    public void onHotelData(HotWireHotels hotWireHotels) {
        this.hotWireHotels = hotWireHotels;

        List<HWAmenities> hwAmenities = hotWireHotels.getMetaData().getHotelMetaData().getAmenities();
        for(HWAmenities amenities: hwAmenities){
            Log.d(TAG, "onHotelData: " + amenities.getName());
            //TODO put id into HashMap, for easy find
        }

        // Gets the api Lat, Longs and info for markers
        List<HWNeighborhoods> hwNeighborHoods = hotWireHotels.getMetaData().getHotelMetaData().getNeighborhoods();
        for(int position = 0; position < hwNeighborHoods.size(); position++){
            Log.d(TAG, "onHotelData: " + hwNeighborHoods.get(position).getName());

            // Pull Latitude and Longitude Data
            String centroid = hwNeighborHoods.get(position).getCentroid();
            String[] centroidList = centroid.split("," , 2);
            double latitude = Double.parseDouble(centroidList[0]);
            double longitude = Double.parseDouble(centroidList[1]);

            // Pull Hotel Reference Number Data
            String hwRefNum = hotWireHotels.getResult().get(position).getHWRefNumber();

            // Plot Marker on Google Maps
            plotGoogleMaps(latitude, longitude, hwRefNum);
        }

    }

    /**
     * Plot markers on Google Maps
     * @param latitude
     * @param longitude
     * @param HWRefNum
     */
    private void plotGoogleMaps(double latitude, double longitude, String HWRefNum){

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("HotWire Ref Num: " + HWRefNum);

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        mGoogleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

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

    @Subscribe
    public void onAirportData(AirportData airport){
        this.airport = airport;

        Log.d(TAG, "onAirportData: ===>>> OnAirportDataReturned   " + airport.getAirport().getCity());
        String destination = airport.getAirport().getCity()+","+airport.getAirport().getStateCode();
        mDestination = destination;

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(DESTINATION, destination);
        editor.apply();

        String hotwireApiKey = getResources().getString(R.string.hotwire_api_key);
        String hotwireStartDate = mStartMonth + "/" + mStartDay + "/" + mStartYear;
        String hotwireEndDate = mEndMonth + "/" + mEndDay + "/" + mEndYear;
        ApiManager.getHotWireApi(bus, hotwireApiKey, hotwireStartDate, hotwireEndDate, mDestination);
    }


    /**
     * Store FragmentPagerAdapter tab position to outState Bundle
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Utilities.POSITION, mTabLayout.getSelectedTabPosition());
    }

    /**
     * Declare layout views
     * @param view
     */
    private void initializeViews(View view){
        mFlightEditText = (EditText)view.findViewById(R.id.input_flight_editText);
        mInputContinueFabButton = (FloatingActionButton)view.findViewById(R.id.input_continue_fab);
    }

    /**
     * Initialize FragmentStatePagerAdapter
     * @param view
     */
    private void initViewPager(View view){
        mViewPager = (ViewPager)view.findViewById(R.id.input_viewPager);
        mInputTabsFragmentPagerAdapter = new InputTabsFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mInputTabsFragmentPagerAdapter);
        mTabLayout = (TabLayout)view.findViewById(R.id.input_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
        //mTabLayout.setScrollbarFadingEnabled(true);

        // adapter.setMyValue()
    }

    /**
     * Get the Shared Preferences
     */
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
        mOriginAirportCode = "SFO";

        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mStartYear);

    }

    /**
     * Create an Otto event bus
     */
    private void createBus(){
        HeadingOutApplication headingOutApplication = (HeadingOutApplication)getActivity().getApplication();
        bus = headingOutApplication.provideBus();
        bus.register(this);
    }

    /**
     * Initialize Google Maps
     * @param view
     * @param savedInstanceState
     */
    private void initGoogleMaps(View view, Bundle savedInstanceState){
        mMapView = (MapView) view.findViewById(R.id.input_fragment_mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        mGoogleMap = mMapView.getMap();

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

    /**
     * Initizlize FAB Button
     */
    private void initFab(){
        setFabIconColor(mInputContinueFabButton, Utilities.FAB_BUTTON_COLOR);
    }

    /**
     * Set the color of FAB Button
     * @param searchFab
     * @param fabColor
     */
    protected static void setFabIconColor(FloatingActionButton searchFab, String fabColor) {
        int color = Color.parseColor(fabColor);
        //searchFab.setImageResource(R.drawable.ic_arrow_forward_24dp);
        searchFab.setImageResource(R.drawable.ic_share_24dp);
        searchFab.setColorFilter(color);
    }

    /**
     * Switch to Detail Fragment when FAB button clicked
     */
    private void onFabContinueButtonClick() {
        mInputContinueFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = "Description";
                String title = "Title";
                String location = "Location";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, description);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, title + ": " + location);

                v.getContext().startActivity(Intent.createChooser(intent, "Share to"));

//                DetailFragment detailFragment = new DetailFragment();
//                detailFragment.setArguments(InputTabHotelRVAdapter.hotelBundle);
//
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.home_fragment_container, detailFragment).addToBackStack(null);
//                fragmentTransaction.commit();
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
        Log.d(TAG, "onDestroy: ==>> InputFragment OnDestroy");
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ==>> InputFragment OnPause");
        super.onPause();
        mMapView.onPause();
    }

    //TODO REMOVE
    @Subscribe
    public void onSizeData(Integer size){
        this.size = size;
        Log.d(TAG, "onSizeData: === Posting Data from adapter" + size);
    }
}
