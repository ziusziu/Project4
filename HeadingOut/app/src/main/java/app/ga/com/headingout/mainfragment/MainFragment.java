package app.ga.com.headingout.mainfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.ga.com.headingout.R;
import app.ga.com.headingout.inputfragment.InputFragment;
import app.ga.com.headingout.model.TripDestination;
import app.ga.com.headingout.util.FragmentUtil;
import app.ga.com.headingout.util.Utilities;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by samsiu on 5/3/16.
 */
public class MainFragment extends Fragment implements
        DateRangePickerFragment.OnDateRangeSelectedListener,
        MainTripRVAdapter.OnMainCardViewClickListener{

    //region Views
    @BindView(R.id.main_calendar_imageView) ImageView calendarImageView;
    @BindView(R.id.main_destination_recyclerView) RecyclerView tripDestinationRecyclerView;
    @BindView(R.id.main_origin_recyclerView) RecyclerView tripOriginRecyclerView;
    @BindView(R.id.main_destination_autocomplete_textView) AutoCompleteTextView destinationAutoCompleteTextView;
    @BindView(R.id.main_origin_autocomplete_textView) AutoCompleteTextView originAutoCompleteTextView;
    @BindView(R.id.main_searchButton_fab) FloatingActionButton mainSearchFAB;
    @BindView(R.id.main_startDate_editText) EditText mainStartDateEditText;
    @BindView(R.id.main_endDate_editText) EditText mainEndDateEditText;
    //endregion

    //region SharedPreferences Objects
    private static String destinationAirportCode;
    private static String originAirportCode;
    private static double latitude;
    private static double longitude;
    private static String startDay;
    private static String startMonth;
    private static String startYear;
    private static String endDay;
    private static String endMonth;
    private static String endYear;
    //endregion

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_content, container, false);

        unbinder = ButterKnife.bind(this, view);

        setViewProperties();

        setSearchFABListener();

        recyclerViewSetup();

        setDefaultDates();
        setCalendarClickListener();

        return view;
    }

    /**
     * Change the color of Resources
     */
    private void setViewProperties(){
        mainStartDateEditText.setKeyListener(null);
        mainEndDateEditText.setKeyListener(null);

        int color = ContextCompat.getColor(getActivity(), R.color.colorAccent);
        calendarImageView.setImageResource(R.drawable.calendar);
        calendarImageView.setColorFilter(color);

        int iconSearch = R.drawable.icon_search;
        Utilities.setFabButton(getActivity(), mainSearchFAB, iconSearch);
    }

    /**
     * Store sharedPreferences and switch out fragment when button is clicked
     */
    private void setSearchFABListener(){
        mainSearchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSharedPreferences();

                // Check textView
                if(isTextViewValid(destinationAutoCompleteTextView) & isTextViewValid(originAutoCompleteTextView)){
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    InputFragment inputFragment = new InputFragment();
                    fragmentTransaction.replace(R.id.home_fragment_container, inputFragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    /**
     * Check the Text Input
     * @param textView
     * @return
     */
    public static boolean isTextViewValid(AutoCompleteTextView textView){
        //TODO Handle error with airport code (EX: ABC, try{} catch{return to mainFragment with toast})
        String location = textView.getText().toString();
        if (location.isEmpty()) {
            textView.setError("Please Input an Airport Code");
            return false;
        }else if(location.length() != 3){
            textView.setError("Please Input a 3 Character Airport Code");
            return false;
        }
        return true;
    }


    /**
     * Store objects to shared preferences
     */
    private void saveSharedPreferences(){
        originAirportCode = originAutoCompleteTextView.getText().toString();
        destinationAirportCode = destinationAutoCompleteTextView.getText().toString();

        Timber.d("saveSharedPreferences: Origin is " + originAirportCode + " Destination is " + destinationAirportCode);
        SharedPreferences sharedPref = getActivity()
                .getSharedPreferences(Utilities.PLACESPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Utilities.DESTINATIONAIRPORTCODE, destinationAirportCode);
        editor.putString(Utilities.ORIGINAIRPORTCODE, originAirportCode);
        editor.putString(Utilities.LATITUDE, Double.toString(latitude));
        editor.putString(Utilities.LONGITUDE, Double.toString(longitude));
        editor.putString(Utilities.STARTDAY, startDay);
        editor.putString(Utilities.STARTMONTH, startMonth);
        editor.putString(Utilities.STARTYEAR, startYear);
        editor.putString(Utilities.ENDDAY, endDay);
        editor.putString(Utilities.ENDMONTH, endMonth);
        editor.putString(Utilities.ENDYEAR, endYear);
        editor.apply();
    }


// ----------------------------- RECYCLERVIEW START --------------------------------------//

    private void recyclerViewSetup(){
        List<TripDestination> tripList = Utilities.initTripDestinations();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        tripDestinationRecyclerView.setLayoutManager(gridLayoutManager);
        tripDestinationRecyclerView.setHasFixedSize(true);

        MainTripRVAdapter recyclerDestinationViewAdapter = new MainTripRVAdapter(tripList, this);
        tripDestinationRecyclerView.setAdapter(recyclerDestinationViewAdapter);


        // Currently not using Origin RecyclerView
        /*
        LinearLayoutManager originLinearLayoutManager = new LinearLayoutManager(
                                                                getActivity(),
                                                                LinearLayoutManager.HORIZONTAL,
                                                                false);

        MainTripRVAdapter recyclerOriginViewAdapter = new MainTripRVAdapter(tripList, this);
        tripOriginRecyclerView.setLayoutManager(originLinearLayoutManager);
        tripOriginRecyclerView.setAdapter(recyclerOriginViewAdapter);*/

    }


    /**
     * Load data from TripDestination object to EditText when city cardview is clicked
     * @param tripDestination
     */
    @Override
    public void onMainCardViewClick(TripDestination tripDestination) {
        destinationAirportCode = tripDestination.getAirportCode();

        destinationAutoCompleteTextView.setText(destinationAirportCode);

        latitude = Double.parseDouble(tripDestination.getLatitude());
        longitude = Double.parseDouble(tripDestination.getLongitude());
    }

// ----------------------------- RECYCLERVIEW END --------------------------------------//

// ------------------------------------ CALENDARVIEW START ----------------------- //
    /**
     * Set listener to show dateRangePickerFragment
     */
    private void setCalendarClickListener(){
        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.newInstance(MainFragment.this, false);
                dateRangePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

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
        startDay = dayFormatter.format(currentDate.getTime());
        startMonth = monthFormatter.format(currentDate.getTime());
        startYear = yearFormatter.format(currentDate.getTime());
        Timber.d("DEFAULT START DATE " + startMonth + "/" + startDay + "/" + startYear);

        currentDate.add(currentDate.DATE, 1); // Day After's Date
        endDay = dayFormatter.format(currentDate.getTime());
        endMonth = monthFormatter.format(currentDate.getTime());
        endYear = yearFormatter.format(currentDate.getTime());
        Timber.d("DEFAULT END DATE " + endMonth + "/" + endDay + "/" + endYear);

    }

    /**
     * Returns selected date from DateRangePickerFragment and populates EditText
     * @param startDayDateRange
     * @param startMonthDateRange
     * @param startYearDateRange
     * @param endDayDateRange
     * @param endMonthDateRange
     * @param endYearDateRange
     */
    @Override
    public void onDateRangeSelected(int startDayDateRange, int startMonthDateRange, int startYearDateRange,
                                    int endDayDateRange, int endMonthDateRange, int endYearDateRange) {
        Timber.d(
                "range : "+ "from: " + startDayDateRange + "-" + startMonthDateRange + "-" + startYearDateRange +
                        " to : " + endDayDateRange + "-" + endMonthDateRange + "-" + endYearDateRange
        );

        NumberFormat formatter = new DecimalFormat("00");

        startDay = formatter.format(startDayDateRange);
        startMonth = formatter.format(startMonthDateRange + 1);
        startYear = formatter.format(startYearDateRange);
        endDay = formatter.format(endDayDateRange);
        endMonth = formatter.format(endMonthDateRange + 1);
        endYear = formatter.format(endYearDateRange);

        mainStartDateEditText.setText(startMonth + "/" + startDay + "/" + startYear);
        mainEndDateEditText.setText(endMonth + "/" + endDay + "/" + endYear);
    }


// ------------------------------------ CALENDARVIEW END ----------------------- //

    @Override
    public void onResume() {
        super.onResume();

        // Change ToolBar name based on fragment
        FragmentUtil fragInfo = (FragmentUtil)getActivity();
        fragInfo.setFragmentToolBar(MainFragment.class.getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
