package siu.example.com.headingout.inputfragment.tabfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import siu.example.com.headingout.HeadingOutApplication;
import siu.example.com.headingout.R;
import siu.example.com.headingout.inputfragment.ApiManager;
import siu.example.com.headingout.inputfragment.rvadapter.InputTabFlightRVAdapter;
import siu.example.com.headingout.model.flights.Flights;
import siu.example.com.headingout.model.flights.Leg;
import siu.example.com.headingout.model.flights.Segment;
import siu.example.com.headingout.model.flights.Slice;
import siu.example.com.headingout.model.flights.TripOption;

/**
 * Created by samsiu on 4/29/16.
 */
public class InputFlightTabFragment extends Fragment {
    private static final String TAG = InputFlightTabFragment.class.getSimpleName();

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String PLACESPREFERENCES = "placesPreferences";
    public static final String DESTINATIONAIRPORTCODE = "destinationAirportCode";

    private static int mPage;
    private static String mDestinationAirportCode;

    private SwipeRefreshLayout mFlightSwipeRefreshLayout;
    private static RecyclerView mFlightRecyclerView;
    private static InputTabFlightRVAdapter recyclerViewAdapter;
    private static TextView mOrigin;
    private static TextView mDestination;

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

        registerOttoBus();

        initViews(view);
        recyclerViewSetup();
        swipeFlightRefreshListener();

    //    registerOttoBus();

        setRecyclerViewFlightsDummyData();

        return view;
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

    private void initViews(View view){

        mFlightSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.input_tab_flight_fragment_swipe_refresh_layout);
        mFlightRecyclerView = (RecyclerView)view.findViewById(R.id.input_tab_flight_fragment_recyclerView);
        mOrigin = (TextView)view.findViewById(R.id.input_tab_flight_origin_textView);
        mDestination = (TextView)view.findViewById(R.id.input_tab_flight_destination_textView);

        // Set Color of Icons
        ImageView mainAirplaneIcon = (ImageView)view.findViewById(R.id.input_tab_flight_planeIcon_ImageView);
        int color = Color.parseColor("#BBFFFFFF");
        mainAirplaneIcon.setColorFilter(color);

        // Set Text to views
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        mDestinationAirportCode = sharedPref.getString(DESTINATIONAIRPORTCODE, "JFK");

        mOrigin.setText("SFO");
        mDestination.setText(mDestinationAirportCode);
    }

    private void recyclerViewSetup(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFlightRecyclerView.setLayoutManager(linearLayoutManager);
        mFlightRecyclerView.setHasFixedSize(true);
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
                Log.d(TAG, "run: ===>>> PULLING TO REFRESH FLIGHTS====");

                HeadingOutApplication headingOutApplication = (HeadingOutApplication) getActivity().getApplication();
                Bus bus = headingOutApplication.provideBus();
                bus.register(this);

                String origin = "BOS";
                String destination = "LAX";
                String date = "2016-07-10";

                String googlePlacesApiKey = getResources().getString(R.string.google_places_key);
                ApiManager.getQPExpressApi(bus, googlePlacesApiKey, origin, destination, date);

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


    private void setRecyclerViewFlightsDummyData(){
        Flights flights = returnFlightsDummyData();
        Log.d(TAG, "onFlightData: SUBSCRIBE  PRICING==> " + flights.getTrips().getTripOption().get(0).getPricing());

        // Loggind data Object
        List<TripOption> tripOption = flights.getTrips().getTripOption();
        for(TripOption trip : tripOption){
            Log.d(TAG, "setRecyclerViewFlightsDummyData: SALETOTAL ***** " + trip.getSaleTotal());
            List<Slice> slice = trip.getSlice();
            for(Slice slice1 : slice){
                List<Segment> segments = slice1.getSegment();
                for(Segment segment : segments){
                    Log.d(TAG, "setRecyclerViewFlightsDummyData: CARRIER ***** " + segment.getFlight().getCarrier());
                    Log.d(TAG, "setRecyclerViewFlightsDummyData: NUMBER***** " + segment.getFlight().getNumber());
                }
            }
        }


        recyclerViewAdapter = new InputTabFlightRVAdapter(flights);
        mFlightRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private Flights returnFlightsDummyData(){
        Gson gson = new Gson();
        Flights sampleFlights = gson.fromJson(loadJSONFromAsset("QPXExpressJSON.json"), Flights.class);
        Log.d(TAG, "onCreateView: ===>>> TEST DATA " + sampleFlights.getTrips().getTripOption().size());
        return sampleFlights;
    }

    public String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

