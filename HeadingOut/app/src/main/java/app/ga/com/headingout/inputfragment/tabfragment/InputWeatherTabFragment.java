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

import app.ga.com.headingout.HeadingOutApplication;
import app.ga.com.headingout.R;
import app.ga.com.headingout.inputfragment.ApiManager;
import app.ga.com.headingout.inputfragment.rvadapter.InputTabWeatherRVAdapter;
import app.ga.com.headingout.model.forecast.Weather;
import timber.log.Timber;

/**
 * Created by samsiu on 5/9/16.
 */
public class InputWeatherTabFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private SwipeRefreshLayout mWeatherSwipeRefreshLayout;
//    private ProgressBar mSpinner;

    private int mPage;


    private static String mLatitude;
    private static String mLongitude;
    private static String mDestinationAirportCode;

    public static final String PLACESPREFERENCES = "placesPreferences";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String DESTINATIONAIRPORTCODE = "destinationAirportCode";

    private static RecyclerView mWeatherRecyclerView;
    private InputTabWeatherRVAdapter recyclerViewAdapter;
    private TextView mDestinationTextView;

    private static String forecastApiKey;

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

        initViews(view);
        initRecyclerView();
        getSharedPreferences();
        swipeWeatherRefreshListener();

        registerOttoBus();

        return view;
    }


    private void initViews(View view){
        mWeatherSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.input_tab_weather_fragment_swipe_refresh_layout);
        mWeatherRecyclerView = (RecyclerView)view.findViewById(R.id.input_tab_weather_fragment_recyclerView);
        mDestinationTextView = (TextView)view.findViewById(R.id.input_tab_weather_destination_textView);
   //     mSpinner = (ProgressBar)view.findViewById(R.id.input_tab_weather_fragment_progressBar);
    }

    private void initRecyclerView(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mWeatherRecyclerView.setLayoutManager(gridLayoutManager);
        mWeatherRecyclerView.setHasFixedSize(true);
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
        mLatitude = sharedPref.getString(LATITUDE, "Default");
        mLongitude = sharedPref.getString(LONGITUDE, "Default");
        mDestinationAirportCode = sharedPref.getString(DESTINATIONAIRPORTCODE, "JFK");

        mDestinationTextView.setText(mDestinationAirportCode);
    }


    private void swipeWeatherRefreshListener(){
        mWeatherSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                Bus bus = createBus();

                ApiManager.getWeatherApi(bus, forecastApiKey, mLatitude, mLongitude);
                //recyclerViewSetup();
                mWeatherSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryLight, R.color.colorAccent, R.color.colorAccentDark);
                mWeatherSwipeRefreshLayout.setRefreshing(false);
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
        mWeatherRecyclerView.setAdapter(recyclerViewAdapter);
   //     mSpinner.setVisibility(View.GONE);

    }

}
