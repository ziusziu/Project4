package siu.example.com.headingout.inputfragment.tabfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import siu.example.com.headingout.HeadingOutApplication;
import siu.example.com.headingout.R;
import siu.example.com.headingout.inputfragment.ApiCaller;
import siu.example.com.headingout.inputfragment.InputFragment;
import siu.example.com.headingout.inputfragment.InputTabsFragmentPagerAdapter;
import siu.example.com.headingout.inputfragment.rvadapter.InputTabFlightRVAdapter;
import siu.example.com.headingout.inputfragment.rvadapter.InputTabWeatherRVAdapter;
import siu.example.com.headingout.model.FlightTest;
import siu.example.com.headingout.model.forecast.Weather;

/**
 * Created by samsiu on 5/9/16.
 */
public class InputWeatherTabFragment extends Fragment {
    private static final String TAG = InputWeatherTabFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";

    private SwipeRefreshLayout mWeatherSwipeRefreshLayout;
//    private ProgressBar mSpinner;

    private int mPage;


    private static String mLatitude;
    private static String mLongitude;
    public static final String PLACESPREFERENCES = "placesPreferences";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private static RecyclerView mWeatherRecyclerView;
    private InputTabWeatherRVAdapter recyclerViewAdapter;

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

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        mLatitude = sharedPref.getString(LATITUDE, "Default");
        mLongitude = sharedPref.getString(LONGITUDE, "Default");
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mLatitude);
        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mLongitude);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_tab_weather_fragment, container, false);

        initViews(view);
        initRecyclerView();
        swipeWeatherRefreshListener();
        Bus bus = createBus();
        bus.register(this);

        return view;
    }


    private void initViews(View view){
        mWeatherSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.input_tab_weather_fragment_swipe_refresh_layout);
        mWeatherRecyclerView = (RecyclerView)view.findViewById(R.id.input_tab_weather_fragment_recyclerView);
   //     mSpinner = (ProgressBar)view.findViewById(R.id.input_tab_weather_fragment_progressBar);
    }

    private void initRecyclerView(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mWeatherRecyclerView.setLayoutManager(gridLayoutManager);
        mWeatherRecyclerView.setHasFixedSize(true);
    }

    private Bus createBus(){
        // Register for bus events
        HeadingOutApplication headingOutApplication = (HeadingOutApplication)getActivity().getApplication();
        Bus bus = headingOutApplication.provideBus();
        return bus;
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
                Log.d(TAG, "run: ===>>> PULLING TO REFRESH Weather====");

                forecastApiKey = getResources().getString(R.string.forecast_api_key);
                Bus bus = createBus();

                ApiCaller.getWeatherApi(bus, forecastApiKey, mLatitude, mLongitude);
                //recyclerViewSetup();
                mWeatherSwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: INPUT------WEATHER----TABFRAGMENT ===>>> resuming");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: INPUT----WEATHER--- TABFRAGMENT ===>>>> onDestroy");
    }

    @Subscribe
    public void onWeatherData(Weather weather){
        Log.d(TAG, "onWeatherData: WEATHER DATA daily Size ==>> " + weather.getDaily().getData().size());
        recyclerViewAdapter = new InputTabWeatherRVAdapter(weather, getContext());
        mWeatherRecyclerView.setAdapter(recyclerViewAdapter);
   //     mSpinner.setVisibility(View.GONE);

    }

}
