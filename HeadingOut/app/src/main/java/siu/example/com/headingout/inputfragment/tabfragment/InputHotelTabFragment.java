package siu.example.com.headingout.inputfragment.tabfragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import siu.example.com.headingout.HeadingOutApplication;
import siu.example.com.headingout.R;
import siu.example.com.headingout.inputfragment.ApiManager;
import siu.example.com.headingout.inputfragment.rvadapter.InputTabHotelRVAdapter;
import siu.example.com.headingout.model.hotels.HotWireHotels;

/**
 * Created by samsiu on 4/29/16.
 */
public class InputHotelTabFragment extends Fragment {
    private static final String TAG = InputHotelTabFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    private SwipeRefreshLayout mHotelSwipeRefreshLayout;

    private int mPage;
    private static RecyclerView mHotelRecyclerView;
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
    //endregion
    //region SharedPreferences Variables
    private static String mLatitude;
    private static String mLongitude;
    private static String mStartDay;
    private static String mStartMonth;
    private static String mStartYear;
    private static String mEndDay;
    private static String mEndMonth;
    private static String mEndYear;
    private static String forecastApiKey;
    private static String mDestinationAirportCode;
    private static String mOriginAirportCode;
    //endregion

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
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_tab_hotel_fragment, container, false);
        Log.d(TAG, "onCreateView: Page of TabLayout " + mPage);

        progressBar = (ProgressBar) view.findViewById(R.id.input_tab_hotel_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        registerOttoBus();

        initViews(view);
        recyclerViewSetup();
        swipeHotelRefreshListener();

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
        mHotelSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.input_tab_hotel_fragment_swipe_refresh_layout);
        mHotelRecyclerView = (RecyclerView)view.findViewById(R.id.input_tab_hotel_fragment_recyclerView);

    }

    private void recyclerViewSetup(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mHotelRecyclerView.setLayoutManager(linearLayoutManager);
        mHotelRecyclerView.setHasFixedSize(true);
    }

    private void swipeHotelRefreshListener(){
        mHotelSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                Log.d(TAG, "run: ===>>> PULLING TO REFRESH Hotels====");

                HeadingOutApplication headingOutApplication = (HeadingOutApplication) getActivity().getApplication();
                Bus bus = headingOutApplication.provideBus();

                String hotwireApiKey = getResources().getString(R.string.hotwire_api_key);
                String hotwireStartDate = mStartYear + "/" + mStartMonth + "/" + mStartDay;
                String hotwireEndDate = mEndYear + "/" + mEndMonth + "/" + mEndDay;
                ApiManager.getHotWireApi(bus, hotwireApiKey, hotwireStartDate, hotwireEndDate, mDestinationAirportCode);

                //recyclerViewSetup();
                mHotelSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimayLight, R.color.colorAccent, R.color.colorAccentDark);
                mHotelSwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
    }

    /**
     * Get the Shared Preferences
     */
    private void getSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        mLatitude = sharedPref.getString(LATITUDE, "Default");
        mLongitude = sharedPref.getString(LONGITUDE, "Default");
        mStartDay = sharedPref.getString(STARTDAY, "Default");
        mStartMonth = sharedPref.getString(STARTMONTH, "Default");
        mStartYear = sharedPref.getString(STARTYEAR, "Default");
        mEndDay = sharedPref.getString(ENDDAY, "Default");
        mEndMonth = sharedPref.getString(ENDMONTH, "Default");
        mEndYear = sharedPref.getString(ENDYEAR, "Default");
        mDestinationAirportCode = sharedPref.getString(DESTINATIONAIRPORTCODE, "JFK");
        mOriginAirportCode = "SFO";

        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> " + mStartYear);

    }

    /**
     * Listen to data from ApiManager Post or from Produce from InputFragment
     * @param hotWireHotels
     */
    @Subscribe
    public void onHotelData(HotWireHotels hotWireHotels) {
        Log.d(TAG, "onHotelData  SIZE " + hotWireHotels.getResult().size());

        progressBar.setVisibility(View.GONE);
        recyclerViewAdapter = new InputTabHotelRVAdapter(hotWireHotels);
        mHotelRecyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: INPUT=----HOTEL---TABFRAGMENT ===>>> resuming");
    }
}
