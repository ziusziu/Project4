package siu.example.com.headingout.detailfragment.tabfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import siu.example.com.headingout.R;
import siu.example.com.headingout.detailfragment.rvadapter.DetailTabHotelRVAdapter;
import siu.example.com.headingout.model.Hotel;

/**
 * Created by samsiu on 4/29/16.
 */
public class DetailHotelTabFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private static RecyclerView mHotelRecyclerView;

    public static DetailHotelTabFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        DetailHotelTabFragment fragment = new DetailHotelTabFragment();
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
        View view = inflater.inflate(R.layout.detail_tab_hotel_fragment, container, false);

        mHotelRecyclerView = (RecyclerView)view.findViewById(R.id.detail_tab_hotel_fragment_recyclerView);

        recyclerViewSetup();

        return view;
    }

    private void recyclerViewSetup(){
        List<Hotel> hotelList = new ArrayList<>();

        // Dummy Data
        Hotel hotel = new Hotel("Hilton", "SF", "CA", "US", "5");
        Hotel hotel1 = new Hotel("Hilton", "SF", "CA", "US", "5");
        Hotel hotel2 = new Hotel("Hilton", "SF", "CA", "US", "5");
        Hotel hotel3 = new Hotel("Hilton", "SF", "CA", "US", "5");
        Hotel hotel4 = new Hotel("Hilton", "SF", "CA", "US", "5");
        hotelList.add(hotel);
        hotelList.add(hotel1);
        hotelList.add(hotel2);
        hotelList.add(hotel3);
        hotelList.add(hotel4);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mHotelRecyclerView.setLayoutManager(linearLayoutManager);
        mHotelRecyclerView.setHasFixedSize(true);
        DetailTabHotelRVAdapter recyclerViewAdapter = new DetailTabHotelRVAdapter(hotelList);
        mHotelRecyclerView.setAdapter(recyclerViewAdapter);

    }

}
