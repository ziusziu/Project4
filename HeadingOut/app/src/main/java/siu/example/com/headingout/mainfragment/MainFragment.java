package siu.example.com.headingout.mainfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import siu.example.com.headingout.MainActivity;
import siu.example.com.headingout.PlaceArrayAdapter;
import siu.example.com.headingout.R;
import siu.example.com.headingout.inputfragment.DateRangePickerFragment;
import siu.example.com.headingout.inputfragment.InputFragment;
import siu.example.com.headingout.model.TestTrip;
import siu.example.com.headingout.util.FragmentUtil;

/**
 * Created by samsiu on 5/3/16.
 */
public class MainFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        DateRangePickerFragment.OnDateRangeSelectedListener{

    public static final String PLACESPREFERENCES = "placesPreferences";
    public static final String ORIGINNAME = "originName";
    public static final String ORIGINADDRESS = "originAddress";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String STARTDAY = "startDay";
    public static final String STARTMONTH = "startMonth";
    public static final String STARTYEAR = "startYear";
    public static final String ENDDAY = "endDay";
    public static final String ENDMONTH = "endMonth";
    public static final String ENDYEAR = "endYear";


    private static String TAG = MainFragment.class.getSimpleName();
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static Button mAddButton;
    private static RecyclerView mTripRecyclerView;
    private static ImageView mCalendarImageView;

    private static AutoCompleteTextView mAutoCompleteTextView;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;

    private static String mOriginName;
    private static String mOriginAddress;
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
        //initAutoCompleteFragment();

        getGooglePlacesApi();


        mCalendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.newInstance(MainFragment.this, false);
                dateRangePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });


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



    private void getGooglePlacesApi(){
        String apiKey = getResources().getString(R.string.google_places_key);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutoCompleteTextView.setThreshold(1);
        mAutoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutoCompleteTextView.setAdapter(mPlaceArrayAdapter);

    }

    private void recyclerViewSetup(){
        List<TestTrip> tripList = new ArrayList<>();

        // Dummy Data
        TestTrip trip0 = new TestTrip("San Francisco");
        TestTrip trip1 = new TestTrip("Los Angeles");
        TestTrip trip2 = new TestTrip("Washington D.C.");
        TestTrip trip3 = new TestTrip("New York City");
        TestTrip trip4 = new TestTrip("Hawaii");
        TestTrip trip5 = new TestTrip("Miami");
        TestTrip trip6 = new TestTrip("Seatle");
        TestTrip trip7 = new TestTrip("Chicago");
        TestTrip trip8 = new TestTrip("LasVegas");

        tripList.add(trip0);
        tripList.add(trip1);
        tripList.add(trip2);
        tripList.add(trip3);
        tripList.add(trip4);
        tripList.add(trip5);
        tripList.add(trip6);
        tripList.add(trip7);
        tripList.add(trip8);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mTripRecyclerView.setLayoutManager(gridLayoutManager);
        mTripRecyclerView.setHasFixedSize(true);
        MainTripRVAdapter recyclerViewAdapter = new MainTripRVAdapter(tripList);
        mTripRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void setAddButtonListener(){
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(ORIGINNAME, mOriginName);
                editor.putString(ORIGINADDRESS, mOriginAddress);
                editor.putString(LATITUDE, Double.toString(mLatitude));
                editor.putString(LONGITUDE, Double.toString(mLongitude));
                editor.putString(STARTDAY, mStartDay);
                editor.putString(STARTMONTH, mStartMonth);
                editor.putString(STARTYEAR, mStartYear);
                editor.putString(ENDDAY, mEndDay);
                editor.putString(ENDMONTH, mEndMonth);
                editor.putString(ENDYEAR, mEndYear);
                editor.apply();


                String location = mAutoCompleteTextView.getText().toString();
                if(location.isEmpty()){
                    mAutoCompleteTextView.setError("Please input a location");
                    return;
                }


                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                InputFragment inputFragment = new InputFragment();
                fragmentTransaction.replace(R.id.home_fragment_container, inputFragment);
                fragmentTransaction.commit();
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

    @Override
    public void onResume() {
        super.onResume();
        FragmentUtil fragInfo = (FragmentUtil)getActivity();
        fragInfo.setFragmentToolBar(MainFragment.class.getSimpleName());
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO Add comments
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);

            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            mLatitude = place.getLatLng().latitude;
            mLongitude = place.getLatLng().longitude;
            mOriginName = String.valueOf(place.getName());
            mOriginAddress = String.valueOf(place.getAddress());

            Log.d(TAG, "onResult: ----->>>> MainFragment AutoFillCallBack <<<<<--------");
            Log.d(TAG, "onResult: Latitude "+ mLatitude);
            Log.d(TAG, "onResult: Longitude "+ mLongitude);
            Log.d(TAG, "onResult: PlaceName " + place.getName());
            Log.d(TAG, "onResult: PlaceAddress " + place.getAddress());
            Log.d(TAG, "onResult: PlaceLocale  " + place.getLocale());
            Log.d(TAG, "onResult: ----->>>> MainFragment AutoFillCallBack <<<<<--------");
        }
    };


    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onPause() {
        super.onPause();
        stopAutoManage();
    }

    private void stopAutoManage() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.stopAutoManage(getActivity());
    }

}
