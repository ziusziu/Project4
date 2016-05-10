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

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import siu.example.com.headingout.R;
import siu.example.com.headingout.detailfragment.DetailFragment;
import siu.example.com.headingout.inputfragment.providers.FlightStatsService;
import siu.example.com.headingout.inputfragment.providers.ForecastService;
import siu.example.com.headingout.inputfragment.providers.GoogleHotelService;
import siu.example.com.headingout.inputfragment.providers.GoogleQPExpressService;
import siu.example.com.headingout.inputfragment.providers.HotwireService;
import siu.example.com.headingout.model.TestHotels;
import siu.example.com.headingout.model.flights.Flights;

import siu.example.com.headingout.model.airports.Airports;
import siu.example.com.headingout.model.flights.postrequest.Passengers;
import siu.example.com.headingout.model.flights.postrequest.PostSlice;
import siu.example.com.headingout.model.flights.postrequest.Request;
import siu.example.com.headingout.model.flights.postrequest.RequestJson;
import siu.example.com.headingout.model.forecast.Weather;
import siu.example.com.headingout.model.hotels.HotWireHotels;
import siu.example.com.headingout.util.FragmentUtil;
import siu.example.com.headingout.util.Utilities;

/**
 * Created by samsiu on 5/4/16.
 */
public class InputFragment extends Fragment {

    private static final String TAG = InputFragment.class.getSimpleName();
    private static Toolbar mToolBar;
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;
    private FloatingActionButton mInputContinueFabButton;
    private static EditText mFlightEditText;
    private static String forecastApiKey;
    private static String flightStatsApiKey;
    private static String flightStatsAppId;

    InputTabsFragmentPagerAdapter mInputTabsFragmentPagerAdapter;

    private static String mLatitude;
    private static String mLongitude;
    public static final String PLACESPREFERENCES = "placesLatLong";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private static final String FORECAST_API_URL = "https://api.forecast.io/forecast/";
    private static final String FLIGTHSTATS_API_URL = "https://api.flightstats.com/flex/airports/rest/v1/json/withinRadius/";
    private static final String GOOGLE_HOTELS_API_URL = "https://www.googleapis.com/travelpartner/v1.2/";
    private static final String GOOGLE_QPEXPRESS_API_URL = "https://www.googleapis.com/qpxExpress/v1/trips/";
    private static final String HOTWIRE_API_URL = "http://api.hotwire.com/v1/search/";
    Weather weather;
    Airports airports;
    HotWireHotels hotels;
    Retrofit retrofit;
    Flights flights;
    TestHotels testHotels;

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
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mLatitude);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mLongitude);


        Log.d(TAG, "onCreateView: INPUTFRAGMENT ====>>> OnCreateView");

        flightStatsApiKey = getResources().getString(R.string.flightStats_api_key);
        flightStatsAppId = getResources().getString(R.string.flightStats_app_id);
        ApiCaller.getAirportsApi(flightStatsApiKey, flightStatsAppId);

        forecastApiKey = getResources().getString(R.string.forecast_api_key);
        Weather weather = ApiCaller.getWeatherApi(forecastApiKey, mLatitude, mLongitude);


        String googlePlacesApiKey = getResources().getString(R.string.google_places_key);
        ApiCaller.getQPExpressApi(googlePlacesApiKey);

        String hotwireApiKey = getResources().getString(R.string.hotwire_api_key);
        ApiCaller.getHotWireApi(hotwireApiKey);


        return view;

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
        mViewPager.setAdapter(new InputTabsFragmentPagerAdapter(getActivity().getSupportFragmentManager()));
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

    private void onFabContinueButtonClick(){
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




}
