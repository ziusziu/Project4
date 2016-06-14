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
import app.ga.com.headingout.inputfragment.ApiManager;
import app.ga.com.headingout.inputfragment.rvadapter.InputTabHotelRVAdapter;
import app.ga.com.headingout.model.hotels.HotWireHotels;
import app.ga.com.headingout.util.Utilities;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by samsiu on 4/29/16.
 */
public class InputHotelTabFragment extends Fragment {

    private int page;
    private InputTabHotelRVAdapter recyclerViewAdapter;
    private ProgressBar progressBar;

    //region SharedPreferences Variables
    private static String startDay;
    private static String startMonth;
    private static String startYear;
    private static String endDay;
    private static String endMonth;
    private static String endYear;
    private static String destinationAirportCode;
    private static String originAirportCode;
    private static String destination;
    //endregion



    @BindView(R.id.input_tab_hotel_fragment_swipe_refresh_layout) SwipeRefreshLayout hotelSwipeRefreshLayout;
    @BindView(R.id.input_tab_hotel_fragment_recyclerView) RecyclerView hotelRecyclerView;
    @BindView(R.id.input_tab_hotel_destination_textView) TextView destinationTextView;

    Unbinder unbinder;
    @Inject @Named("Hotwire") Retrofit retrofit;
    @Inject Bus bus;

    public static InputHotelTabFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(Utilities.ARG_PAGE, page);
        InputHotelTabFragment fragment = new InputHotelTabFragment();
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
        View view = inflater.inflate(R.layout.input_tab_hotel_fragment, container, false);
        Timber.d("onCreateView: Page of TabLayout " + page);

        progressBar = (ProgressBar) view.findViewById(R.id.input_tab_hotel_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        unbinder = ButterKnife.bind(this, view);

        // Dagger2
        ((HeadingOutApplication)getActivity().getApplication()).getNetComponent().inject(this);

        //getSharedPreferences();

        registerOttoBus();

        recyclerViewSetup();

        swipeHotelRefreshListener();

        return view;
    }


    /**
     * Get the Shared Preferences
     */
    private void getSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences(Utilities.PLACESPREFERENCES, Context.MODE_PRIVATE);
        startDay = sharedPref.getString(Utilities.STARTDAY, "Default");
        startMonth = sharedPref.getString(Utilities.STARTMONTH, "Default");
        startYear = sharedPref.getString(Utilities.STARTYEAR, "Default");
        endDay = sharedPref.getString(Utilities.ENDDAY, "Default");
        endMonth = sharedPref.getString(Utilities.ENDMONTH, "Default");
        endYear = sharedPref.getString(Utilities.ENDYEAR, "Default");
        destinationAirportCode = sharedPref.getString(Utilities.DESTINATIONAIRPORTCODE, "JFK");
        originAirportCode = "SFO";
        destination = sharedPref.getString(Utilities.DESTINATION, "default");
        Timber.d("INPUT FRAGMENT CREATED======>>>>>>>> " + startYear);

        destinationTextView.setText(destinationAirportCode);
    }

    private void registerOttoBus(){
        bus.register(InputHotelTabFragment.this);
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
                refreshHotelContent();
            }
        });
    }

    /**
     * Pull down to refresh will make new API call
     */
    private void refreshHotelContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSharedPreferences();
                Timber.d("run: ===>>> PULLING TO REFRESH Hotels==== Destination: " + destination);

                String hotwireApiKey = getResources().getString(R.string.hotwire_api_key);
                String hotwireStartDate = startMonth + "/" + startDay + "/" + startYear;
                String hotwireEndDate = endMonth + "/" + endDay + "/" + endYear;
                ApiManager.getHotWireHotels(retrofit, bus, hotwireApiKey, hotwireStartDate, hotwireEndDate, destination);

                //recyclerViewSetup();
                hotelSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryLight, R.color.colorAccent, R.color.colorAccentDark);
                hotelSwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
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
        Timber.d("InputHotelTabFragment ===>>> resuming");
        getSharedPreferences();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
