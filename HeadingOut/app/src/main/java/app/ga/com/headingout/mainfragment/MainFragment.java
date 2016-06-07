package app.ga.com.headingout.mainfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.ga.com.headingout.R;
import app.ga.com.headingout.inputfragment.DateRangePickerFragment;
import app.ga.com.headingout.inputfragment.InputFragment;
import app.ga.com.headingout.model.TripDestination;
import app.ga.com.headingout.util.FragmentUtil;
import app.ga.com.headingout.util.Utilities;
import butterknife.BindDrawable;
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

    @BindView(R.id.main_calendar_imageView) ImageView calendarImageView;
    @BindView(R.id.main_addLocation_button) Button addButton;
    @BindView(R.id.main_destination_recyclerView) RecyclerView tripDestinationRecyclerView;
    @BindView(R.id.main_origin_recyclerView) RecyclerView tripOriginRecyclerView;
    @BindView(R.id.main_destination_autocomplete_textView) AutoCompleteTextView destinationAutoCompleteTextView;
    @BindView(R.id.main_origin_autocomplete_textView) AutoCompleteTextView originAutoCompleteTextView;
    @BindView(R.id.main_goButton_fab) FloatingActionButton mainGoButtonFAB;
    @BindView(R.id.main_startDate_editText) EditText mainStartDateEditText;
    @BindView(R.id.main_endDate_editText) EditText mainEndDateEditText;

    //region SharedPreferences Constants
    public static final String PLACESPREFERENCES = "placesPreferences";
    public static final String DESTINATIONAIRPORTCODE = "destinationAirportCode";
    public static final String ORIGINAIRPORTCODE = "originAirportCode";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String STARTDAY = "startDay";
    public static final String STARTMONTH = "startMonth";
    public static final String STARTYEAR = "startYear";
    public static final String ENDDAY = "endDay";
    public static final String ENDMONTH = "endMonth";
    public static final String ENDYEAR = "endYear";
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

    //region View Declarations
