package siu.example.com.headingout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import siu.example.com.headingout.detailactivity.DetailActivity;
import siu.example.com.headingout.inputactivity.InputTabsFragmentPagerAdapter;
import siu.example.com.headingout.util.Utilities;

/**
 * Created by samsiu on 5/4/16.
 */
public class InputFragment extends Fragment {

    private static final String TAG = InputFragment.class.getSimpleName();
    private static Toolbar mToolBar;
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;
    private FloatingActionButton mInputContinueFabButton;
    private static EditText mFlightEditText;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_content, container, false);

        initializeViews(view);
        initViewPager(view);
        initFab();

        onFabContinueButtonClick();

        return view;
    }

    private void initializeViews(View view){
        mFlightEditText = (EditText)view.findViewById(R.id.input_flight_editText);
        mInputContinueFabButton = (FloatingActionButton)view.findViewById(R.id.input_continue_fab);
    }

    private void initViewPager(View view){
        mViewPager = (ViewPager)view.findViewById(R.id.input_viewPager);
        mViewPager.setAdapter(new InputTabsFragmentPagerAdapter(getActivity().getSupportFragmentManager()));
        mTabLayout = (TabLayout)view.findViewById(R.id.input_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initFab(){
        setFabIconColor(mInputContinueFabButton, Utilities.FAB_BUTTON_COLOR);
    }

    protected static void setFabIconColor(FloatingActionButton searchFab, String fabColor){
        int color = Color.parseColor(fabColor);
        searchFab.setImageResource(R.drawable.icon_search);
        searchFab.setColorFilter(color);
    }

    private void onFabContinueButtonClick(){
        mInputContinueFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] searchTerms = {
                        "Hello"//mFlightEditText.getText().toString(),
                };

                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(Utilities.SHARED_PREFERENCES_FLIGHTTERM, searchTerms[0])
                        .apply();

                Intent mFlightResultsIntent = new Intent(getActivity(), DetailActivity.class);
                mFlightResultsIntent.putExtra(Utilities.INTENT_FLIGHT_KEY, searchTerms);
                startActivity(mFlightResultsIntent);
            }
        });
    }


}
