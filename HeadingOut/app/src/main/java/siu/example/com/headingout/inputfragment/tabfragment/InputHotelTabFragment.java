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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import siu.example.com.headingout.HeadingOutApplication;
import siu.example.com.headingout.R;
import siu.example.com.headingout.inputfragment.ApiManager;
import siu.example.com.headingout.inputfragment.rvadapter.InputTabHotelRVAdapter;
import siu.example.com.headingout.model.TestHotels;
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

        // Register for bus events
        HeadingOutApplication headingOutApplication = (HeadingOutApplication)getActivity().getApplication();
        Bus bus = headingOutApplication.provideBus();
        bus.register(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_tab_hotel_fragment, container, false);
        TextView textView = (TextView) view.findViewById(R.id.input_hotel_editText);
        textView.setText("Fragment #" + mPage);

        progressBar = (ProgressBar) view.findViewById(R.id.input_tab_hotel_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        mHotelSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.input_tab_hotel_fragment_swipe_refresh_layout);

        mHotelRecyclerView = (RecyclerView)view.findViewById(R.id.input_tab_hotel_fragment_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mHotelRecyclerView.setLayoutManager(linearLayoutManager);
        mHotelRecyclerView.setHasFixedSize(true);

        //recyclerViewSetup();

        swipeHotelRefreshListener();


        return view;
    }

    private void recyclerViewSetup(){
        List<TestHotels> hotelList = new ArrayList<>();

        // Dummy Data
        TestHotels hotel = new TestHotels("Hilton", "SF", "CA", "US", "5");
        TestHotels hotel1 = new TestHotels("Hilton", "SF", "CA", "US", "5");
        TestHotels hotel2 = new TestHotels("Hilton", "SF", "CA", "US", "5");
        TestHotels hotel3 = new TestHotels("Hilton", "SF", "CA", "US", "5");
        TestHotels hotel4 = new TestHotels("Hilton", "SF", "CA", "US", "5");
        hotelList.add(hotel);
        hotelList.add(hotel1);
        hotelList.add(hotel2);
        hotelList.add(hotel3);
        hotelList.add(hotel4);


//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        mHotelRecyclerView.setLayoutManager(linearLayoutManager);
//        mHotelRecyclerView.setHasFixedSize(true);
//        recyclerViewAdapter = new InputTabHotelRVAdapter(hotelList);
//        mHotelRecyclerView.setAdapter(recyclerViewAdapter);

    }

    private void swipeHotelRefreshListener(){
        mHotelSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                Log.d(TAG, "run: ===>>> PULLING TO REFRESH Hotels====");

                String hotwireApiKey = getResources().getString(R.string.hotwire_api_key);
                HeadingOutApplication headingOutApplication = (HeadingOutApplication)getActivity().getApplication();
                Bus bus = headingOutApplication.provideBus();
                ApiManager.getHotWireApi(bus, hotwireApiKey);

                //recyclerViewSetup();
                mHotelSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimayLight, R.color.colorAccent, R.color.colorAccentDark);
                mHotelSwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: INPUT=----HOTEL---TABFRAGMENT ===>>> resuming");
    }

    @Subscribe
    public void onHotelData(HotWireHotels hotWireHotels) {
        Log.d(TAG, "onHotelData  SIZE " + hotWireHotels.getResult().size());


        progressBar.setVisibility(View.GONE);
        recyclerViewAdapter = new InputTabHotelRVAdapter(hotWireHotels);
        mHotelRecyclerView.setAdapter(recyclerViewAdapter);

    }

}
