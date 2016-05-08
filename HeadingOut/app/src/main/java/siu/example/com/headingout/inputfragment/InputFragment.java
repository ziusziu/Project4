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
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import siu.example.com.headingout.R;
import siu.example.com.headingout.detailfragment.DetailFragment;
import siu.example.com.headingout.model.flights.Flights;
import siu.example.com.headingout.model.Hotels;
import siu.example.com.headingout.model.airports.Airports;
import siu.example.com.headingout.model.flights.postrequest.Passengers;
import siu.example.com.headingout.model.flights.postrequest.PostSlice;
import siu.example.com.headingout.model.flights.postrequest.Request;
import siu.example.com.headingout.model.flights.postrequest.RequestJson;
import siu.example.com.headingout.model.forecast.Weather;
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

    private static String mLatitude;
    private static String mLongitude;
    public static final String PLACESPREFERENCES = "placesLatLong";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private static final String FORECAST_API_URL = "https://api.forecast.io/forecast/";
    private static final String FLIGTHSTATS_API_URL = "https://api.flightstats.com/flex/airports/rest/v1/json/withinRadius/";
    private static final String GOOGLE_HOTELS_API_URL = "https://www.googleapis.com/travelpartner/v1.2/";
    private static final String GOOGLE_QPEXPRESS_API_URL = "https://www.googleapis.com/qpxExpress/v1/trips/";
    Weather weather;
    Airports airports;
    Hotels hotels;
    Retrofit retrofit;
    Flights flights;

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



        //getAirportsApi();
        //getWeatherApi();



        String googlePlacesApiKey = getResources().getString(R.string.google_places_key);
        Passengers passengers = new Passengers(1,0,0,0,0);
        PostSlice postSlice = new PostSlice("BOS", "LAX", "2016-05-10");
        ArrayList<PostSlice> slice = new ArrayList<>();
        slice.add(postSlice);
        Request request = new Request(slice, passengers, 20, false);
        RequestJson requestJson = new RequestJson(request);


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(GOOGLE_QPEXPRESS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        GoogleQPExpressService service = retrofit.create(GoogleQPExpressService.class);
        Call<Flights> call = service.getFlights(googlePlacesApiKey, requestJson);
        call.enqueue(new Callback<Flights>() {
            @Override
            public void onResponse(Call<Flights> call, Response<Flights> response) {
                if (response.isSuccessful()) {
                    flights = response.body();
                    Log.d(TAG, "onResponse: ===>>>" + flights.getTrips().getTripOption().get(0).getSlice().get(0).getSegment().get(0).getCabin());
                    Log.d(TAG, "onResponse: ====>>> RESPONSE BODY" + response.body().toString());


                } else {
                    Log.d(TAG, "onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                }
            }

            @Override
            public void onFailure(Call<Flights> call, Throwable t) {
                Log.d(TAG, "onFailure: onFailure UNSUCCESSFUL");
            }
        });




        return view;

    }

    private void getGoogleHotleApi(){


        String queryType = "type";


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(GOOGLE_HOTELS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        GoogleHotelService service = retrofit.create(GoogleHotelService.class);
        Call<Hotels> call = service.getHotels();
        call.enqueue(new Callback<Hotels>() {
            @Override
            public void onResponse(Call<Hotels> call, Response<Hotels> response) {
                if (response.isSuccessful()) {
                    hotels = response.body();
                    Log.d(TAG, "onResponse: ===>>>" + hotels.getKind());

                    Log.d(TAG, "onResponse: ====>>> RESPONSE BODY" + response.body().toString());


                } else {
                    Log.d(TAG, "onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                }
            }

            @Override
            public void onFailure(Call<Hotels> call, Throwable t) {
                Log.d(TAG, "onFailure: onFailure UNSUCCESSFUL");
            }
        });


    }



    private void getAirportsApi(){


        flightStatsApiKey = getResources().getString(R.string.flightStats_api_key);
        flightStatsAppId = getResources().getString(R.string.flightStats_app_id);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        mLatitude = sharedPref.getString(LATITUDE, "Default");
        mLongitude = sharedPref.getString(LONGITUDE, "Default");
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mLatitude);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mLongitude);
        String distance = "50";

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(FLIGTHSTATS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        FlightStatsService service = retrofit.create(FlightStatsService.class);
        Call<Airports> call = service.getAirports(mLongitude, mLatitude, distance, flightStatsAppId, flightStatsApiKey);
        call.enqueue(new Callback<Airports>() {
            @Override
            public void onResponse(Call<Airports> call, Response<Airports> response) {
                if (response.isSuccessful()) {
                    airports = response.body();

                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + airports.getAirport().get(0).getName());
                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + airports.getAirport().get(0).getRegionName());
                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + airports.getAirport().get(0).getCityCode());
                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + airports.getAirport().get(0).getCountryName());


                } else {
                    Log.d(TAG, "onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                }
            }

            @Override
            public void onFailure(Call<Airports> call, Throwable t) {
                Log.d(TAG, "onFailure: onFailure UNSUCCESSFUL");
            }
        });


    }


    private void getWeatherApi(){
        forecastApiKey = getResources().getString(R.string.forecast_api_key);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        mLatitude = sharedPref.getString(LATITUDE, "Default");
        mLongitude = sharedPref.getString(LONGITUDE, "Default");
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mLatitude);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mLongitude);

        String latLong = mLatitude+","+mLongitude;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(FORECAST_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ForecastService service = retrofit.create(ForecastService.class);
        Call<Weather> call = service.getWeather(forecastApiKey, latLong);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    weather = response.body();

                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + weather.getTimezone());
                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + weather.getHourly().getData().get(0).getApparentTemperature());
                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + weather.getDaily().getData().get(0).getOzone());
                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + weather.getMinutely().getData().get(0).getPrecipProbability());

                } else {
                    Log.d(TAG, "onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.d(TAG, "onFailure: onFailure UNSUCCESSFUL");
            }
        });
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
                fragmentTransaction.replace(R.id.home_fragment_container, detailFragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentUtil fragInfo = (FragmentUtil)getActivity();
        fragInfo.setFragmentToolBar(InputFragment.class.getSimpleName());
    }

}
