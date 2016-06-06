package app.ga.com.headingout.inputfragment.tabfragment;

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

import app.ga.com.headingout.HeadingOutApplication;
import app.ga.com.headingout.R;
import app.ga.com.headingout.inputfragment.ApiManager;
import app.ga.com.headingout.inputfragment.rvadapter.InputTabFlightRVAdapter;
import app.ga.com.headingout.model.flights.Flights;
import app.ga.com.headingout.model.flights.Segment;
import app.ga.com.headingout.model.flights.Slice;
import app.ga.com.headingout.model.flights.TripOption;
import timber.log.Timber;

/**
 * Created by samsiu on 4/29/16.
 */
public class InputFlightTabFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String PLACESPREFERENCES = "placesPreferences";
    public static final String DESTINATIONAIRPORTCODE = "destinationAirportCode";
    public static final String ORIGINAIRPORTCODE = "originAirportCode";
    public static final String ENDDAY = "endDay";
    public static final String ENDMONTH = "endMonth";
    public static final String ENDYEAR = "endYear";

    private static int mPage;
    private static String mDestinationAirportCode;
    private static String mOriginAirportCode;
    private static String mEndDay;
    private static String mEndMonth;
    private static String mEndYear;

    private SwipeRefreshLayout mFlightSwipeRefreshLayout;
    private static RecyclerView mFlightRecyclerView;
    private static InputTabFlightRVAdapter recyclerViewAdapter;
    private static TextView mOriginTextView;
    private static TextView mDestinationTextView;

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

        initViews(view);
        getSharedPreferences();

        registerOttoBus();


        recyclerViewSetup();
        swipeFlightRefreshListener();


        //setRecyclerViewFlightsDummyData();

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
        mOriginTextView = (TextView)view.findViewById(R.id.input_tab_flight_origin_textView);
        mDestinationTextView = (TextView)view.findViewById(R.id.input_tab_flight_destination_textView);

        // Set Color of Icons
        ImageView mainAirplaneIcon = (ImageView)view.findViewById(R.id.input_tab_flight_planeIcon_ImageView);
        int color = Color.parseColor("#BBFFFFFF");
        mainAirplaneIcon.setColorFilter(color);

        getSharedPreferences();

        Timber.d("initViews: mOriginAirportCode " + mOriginAirportCode);

        mOriginTextView.setText(mOriginAirportCode);
        mDestinationTextView.setText(mDestinationAirportCode);
    }

    private void getSharedPreferences(){
        // Set Text to views
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        mDestinationAirportCode = sharedPref.getString(DESTINATIONAIRPORTCODE, "JFK");
        mOriginAirportCode = sharedPref.getString(ORIGINAIRPORTCODE, "JFK");
        mEndDay = sharedPref.getString(ENDDAY, "Default");
        mEndMonth = sharedPref.getString(ENDMONTH, "Default");
        mEndYear = sharedPref.getString(ENDYEAR, "Default");
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
                Timber.d("run: ===>>> PULLING TO REFRESH FLIGHTS====");

                HeadingOutApplication headingOutApplication = (HeadingOutApplication) getActivity().getApplication();
                Bus bus = headingOutApplication.provideBus();
                bus.register(this);

                String date = mEndYear + "-" + mEndMonth + "-" + mEndDay; // yyyy-MM-dd

                String googlePlacesApiKey = getResources().getString(R.string.google_places_key);
                ApiManager.getQPExpressApi(bus, googlePlacesApiKey, mOriginAirportCode, mDestinationAirportCode, date);

                recyclerViewSetup();
                mFlightSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryLight, R.color.colorAccent, R.color.colorAccentDark);
                mFlightSwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume: INPUT----FLIGHT-----TABFRAGMENT ===>>> resuming");
    }

    @Subscribe
    public void onFlightData(Flights flights){
        Timber.d("onFlightData: SUBSCRIBE  PRICING==> " + flights.getTrips().getTripOption().get(0).getPricing());

        recyclerViewAdapter = new InputTabFlightRVAdapter(flights);
        mFlightRecyclerView.setAdapter(recyclerViewAdapter);
    }


    private void setRecyclerViewFlightsDummyData(){
        Flights flights = returnFlightsDummyData();
        Timber.d("onFlightData: SUBSCRIBE  PRICING==> " + flights.getTrips().getTripOption().get(0).getPricing());

        // Loggind data Object
        List<TripOption> tripOption = flights.getTrips().getTripOption();
        for(TripOption trip : tripOption){
            Timber.d("setRecyclerViewFlightsDummyData: SALETOTAL ***** " + trip.getSaleTotal());
            List<Slice> slice = trip.getSlice();
            for(Slice slice1 : slice){
                List<Segment> segments = slice1.getSegment();
                for(Segment segment : segments){
                    Timber.d("setRecyclerViewFlightsDummyData: CARRIER ***** " + segment.getFlight().getCarrier());
                    Timber.d("setRecyclerViewFlightsDummyData: NUMBER***** " + segment.getFlight().getNumber());
                }
            }
        }


        recyclerViewAdapter = new InputTabFlightRVAdapter(flights);
        mFlightRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private Flights returnFlightsDummyData(){
        Gson gson = new Gson();
        Flights sampleFlights = gson.fromJson(loadJSONFromAsset("QPXExpressJSON.json"), Flights.class);
        Timber.d("onCreateView: ===>>> TEST DATA " + sampleFlights.getTrips().getTripOption().size());
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

