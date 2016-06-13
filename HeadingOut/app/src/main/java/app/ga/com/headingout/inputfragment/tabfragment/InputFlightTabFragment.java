package app.ga.com.headingout.inputfragment.tabfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import javax.inject.Inject;
import javax.inject.Named;

import app.ga.com.headingout.HeadingOutApplication;
import app.ga.com.headingout.R;
import app.ga.com.headingout.inputfragment.ApiManager;
import app.ga.com.headingout.inputfragment.rvadapter.InputTabFlightRVAdapter;
import app.ga.com.headingout.model.flights.Flights;
import app.ga.com.headingout.model.flights.Segment;
import app.ga.com.headingout.model.flights.Slice;
import app.ga.com.headingout.model.flights.TripOption;
import app.ga.com.headingout.util.Utilities;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by samsiu on 4/29/16.
 */
public class InputFlightTabFragment extends Fragment {

    private static int page;
    private static String destinationAirportCode;
    private static String originAirportCode;
    private static String endDay;
    private static String endMonth;
    private static String endYear;

    private static InputTabFlightRVAdapter recyclerViewAdapter;

    @BindView(R.id.input_tab_flight_fragment_swipe_refresh_layout) SwipeRefreshLayout flightSwipeRefreshLayout;
    @BindView(R.id.input_tab_flight_fragment_recyclerView) RecyclerView flightRecyclerView;
    @BindView(R.id.input_tab_flight_origin_textView) TextView originTextView;
    @BindView(R.id.input_tab_flight_destination_textView) TextView destinationTextView;

    private Flights flights;
    @Inject @Named("QPXExpress") Retrofit retrofit;
    Unbinder unbinder;

    public static InputFlightTabFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(Utilities.ARG_PAGE, page);
        InputFlightTabFragment fragment = new InputFlightTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(Utilities.ARG_PAGE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_tab_flight_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);
        // Dagger2
        ((HeadingOutApplication)getActivity().getApplication()).getNetComponent().inject(this);

        registerOttoBus();

        getSharedPreferences();

        initViews(view);

        recyclerViewSetup();

        swipeFlightRefreshListener();

        //setRecyclerViewFlightsDummyData();

        return view;
    }

    private void registerOttoBus(){
        Bus bus = createBus();
        bus.register(InputFlightTabFragment.this);
    }

    private Bus createBus(){
        // Register for bus events
        HeadingOutApplication headingOutApplication = (HeadingOutApplication)getActivity().getApplication();
        Bus bus = headingOutApplication.provideBus();
        return bus;
    }

    private void initViews(View view){
        // Set Color of Icons
        ImageView mainAirplaneIcon = (ImageView)view.findViewById(R.id.input_tab_flight_planeIcon_ImageView);
        //int color = Color.parseColor("#BBFFFFFF");
        int color = Utilities.convertColorHexToResource("#BBFFFFFF");
        mainAirplaneIcon.setColorFilter(color);

        Timber.d("initViews: mOriginAirportCode " + originAirportCode);

        originTextView.setText(originAirportCode);
        destinationTextView.setText(destinationAirportCode);
    }

    private void getSharedPreferences(){
        // Set Text to views
        SharedPreferences sharedPref = getActivity().getSharedPreferences(Utilities.PLACESPREFERENCES, Context.MODE_PRIVATE);
        destinationAirportCode = sharedPref.getString(Utilities.DESTINATIONAIRPORTCODE, "JFK");
        originAirportCode = sharedPref.getString(Utilities.ORIGINAIRPORTCODE, "JFK");
        endDay = sharedPref.getString(Utilities.ENDDAY, "Default");
        endMonth = sharedPref.getString(Utilities.ENDMONTH, "Default");
        endYear = sharedPref.getString(Utilities.ENDYEAR, "Default");
    }

    private void recyclerViewSetup(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        flightRecyclerView.setLayoutManager(linearLayoutManager);
        flightRecyclerView.setHasFixedSize(true);
    }

    private void swipeFlightRefreshListener(){
        flightSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                final Bus bus = headingOutApplication.provideBus();
                bus.register(this);

                String date = endYear + "-" + endMonth + "-" + endDay; // yyyy-MM-dd
                String googlePlacesApiKey = getResources().getString(R.string.google_places_key);
                ApiManager.getQPExpressFlights(retrofit, bus, googlePlacesApiKey, originAirportCode, destinationAirportCode, date);

                recyclerViewSetup();
                flightSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryLight, R.color.colorAccent, R.color.colorAccentDark);
                flightSwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume: INPUT----FLIGHT-----TABFRAGMENT ===>>> resuming");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Subscribe
    public void onFlightData(Flights flights){
        Timber.d("onFlightData: SUBSCRIBE  PRICING==> " + flights.getTrips().getTripOption().get(0).getPricing());

        recyclerViewAdapter = new InputTabFlightRVAdapter(flights);
        flightRecyclerView.setAdapter(recyclerViewAdapter);
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
        flightRecyclerView.setAdapter(recyclerViewAdapter);
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


