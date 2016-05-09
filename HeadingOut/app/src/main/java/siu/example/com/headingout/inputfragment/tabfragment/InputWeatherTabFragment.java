package siu.example.com.headingout.inputfragment.tabfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import siu.example.com.headingout.R;
import siu.example.com.headingout.inputfragment.rvadapter.InputTabFlightRVAdapter;
import siu.example.com.headingout.inputfragment.rvadapter.InputTabWeatherRVAdapter;
import siu.example.com.headingout.model.FlightTest;

/**
 * Created by samsiu on 5/9/16.
 */
public class InputWeatherTabFragment extends Fragment {
    private static final String TAG = InputWeatherTabFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";

    private SwipeRefreshLayout mWeatherSwipeRefreshLayout;

    private int mPage;
    private static RecyclerView mWeatherRecyclerView;

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
        mWeatherRecyclerView = (RecyclerView)view.findViewById(R.id.input_tab_weather_fragment_recyclerView);

        mWeatherSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.input_tab_weather_fragment_swipe_refresh_layout);

        recyclerViewSetup();


        swipeWeatherRefreshListener();


        return view;
    }

    private void recyclerViewSetup(){
        List<FlightTest> flightList = new ArrayList<>();

        // Dummy Data
        FlightTest flight = new FlightTest("Weather", "Weather", "Weather" , "Weather", "Weather", "Weather");
        FlightTest flight1 = new FlightTest("Weather", "Weather", "Weather" , "Weather", "Weather", "Weather");
        FlightTest flight2 = new FlightTest("Weather", "Weather", "Weather" , "Weather", "Weather", "Weather");
        FlightTest flight3 = new FlightTest("Weather", "Weather", "Weather" , "Weather", "Weather", "Weather");
        FlightTest flight4 = new FlightTest("Weather", "Weather", "Weather" , "Weather", "Weather", "Weather");
        flightList.add(flight);
        flightList.add(flight1);
        flightList.add(flight2);
        flightList.add(flight3);
        flightList.add(flight4);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mWeatherRecyclerView.setLayoutManager(linearLayoutManager);
        mWeatherRecyclerView.setHasFixedSize(true);
        InputTabWeatherRVAdapter recyclerViewAdapter = new InputTabWeatherRVAdapter(flightList);
        mWeatherRecyclerView.setAdapter(recyclerViewAdapter);

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
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Log.d(TAG, "run: ===>>> PULLING TO REFRESH Hotels====");
                recyclerViewSetup();
                mWeatherSwipeRefreshLayout.setRefreshing(false);
            }
        },0);
    }


}
