package siu.example.com.headingout.detailfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import siu.example.com.headingout.R;
import siu.example.com.headingout.model.hotels.HotWireHotels;
import siu.example.com.headingout.util.FragmentUtil;
import siu.example.com.headingout.util.Utilities;

/**
 * Created by samsiu on 5/4/16.
 */
public class DetailFragment extends Fragment{

    private static final String TAG = DetailFragment.class.getSimpleName();
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null){
            int tabPagePosition = savedInstanceState.getInt(Utilities.POSITION);
            mViewPager.setCurrentItem(tabPagePosition);
        }
        View view = inflater.inflate(R.layout.detail_content, container, false);
        initViewPager(view);

        Bundle hotelBundle = getArguments();
        HotWireHotels hotels = (HotWireHotels) hotelBundle.getSerializable("HOTEL_SERIALIZABLE");
        ArrayList<Integer> hotelPositions = hotelBundle.getIntegerArrayList("HOTEL_POSITION");

        Log.d(TAG, "onCreateView: " + hotelPositions.get(0));
        Log.d(TAG, "onCreateView: " + hotels.getResult().get(0).getHWRefNumber());

        // initGoogleMaps();


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        FragmentUtil fragInfo = (FragmentUtil)getActivity();
        fragInfo.setFragmentToolBar(DetailFragment.class.getSimpleName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Utilities.POSITION, mTabLayout.getSelectedTabPosition());
    }

    private void initViewPager(View view){
        mViewPager = (ViewPager)view.findViewById(R.id.detail_viewPager);
        mViewPager.setAdapter(new DetailTabsFragmentPagerAdapter(getActivity().getSupportFragmentManager()));

        mTabLayout = (TabLayout)view.findViewById(R.id.detail_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);


        //mTabLayout.setScrollbarFadingEnabled(true);

    }

}