//    private static Button addButton;
//    private static RecyclerView tripDestinationRecyclerView;
//    private static RecyclerView tripOriginRecyclerView;
//    private static ImageView calendarImageView;
//    private static AutoCompleteTextView destinationAutoCompleteTextView;
//    private static AutoCompleteTextView originAutoCompleteTextView;
//    private static FloatingActionButton mainGoButtonFAB;
//    private static EditText mainStartDateEditText;
//    private static EditText mainEndDateEditText;
    //endregion

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_content, container, false);

        unbinder = ButterKnife.bind(this, view);

        initializeViews();
        initFab();
        //setAddButtonListener();
        setMainGoFABListener();
        recyclerViewSetup();

        setDefaultDates();
        setCalendarClickListener();

        return view;
    }

    /**
     * Initialize Views
     */
    private void initializeViews(){
        // Change the color of Resources
        int color = Color.parseColor("#68EFAD");
        calendarImageView.setImageResource(R.drawable.calendar);
        calendarImageView.setColorFilter(color);

        addButton.getBackground().setColorFilter(color, PorterDuff.Mode.LIGHTEN);
    }

    /**
     * Returns selected date from DateRangePickerFragment
     * @param startDay
     * @param startMonth
     * @param startYear
     * @param endDay
     * @param endMonth
     * @param endYear
     */
    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear,
                                    int endDay, int endMonth, int endYear) {
        Timber.d("range : ", "from: " + startDay + "-" + startMonth + "-" + startYear +
                " to : " + endDay + "-" + endMonth + "-" + endYear);

        NumberFormat formatter = new DecimalFormat("00");

        MainFragment.startDay = formatter.format(startDay);
        MainFragment.startMonth = formatter.format(startMonth + 1);
        MainFragment.startYear = formatter.format(startYear);
        MainFragment.endDay = formatter.format(endDay);
        MainFragment.endMonth = formatter.format(endMonth + 1);
        MainFragment.endYear = formatter.format(endYear);

        mainStartDateEditText.setText(MainFragment.startMonth + "/" + MainFragment.startDay + "/" + MainFragment.startYear);
        mainEndDateEditText.setText(MainFragment.endMonth + "/" + MainFragment.endDay + "/" + MainFragment.endYear);
    }

    private void recyclerViewSetup(){
        List<TripDestination> tripList = Utilities.initTripDestinations();

        LinearLayoutManager destinationLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager originLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        tripDestinationRecyclerView.setLayoutManager(gridLayoutManager);
        tripDestinationRecyclerView.setHasFixedSize(true);

        MainTripRVAdapter recyclerDestinationViewAdapter = new MainTripRVAdapter(tripList, this);
        tripDestinationRecyclerView.setAdapter(recyclerDestinationViewAdapter);

        MainTripRVAdapter recyclerOriginViewAdapter = new MainTripRVAdapter(tripList, this);
        tripOriginRecyclerView.setLayoutManager(originLinearLayoutManager);
        tripOriginRecyclerView.setAdapter(recyclerOriginViewAdapter);

    }

    /**
     * Store sharedPreferences and switch out fragment when button is clicked
     */
    private void setMainGoFABListener(){
        mainGoButtonFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSharedPreferences();

                checkAutoCompleteTextInput(destinationAutoCompleteTextView);


                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                InputFragment inputFragment = new InputFragment();
                fragmentTransaction.replace(R.id.home_fragment_container, inputFragment);
                fragmentTransaction.commit();
            }
        });
    }

    /**
     * Store objects to shared preferences
     */
    private void saveSharedPreferences(){
        originAirportCode = originAutoCompleteTextView.getText().toString();

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(DESTINATIONAIRPORTCODE, destinationAirportCode);
        editor.putString(ORIGINAIRPORTCODE, originAirportCode);
        editor.putString(LATITUDE, Double.toString(latitude));
        editor.putString(LONGITUDE, Double.toString(longitude));
        editor.putString(STARTDAY, startDay);
        editor.putString(STARTMONTH, startMonth);
        editor.putString(STARTYEAR, startYear);
        editor.putString(ENDDAY, endDay);
        editor.putString(ENDMONTH, endMonth);
        editor.putString(ENDYEAR, endYear);
        editor.apply();
    }

    /**
     * Ensure there is a destination in textView
     * @param textView
     */
    private void checkAutoCompleteTextInput(AutoCompleteTextView textView){
        String location = textView.getText().toString();
        if (location.isEmpty()) {
            textView.setError("Please input a location");
            return;
        }
    }

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

        currentDate.add(currentDate.DATE, 1); // Day After's Date
        endDay = dayFormatter.format(currentDate.getTime());
        endMonth = monthFormatter.format(currentDate.getTime());
        endYear = yearFormatter.format(currentDate.getTime());

        Timber.d("MAIN FRAGMENT CREATED======>>>>>>>> " + startDay);
        Timber.d("MAIN FRAGMENT CREATED======>>>>>>>> " + startMonth);
        Timber.d("MAIN FRAGMENT CREATED======>>>>>>>> " + startYear);
        Timber.d("MAIN FRAGMENT CREATED======>>>>>>>> " + endDay);
        Timber.d("MAIN FRAGMENT CREATED======>>>>>>>> " + endMonth);
        Timber.d("MAIN FRAGMENT CREATED======>>>>>>>> " + endYear);
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentUtil fragInfo = (FragmentUtil)getActivity();
        fragInfo.setFragmentToolBar(MainFragment.class.getSimpleName());
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

    /**
     * Initizlize FAB Button
     */
    private void initFab(){
        setFabIconColor(mainGoButtonFAB, Utilities.FAB_BUTTON_COLOR);
    }

    /**
     * Set the color of FAB Button
     * @param searchFab
     * @param fabColor
     */
    protected static void setFabIconColor(FloatingActionButton searchFab, String fabColor) {
        int color = Color.parseColor(fabColor);
        //searchFab.setImageResource(R.drawable.ic_arrow_forward_24dp);
        searchFab.setImageResource(R.drawable.icon_search);
        searchFab.setColorFilter(color);
    }


    /**
     * Store sharedPreferences and switch out fragment when button is clicked
     */
    private void setAddButtonListener(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSharedPreferences();

                checkAutoCompleteTextInput(destinationAutoCompleteTextView);
                originAirportCode = originAutoCompleteTextView.getText().toString();

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                InputFragment inputFragment = new InputFragment();
                fragmentTransaction.replace(R.id.home_fragment_container, inputFragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
