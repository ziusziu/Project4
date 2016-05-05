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
import siu.example.com.headingout.inputfragment.rvadapter.InputTabFlightRVAdapter;
import siu.example.com.headingout.model.Flight;

/**
 * Created by samsiu on 4/29/16.
 */
public class InputFlightTabFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private static RecyclerView mFlightRecyclerView;

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
        TextView textView = (TextView) view.findViewById(R.id.input_flight_editText);
        textView.setText("Fragment #" + mPage);

        mFlightRecyclerView = (RecyclerView)view.findViewById(R.id.input_tab_flight_fragment_recyclerView);

        recyclerViewSetup();

        return view;
    }

    private void recyclerViewSetup(){
        List<Flight> flightList = new ArrayList<>();

        // Dummy Data
        Flight flight = new Flight("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        Flight flight1 = new Flight("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        Flight flight2 = new Flight("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        Flight flight3 = new Flight("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        Flight flight4 = new Flight("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        flightList.add(flight);
        flightList.add(flight1);
        flightList.add(flight2);
        flightList.add(flight3);
        flightList.add(flight4);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFlightRecyclerView.setLayoutManager(linearLayoutManager);
        mFlightRecyclerView.setHasFixedSize(true);
        InputTabFlightRVAdapter recyclerViewAdapter = new InputTabFlightRVAdapter(flightList);
        mFlightRecyclerView.setAdapter(recyclerViewAdapter);

    }

}
