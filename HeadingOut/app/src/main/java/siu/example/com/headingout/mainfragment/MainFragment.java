package siu.example.com.headingout.mainfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import siu.example.com.headingout.MainActivity;
import siu.example.com.headingout.R;
import siu.example.com.headingout.inputfragment.InputFragment;
import siu.example.com.headingout.model.Trip;
import siu.example.com.headingout.util.FragmentUtil;

/**
 * Created by samsiu on 5/3/16.
 */
public class MainFragment extends Fragment{

    private static String TAG = MainFragment.class.getSimpleName();
    private static EditText mLocEditText;
    private static Button mAddButton;
    private static RecyclerView mTripRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_content, container, false);

        initializeViews(view);
        setAddButtonListener();
        recyclerViewSetup();
        return view;
    }

    private void recyclerViewSetup(){
        List<Trip> tripList = new ArrayList<>();

        // Dummy Data
        Trip trip = new Trip("San Francisco");
        Trip trip1 = new Trip("San Francisco");
        Trip trip2 = new Trip("San Francisco");
        Trip trip3 = new Trip("San Francisco");
        Trip trip4 = new Trip("San Francisco");
        Trip trip5 = new Trip("San Francisco");
        Trip trip6 = new Trip("San Francisco");
        tripList.add(trip);
        tripList.add(trip1);
        tripList.add(trip2);
        tripList.add(trip3);
        tripList.add(trip4);
        tripList.add(trip5);
        tripList.add(trip6);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mTripRecyclerView.setLayoutManager(linearLayoutManager);
        mTripRecyclerView.setHasFixedSize(true);
        MainTripRVAdapter recyclerViewAdapter = new MainTripRVAdapter(tripList);
        mTripRecyclerView.setAdapter(recyclerViewAdapter);

    }

    private void setAddButtonListener(){
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                InputFragment inputFragment = new InputFragment();
                fragmentTransaction.replace(R.id.home_fragment_container, inputFragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void initializeViews(View view){
        mLocEditText = (EditText)view.findViewById(R.id.main_locationInput_edittext);
        mAddButton = (Button)view.findViewById(R.id.main_addLocation_button);
        mTripRecyclerView = (RecyclerView)view.findViewById(R.id.main_recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentUtil fragInfo = (FragmentUtil)getActivity();
        fragInfo.setFragmentToolBar(MainFragment.class.getSimpleName());
    }
}
