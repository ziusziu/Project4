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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import siu.example.com.headingout.HeadingOutApplication;
import siu.example.com.headingout.R;
import siu.example.com.headingout.inputfragment.ApiCaller;
import siu.example.com.headingout.inputfragment.rvadapter.InputTabFlightRVAdapter;
import siu.example.com.headingout.model.FlightTest;
import siu.example.com.headingout.model.flights.Flights;
import siu.example.com.headingout.model.flights.Trip;

/**
 * Created by samsiu on 4/29/16.
 */
public class InputFlightTabFragment extends Fragment {
    private static final String TAG = InputFlightTabFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    private SwipeRefreshLayout mFlightSwipeRefreshLayout;

    private int mPage;
    private static RecyclerView mFlightRecyclerView;
    private InputTabFlightRVAdapter recyclerViewAdapter;

    public static InputFlightTabFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        InputFlightTabFragment fragment = new InputFlightTabFragment();
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
        View view = inflater.inflate(R.layout.input_tab_flight_fragment, container, false);
        EditText flightEditText = (EditText) view.findViewById(R.id.input_flight_editText);
        flightEditText.setText("Fragment #" + mPage);
        flightEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: ====>>> Inside edit text");
                return false;
            }



        });

        mFlightSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.input_tab_flight_fragment_swipe_refresh_layout);


        mFlightRecyclerView = (RecyclerView)view.findViewById(R.id.input_tab_flight_fragment_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFlightRecyclerView.setLayoutManager(linearLayoutManager);
        mFlightRecyclerView.setHasFixedSize(true);

        //recyclerViewSetup();

        swipeFlightRefreshListener();


        // Register for bus events
        HeadingOutApplication headingOutApplication = (HeadingOutApplication)getActivity().getApplication();
        Bus bus = headingOutApplication.provideBus();
        bus.register(this);


        return view;
    }

    private void recyclerViewSetup(){
        List<FlightTest> flightList = new ArrayList<>();

        // Dummy Data
        FlightTest flight = new FlightTest("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        FlightTest flight1 = new FlightTest("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        FlightTest flight2 = new FlightTest("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        FlightTest flight3 = new FlightTest("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        FlightTest flight4 = new FlightTest("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        flightList.add(flight);
        flightList.add(flight1);
        flightList.add(flight2);
        flightList.add(flight3);
        flightList.add(flight4);

//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        mFlightRecyclerView.setLayoutManager(linearLayoutManager);
//        mFlightRecyclerView.setHasFixedSize(true);
//        InputTabFlightRVAdapter recyclerViewAdapter = new InputTabFlightRVAdapter(flightList);
//        mFlightRecyclerView.setAdapter(recyclerViewAdapter);

    }

    private void swipeFlightRefreshListener(){
        mFlightSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFlightContent();
            }
        });
    }


    private void refreshFlightContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ===>>> PULLING TO REFRESH FLIGTHS====");

                HeadingOutApplication headingOutApplication = (HeadingOutApplication)getActivity().getApplication();
                Bus bus = headingOutApplication.provideBus();
                bus.register(this);

                String origin = "BOS";
                String destination = "LAX";
                String date = "2016-07-10";

                String googlePlacesApiKey = getResources().getString(R.string.google_places_key);
                ApiCaller.getQPExpressApi(bus, googlePlacesApiKey, origin, destination, date);

                recyclerViewSetup();
                mFlightSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimayLight, R.color.colorAccent, R.color.colorAccentDark);
                mFlightSwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: INPUT----FLIGHT-----TABFRAGMENT ===>>> resuming");
    }

    @Subscribe
    public void onFlightData(Flights flights){
        Log.d(TAG, "onFlightData: SUBSCRIBE  PRICING==> " + flights.getTrips().getTripOption().get(0).getPricing());

        recyclerViewAdapter = new InputTabFlightRVAdapter(flights);
        mFlightRecyclerView.setAdapter(recyclerViewAdapter);
    }

}

