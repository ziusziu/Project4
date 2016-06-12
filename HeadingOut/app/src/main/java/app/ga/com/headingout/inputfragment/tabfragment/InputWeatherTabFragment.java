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
import app.ga.com.headingout.inputfragment.providers.ForecastService;
import app.ga.com.headingout.inputfragment.rvadapter.InputTabWeatherRVAdapter;
import app.ga.com.headingout.model.forecast.Weather;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by samsiu on 5/9/16.
 */
public class InputWeatherTabFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    
//    private ProgressBar mSpinner;

    private int mPage;


    private static String latitude;
    private static String longitude;
    private static String destinationAirportCode;

    public static final String PLACESPREFERENCES = "placesPreferences";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String DESTINATIONAIRPORTCODE = "destinationAirportCode";
    
    private InputTabWeatherRVAdapter recyclerViewAdapter;
    
    private static String forecastApiKey;
    
    @BindView(R.id.input_tab_weather_fragment_swipe_refresh_layout) SwipeRefreshLayout weatherSwipeRefreshLayout;
    @BindView(R.id.input_tab_weather_fragment_recyclerView) RecyclerView weatherRecyclerView;
    @BindView(R.id.input_tab_weather_destination_textView) TextView destinationTextView;
    
    
    private Weather weather;
    Unbinder unbinder;
    @Inject @Named("Forecast") Retrofit retrofit;

    public static InputWeatherTabFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        InputWeatherTabFragment fragment = new InputWeatherTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_tab_weather_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);


        ((HeadingOutApplication)getActivity().getApplication()).getNetComponent().inject(this);

        initViews(view);
        initRecyclerView();
        getSharedPreferences();
        swipeWeatherRefreshListener();

        registerOttoBus();

        return view;
    }


    private void initViews(View view){
        
   //     mSpinner = (ProgressBar)view.findViewById(R.id.input_tab_weather_fragment_progressBar);
    }

    private void initRecyclerView(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        weatherRecyclerView.setLayoutManager(gridLayoutManager);
        weatherRecyclerView.setHasFixedSize(true);
    }

    private void registerOttoBus(){
        Bus bus = createBus();
        bus.register(this);
    }

    private Bus createBus(){
        // Register for bus events
        HeadingOutApplication headingOutApplication = (HeadingOutApplication)getActivity().getApplication();
        Bus bus = headingOutApplication.provideBus();
        return bus;
    }

    private void getSharedPreferences(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        latitude = sharedPref.getString(LATITUDE, "Default");
        longitude = sharedPref.getString(LONGITUDE, "Default");
        destinationAirportCode = sharedPref.getString(DESTINATIONAIRPORTCODE, "JFK");

        destinationTextView.setText(destinationAirportCode);
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
                Timber.d("run: ===>>> PULLING TO REFRESH Weather====");

                forecastApiKey = getResources().getString(R.string.forecast_api_key);
                final Bus bus = createBus();

                //ApiManager.getWeatherApi(bus, forecastApiKey, latitude, longitude);

                String latLong = latitude+","+longitude;

                ForecastService service = retrofit.create(ForecastService.class);
                Call<Weather> call = service.getWeather(forecastApiKey, latLong);
                call.enqueue(new Callback<Weather>() {
                    @Override
                    public void onResponse(Call<Weather> call, Response<Weather> response) {
                        if (response.isSuccessful()) {
                            weather = response.body();

                            bus.post(weather);
                            Timber.d("onResponse: RESPONSE SUCCESSFUL *****  " + weather.getTimezone());
                            Timber.d("onResponse: RESPONSE SUCCESSFUL *****  " + weather.getHourly().getData().get(0).getApparentTemperature());
                            Timber.d("onResponse: RESPONSE SUCCESSFUL *****  " + weather.getDaily().getData().get(0).getOzone());

                        } else {
                            Timber.d("onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Weather> call, Throwable t) {
                        Timber.d("onFailure: onFailure UNSUCCESSFUL");
                    }
                });

                
                //recyclerViewSetup();
                weatherSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryLight, R.color.colorAccent, R.color.colorAccentDark);
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
        recyclerViewAdapter = new InputTabWeatherRVAdapter(weather, getContext());
        weatherRecyclerView.setAdapter(recyclerViewAdapter);
   //     mSpinner.setVisibility(View.GONE);

    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
