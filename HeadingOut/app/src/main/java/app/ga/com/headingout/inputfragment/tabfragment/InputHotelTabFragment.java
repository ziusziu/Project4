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
import android.widget.ProgressBar;
import android.widget.TextView;


import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Named;

import app.ga.com.headingout.HeadingOutApplication;
import app.ga.com.headingout.R;
import app.ga.com.headingout.inputfragment.NetComponent;
import app.ga.com.headingout.inputfragment.providers.HotwireService;
import app.ga.com.headingout.inputfragment.rvadapter.InputTabHotelRVAdapter;
import app.ga.com.headingout.model.hotels.HotWireHotels;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by samsiu on 4/29/16.
 */
public class InputHotelTabFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int page;
    private InputTabHotelRVAdapter recyclerViewAdapter;
    private ProgressBar progressBar;


    //region SharedPreferences Constants
    public static final String PLACESPREFERENCES = "placesPreferences";
    public static final String DESTINATIONAIRPORTCODE = "destinationAirportCode";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String STARTDAY = "startDay";
    public static final String STARTMONTH = "startMonth";
    public static final String STARTYEAR = "startYear";
    public static final String ENDDAY = "endDay";
    public static final String ENDMONTH = "endMonth";
    public static final String ENDYEAR = "endYear";
    public static final String DESTINATION = "destination";
    //endregion
    //region SharedPreferences Variables
    private static String latitude;
    private static String longitude;
    private static String startDay;
    private static String startMonth;
    private static String startYear;
    private static String endDay;
    private static String endMonth;
    private static String endYear;
    private static String forecastApiKey;
    private static String destinationAirportCode;
    private static String originAirportCode;
    private static String destination;
    //endregion

    private HotWireHotels hotels;
    private NetComponent netComponent;
    @Inject @Named("Hotwire") Retrofit retrofit;

    @BindView(R.id.input_tab_hotel_fragment_swipe_refresh_layout) SwipeRefreshLayout hotelSwipeRefreshLayout;
    @BindView(R.id.input_tab_hotel_fragment_recyclerView) RecyclerView hotelRecyclerView;
    @BindView(R.id.input_tab_hotel_destination_textView) TextView destinationTextView;

    Unbinder unbinder;

    public static InputHotelTabFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        InputHotelTabFragment fragment = new InputHotelTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_tab_hotel_fragment, container, false);
        Timber.d("onCreateView: Page of TabLayout " + page);

        progressBar = (ProgressBar) view.findViewById(R.id.input_tab_hotel_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        unbinder = ButterKnife.bind(this, view);

        ((HeadingOutApplication)getActivity().getApplication()).getNetComponent().inject(this);


        registerOttoBus();
        getSharedPreferences();

        recyclerViewSetup();
        swipeHotelRefreshListener();

        return view;
    }

    private void registerOttoBus(){
        Bus bus = createBus();
        bus.register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Bus createBus(){
        // Register for bus events
        HeadingOutApplication headingOutApplication = (HeadingOutApplication)getActivity().getApplication();
        Bus bus = headingOutApplication.provideBus();
        return bus;
    }

    private void recyclerViewSetup(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        hotelRecyclerView.setLayoutManager(linearLayoutManager);
        hotelRecyclerView.setHasFixedSize(true);
    }

    private void swipeHotelRefreshListener(){
        hotelSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFlightContent();
            }
        });
    }

    /**
     * Pull down to refresh will make new API call
     */
    private void refreshFlightContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Timber.d("run: ===>>> PULLING TO REFRESH Hotels====");

                HeadingOutApplication headingOutApplication = (HeadingOutApplication) getActivity().getApplication();
                final Bus bus = headingOutApplication.provideBus();

                String hotwireApiKey = getResources().getString(R.string.hotwire_api_key);
                String hotwireStartDate = startMonth + "/" + startDay + "/" + startYear;
                String hotwireEndDate = endMonth + "/" + endDay + "/" + endYear;

                Timber.d("run: ====>>>>>> Pull Down Refresh  " + destination);

                //ApiManager.getHotWireApi(bus, hotwireApiKey, hotwireStartDate, hotwireEndDate, destination);


                String responseFormat = "json";
                //String destination = "San%20Francisco,%20Ca."; //Only having city input is okay
                String rooms = "1";
                String adults = "2";
                String children = "0";


                HotwireService service = retrofit.create(HotwireService.class);
                Call<HotWireHotels> call = service.getHotels(hotwireApiKey,
                        responseFormat,
                        destination,
                        rooms,
                        adults,
                        children,
                        hotwireStartDate,
                        hotwireEndDate);
                call.enqueue(new Callback<HotWireHotels>() {
                    @Override
                    public void onResponse(Call<HotWireHotels> call, Response<HotWireHotels> response) {
                        if (response.isSuccessful()) {
                            hotels = response.body();

                            bus.post(hotels);
                            //inputTabsFragmentPagerAdapter.setHotels(hotels);
                            // inputTabsFragmentPagerAdapter.notifyDataSetChanged();

                        } else {
                            Timber.d("onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                        }
                    }

                    @Override
                    public void onFailure(Call<HotWireHotels> call, Throwable t) {
                        Timber.d("onFailure: onFailure UNSUCCESSFUL");
                        t.printStackTrace();
                    }
                });


                //recyclerViewSetup();
                hotelSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryLight, R.color.colorAccent, R.color.colorAccentDark);
                hotelSwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
    }

    /**
     * Get the Shared Preferences
     */
    private void getSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        latitude = sharedPref.getString(LATITUDE, "Default");
        longitude = sharedPref.getString(LONGITUDE, "Default");
        startDay = sharedPref.getString(STARTDAY, "Default");
        startMonth = sharedPref.getString(STARTMONTH, "Default");
        startYear = sharedPref.getString(STARTYEAR, "Default");
        endDay = sharedPref.getString(ENDDAY, "Default");
        endMonth = sharedPref.getString(ENDMONTH, "Default");
        endYear = sharedPref.getString(ENDYEAR, "Default");
        destinationAirportCode = sharedPref.getString(DESTINATIONAIRPORTCODE, "JFK");
        originAirportCode = "SFO";
        destination = sharedPref.getString(DESTINATION, "default");
        Timber.d("INPUT FRAGMENT CREATED======>>>>>>>> " + startYear);

        destinationTextView.setText(destinationAirportCode);
    }

    /**
     * Listen to data from ApiManager Post or from Produce from InputFragment
     * @param hotWireHotels
     */
    @Subscribe
    public void onHotelData(HotWireHotels hotWireHotels) {
        Timber.d("onHotelData  SIZE " + hotWireHotels.getResult().size());

        progressBar.setVisibility(View.GONE);
        recyclerViewAdapter = new InputTabHotelRVAdapter(hotWireHotels);
        hotelRecyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume: INPUT=----HOTEL---TABFRAGMENT ===>>> resuming");
    }
}
