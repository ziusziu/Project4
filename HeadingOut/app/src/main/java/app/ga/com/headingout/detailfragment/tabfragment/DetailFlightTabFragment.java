package app.ga.com.headingout.detailfragment.tabfragment;

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

import app.ga.com.headingout.R;
import app.ga.com.headingout.detailfragment.rvadapter.DetailTabFlightRVAdapter;
import app.ga.com.headingout.model.FlightTest;

/**
 * Created by samsiu on 4/29/16.
 */
public class DetailFlightTabFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int page;
    private static RecyclerView flightRecyclerView;

    public static DetailFlightTabFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        DetailFlightTabFragment fragment = new DetailFlightTabFragment();
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
        View view = inflater.inflate(R.layout.detail_tab_flight_fragment, container, false);
        flightRecyclerView = (RecyclerView)view.findViewById(R.id.detail_tab_flight_fragment_recyclerView);

        recyclerViewSetup();

        return view;
    }

    private void recyclerViewSetup(){
        List<FlightTest> flightList = new ArrayList<>();

        // Dummy Data
        FlightTest flight = new FlightTest("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        FlightTest flight1 = new FlightTest("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        FlightTest flight2 = new FlightTest("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        FlightTest flight3 = new FlightTest("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        FlightTest flight4 = new FlightTest("AAL342", "American Airlines", "ORD" , "3", "MIA", "N");
        flightList.add(flight);
        flightList.add(flight1);
        flightList.add(flight2);
        flightList.add(flight3);
        flightList.add(flight4);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        flightRecyclerView.setLayoutManager(linearLayoutManager);
        flightRecyclerView.setHasFixedSize(true);
        DetailTabFlightRVAdapter recyclerViewAdapter = new DetailTabFlightRVAdapter(flightList);
        flightRecyclerView.setAdapter(recyclerViewAdapter);

    }

}
