package app.ga.com.headingout.inputfragment.tabfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Named;

import app.ga.com.headingout.HeadingOutApplication;
import app.ga.com.headingout.R;
import app.ga.com.headingout.inputfragment.ApiManager;
import app.ga.com.headingout.inputfragment.rvadapter.InputTabWeatherRVAdapter;
import app.ga.com.headingout.model.forecast.Weather;
import app.ga.com.headingout.util.Utilities;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by samsiu on 5/9/16.
 */
public class InputWeatherTabFragment extends Fragment {

//    private ProgressBar mSpinner;
    private static String latitude;
    private static String longitude;
    private static String destinationAirportCode;
    private static String destination;
    private int page;

    private InputTabWeatherRVAdapter recyclerViewAdapter;

    @BindView(R.id.input_tab_weather_fragment_swipe_refresh_layout) SwipeRefreshLayout weatherSwipeRefreshLayout;
    @BindView(R.id.input_tab_weather_fragment_recyclerView) RecyclerView weatherRecyclerView;
    @BindView(R.id.input_tab_weather_destination_textView) TextView destinationTextView;

    private static String forecastApiKey;
    Unbinder unbinder;
    @Inject @Named("Forecast") Retrofit retrofit;
    @Inject Bus bus;

    public static InputWeatherTabFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(Utilities.ARG_PAGE, page);
        InputWeatherTabFragment fragment = new InputWeatherTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate: INPUT----WEATHER--- TABFRAGMENT ===>>>> onCreate");
        page = getArguments().getInt(Utilities.ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("onCreateView: INPUT----WEATHER--- TABFRAGMENT ===>>>> onCreateView");
        View view = inflater.inflate(R.layout.input_tab_weather_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);

        ((HeadingOutApplication)getActivity().getApplication()).getNetComponent().inject(this);

        getSharedPreferences();

        registerOttoBus();

        initRecyclerView();

        swipeWeatherRefreshListener();

        return view;
    }

    private void registerOttoBus(){
        bus.register(InputWeatherTabFragment.this);
    }

    private void initRecyclerView(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        weatherRecyclerView.setLayoutManager(gridLayoutManager);
        weatherRecyclerView.setHasFixedSize(true);

        weatherSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryLight, R.color.colorAccent, R.color.colorAccentDark);
    }

    private void getSharedPreferences(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(Utilities.PLACESPREFERENCES, Context.MODE_PRIVATE);
        latitude = sharedPref.getString(Utilities.LATITUDE, "Default");
        longitude = sharedPref.getString(Utilities.LONGITUDE, "Default");
        destinationAirportCode = sharedPref.getString(Utilities.DESTINATIONAIRPORTCODE, "JFK");
        destination = sharedPref.getString(Utilities.DESTINATION, "Unknown Destination");


        destinationTextView.setText(destination);
    }


    private void swipeWeatherRefreshListener(){
        weatherSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshWeatherContent();
            }
        });
    }


    private void refreshWeatherContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Timber.d("run: ===>>> PULLING TO REFRESH Weather==== Destination: " + destinationAirportCode);

                forecastApiKey = getResources().getString(R.string.forecast_api_key);
                ApiManager.getForecastWeather(retrofit, bus, forecastApiKey, latitude, longitude);
                
                //recyclerViewSetup();

                weatherSwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume: INPUT------WEATHER----TABFRAGMENT ===>>> resuming");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy: INPUT----WEATHER--- TABFRAGMENT ===>>>> onDestroy");
    }

    @Subscribe
    public void onWeatherData(Weather weather){
        Timber.d("onWeatherData: WEATHER DATA daily Size ==>> " + weather.getDaily().getData().size());

        recyclerViewAdapter = new InputTabWeatherRVAdapter(weather);

        if(weatherRecyclerView != null){
            Timber.d("RecyclerView is null");
            weatherRecyclerView.setAdapter(recyclerViewAdapter);
        }

        //     mSpinner.setVisibility(View.GONE);

    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Timber.d("onDestroyView: INPUT----WEATHER--- TABFRAGMENT ===>>>> onDestroyView");
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("onStart: INPUT----WEATHER--- TABFRAGMENT ===>>>> onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause: INPUT----WEATHER--- TABFRAGMENT ===>>>> onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("onStop: INPUT----WEATHER--- TABFRAGMENT ===>>>> onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.d("onDetach: INPUT----WEATHER--- TABFRAGMENT ===>>>> onDetach");
    }
}
