package siu.example.com.headingout.inputfragment.tabfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import siu.example.com.headingout.R;
import siu.example.com.headingout.inputfragment.rvadapter.InputTabHotelRVAdapter;
import siu.example.com.headingout.model.Hotels;

/**
 * Created by samsiu on 4/29/16.
 */
public class InputHotelTabFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private static RecyclerView mHotelRecyclerView;

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
        TextView textView = (TextView) view.findViewById(R.id.input_hotel_editText);
        textView.setText("Fragment #" + mPage);

        mHotelRecyclerView = (RecyclerView)view.findViewById(R.id.input_tab_hotel_fragment_recyclerView);

        recyclerViewSetup();

        return view;
    }

    private void recyclerViewSetup(){
        List<Hotels> hotelList = new ArrayList<>();

        // Dummy Data
        Hotels hotel = new Hotels("Hilton", "SF", "CA", "US", "5");
        Hotels hotel1 = new Hotels("Hilton", "SF", "CA", "US", "5");
        Hotels hotel2 = new Hotels("Hilton", "SF", "CA", "US", "5");
        Hotels hotel3 = new Hotels("Hilton", "SF", "CA", "US", "5");
        Hotels hotel4 = new Hotels("Hilton", "SF", "CA", "US", "5");
        hotelList.add(hotel);
        hotelList.add(hotel1);
        hotelList.add(hotel2);
        hotelList.add(hotel3);
        hotelList.add(hotel4);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mHotelRecyclerView.setLayoutManager(linearLayoutManager);
        mHotelRecyclerView.setHasFixedSize(true);
        InputTabHotelRVAdapter recyclerViewAdapter = new InputTabHotelRVAdapter(hotelList);
        mHotelRecyclerView.setAdapter(recyclerViewAdapter);

    }

}
