package siu.example.com.headingout.mainfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import siu.example.com.headingout.R;
import siu.example.com.headingout.inputfragment.DateRangePickerFragment;
import siu.example.com.headingout.inputfragment.InputFragment;
import siu.example.com.headingout.model.TripDestination;
import siu.example.com.headingout.util.FragmentUtil;
import siu.example.com.headingout.util.Utilities;

/**
 * Created by samsiu on 5/3/16.
 */
public class MainFragment extends Fragment implements
        DateRangePickerFragment.OnDateRangeSelectedListener,
        MainTripRVAdapter.OnMainCardViewClickListener{

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


    private static String TAG = MainFragment.class.getSimpleName();
    private static Button mAddButton;
    private static RecyclerView mTripRecyclerView;
    private static ImageView mCalendarImageView;

    private static AutoCompleteTextView mAutoCompleteTextView;

    private static String mDestinationAirportCode;
    private static double mLatitude;
    private static double mLongitude;
    private static String mStartDay;
    private static String mStartMonth;
    private static String mStartYear;
    private static String mEndDay;
    private static String mEndMonth;
    private static String mEndYear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_content, container, false);

        initializeViews(view);
        setAddButtonListener();
        recyclerViewSetup();

        setDefaultDates();
        setCalendarClickListener();


        return view;
    }

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        Log.d("range : ", "from: " + startDay + "-" + startMonth + "-" + startYear + " to : " + endDay + "-" + endMonth + "-" + endYear);
        mStartDay = String.valueOf(startDay);
        mStartMonth = String.valueOf(startMonth);
        mStartYear = String.valueOf(startYear);
        mEndDay = String.valueOf(endDay);
        mEndMonth = String.valueOf(endMonth);
        mEndYear = String.valueOf(endYear);
    }

    private void recyclerViewSetup(){
        List<TripDestination> tripList = Utilities.initTripDestinations();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mTripRecyclerView.setLayoutManager(gridLayoutManager);
        mTripRecyclerView.setHasFixedSize(true);

        MainTripRVAdapter recyclerViewAdapter = new MainTripRVAdapter(tripList, this);
        mTripRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void setAddButtonListener(){
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSharedPreferences();

                checkAutoCompleteTextInput(mAutoCompleteTextView);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                InputFragment inputFragment = new InputFragment();
                fragmentTransaction.replace(R.id.home_fragment_container, inputFragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void saveSharedPreferences(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(DESTINATIONAIRPORTCODE, mDestinationAirportCode);
        editor.putString(LATITUDE, Double.toString(mLatitude));
        editor.putString(LONGITUDE, Double.toString(mLongitude));
        editor.putString(STARTDAY, mStartDay);
        editor.putString(STARTMONTH, mStartMonth);
        editor.putString(STARTYEAR, mStartYear);
        editor.putString(ENDDAY, mEndDay);
        editor.putString(ENDMONTH, mEndMonth);
        editor.putString(ENDYEAR, mEndYear);
        editor.apply();
    }


    private void checkAutoCompleteTextInput(AutoCompleteTextView textView){
        String location = textView.getText().toString();
        if (location.isEmpty()) {
            textView.setError("Please input a location");
            return;
        }
    }

    private void setCalendarClickListener(){
        mCalendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.newInstance(MainFragment.this, false);
                dateRangePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

    }

    private void initializeViews(View view){
        mCalendarImageView = (ImageView)view.findViewById(R.id.main_calendar_imageView);
        mAddButton = (Button)view.findViewById(R.id.main_addLocation_button);
        mTripRecyclerView = (RecyclerView)view.findViewById(R.id.main_recyclerView);
        mAutoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.main_autocomplete_textView);


        int color = Color.parseColor("#68EFAD");
        mCalendarImageView.setImageResource(R.drawable.calendar);
        mCalendarImageView.setColorFilter(color);
        mAddButton.getBackground().setColorFilter(color, PorterDuff.Mode.LIGHTEN);

    }

    /**
     * Set Defaul Calendar dates to today and tomorrow
     */
    private void setDefaultDates(){
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd");
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");
        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");

        currentDate.add(currentDate.DATE, 1); // Tomorrow's Date
        mStartDay = dayFormatter.format(currentDate.getTime());
        mStartMonth = monthFormatter.format(currentDate.getTime());
        mStartYear = yearFormatter.format(currentDate.getTime());

        currentDate.add(currentDate.DATE, 1); // Day After's Date
        mEndDay = dayFormatter.format(currentDate.getTime());
        mEndMonth = monthFormatter.format(currentDate.getTime());
        mEndYear = yearFormatter.format(currentDate.getTime());

        Log.d(TAG, "MAIN FRAGMENT CREATED======>>>>>>>> " + mStartDay);
        Log.d(TAG, "MAIN FRAGMENT CREATED======>>>>>>>> " + mStartMonth);
        Log.d(TAG, "MAIN FRAGMENT CREATED======>>>>>>>> " + mStartYear);
        Log.d(TAG, "MAIN FRAGMENT CREATED======>>>>>>>> " + mEndDay);
        Log.d(TAG, "MAIN FRAGMENT CREATED======>>>>>>>> " + mEndMonth);
        Log.d(TAG, "MAIN FRAGMENT CREATED======>>>>>>>> " + mEndYear);
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentUtil fragInfo = (FragmentUtil)getActivity();
        fragInfo.setFragmentToolBar(MainFragment.class.getSimpleName());
    }

    @Override
    public void onMainCardViewClick(TripDestination tripDestination) {
        mDestinationAirportCode = tripDestination.getAirportCode();
        mAutoCompleteTextView.setText(mDestinationAirportCode);
        mLatitude = Double.parseDouble(tripDestination.getLatitude());
        mLongitude = Double.parseDouble(tripDestination.getLongitude());
    }
}
