package app.ga.com.headingout.inputfragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TabHost;

import app.ga.com.headingout.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A DialogFragment that show 2 date pickers.
 * Yesid Lazaro @ingyesid
 *
 */
public class DateRangePickerFragment extends DialogFragment implements View.OnClickListener{

    @BindView(R.id.tabHost) TabHost tabHost;
    @BindView(R.id.but_set_time_range) Button butSetDateRange;
    @BindView(R.id.start_date_picker) DatePicker startDatePicker;
    @BindView(R.id.end_date_picker) DatePicker endDatePicker;


    private OnDateRangeSelectedListener onDateRangeSelectedListener;

    boolean is24HourMode;

    private Unbinder unbinder;

    public DateRangePickerFragment() {
        // Required empty public constructor
    }

    public static DateRangePickerFragment newInstance(OnDateRangeSelectedListener callback, boolean is24HourMode) {
        DateRangePickerFragment dateRangePickerFragment = new DateRangePickerFragment();
        dateRangePickerFragment.initialize(callback, is24HourMode);
        return dateRangePickerFragment;
    }

    public void initialize(OnDateRangeSelectedListener callback,
                           boolean is24HourMode) {
        onDateRangeSelectedListener = callback;
        this.is24HourMode = is24HourMode;
    }


    /**
     * Inflate Calendar Layout
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_range_picker, container, false);
        unbinder = ButterKnife.bind(this, view);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        butSetDateRange.setOnClickListener(this);
        tabHost.findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec startDatePage = tabHost.newTabSpec("start");
        startDatePage.setContent(R.id.start_date_group);
        startDatePage.setIndicator(getString(R.string.title_tab_start_date));
        TabHost.TabSpec endDatePage = tabHost.newTabSpec("end");
        endDatePage.setContent(R.id.end_date_group);
        endDatePage.setIndicator(getString(R.string.ttile_tab_end_date));
        tabHost.addTab(startDatePage);
        tabHost.addTab(endDatePage);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null)
            return;
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    public void setOnDateRangeSelectedListener(OnDateRangeSelectedListener callback) {
        this.onDateRangeSelectedListener = callback;
    }

    /**
     * Handle clicks inputs to view
     * @param v
     */
    @Override
    public void onClick(View v) {
        dismiss();
        onDateRangeSelectedListener.onDateRangeSelected(startDatePicker.getDayOfMonth(),startDatePicker.getMonth(),startDatePicker.getYear(),
                endDatePicker.getDayOfMonth(),endDatePicker.getMonth(),endDatePicker.getYear());
    }

    /**
     * Interface to pass dates out of fragment
     */
    public interface OnDateRangeSelectedListener {
        void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}