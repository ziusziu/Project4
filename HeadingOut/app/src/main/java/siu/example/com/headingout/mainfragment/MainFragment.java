package siu.example.com.headingout.mainfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import siu.example.com.headingout.inputfragment.InputFragment;
import siu.example.com.headingout.model.TestTrip;
import siu.example.com.headingout.util.FragmentUtil;

/**
 * Created by samsiu on 5/3/16.
 */
public class MainFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    private static String TAG = MainFragment.class.getSimpleName();
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static EditText mLocEditText;
    private static Button mAddButton;
    private static RecyclerView mTripRecyclerView;

    private static AutoCompleteTextView mAutoCompleteTextView;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;

    private static double mLatitude;
    private static double mLongitude;
    public static final String PLACESPREFERENCES = "placesLatLong";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_content, container, false);

        initializeViews(view);
        setAddButtonListener();
        recyclerViewSetup();
        //initAutoCompleteFragment();

        getGooglePlacesApi();

        return view;
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
        TestTrip trip = new TestTrip("San Francisco");
        TestTrip trip1 = new TestTrip("San Francisco");
        TestTrip trip2 = new TestTrip("San Francisco");
        TestTrip trip3 = new TestTrip("San Francisco");
        TestTrip trip4 = new TestTrip("San Francisco");
        TestTrip trip5 = new TestTrip("San Francisco");
        TestTrip trip6 = new TestTrip("San Francisco");
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

                SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(LATITUDE, Double.toString(mLatitude));
                editor.putString(LONGITUDE, Double.toString(mLongitude));
                editor.commit();

                Log.d(TAG, "onClick: THIS IS AUTOCOMPLETETEXT VALUE "+ mAutoCompleteTextView.getText());
                //TODO if empty pop up error and block
                String location = mAutoCompleteTextView.getText().toString();
                if(location.isEmpty()){
                    mAutoCompleteTextView.setError("Please input a location");
                    return;
                }

                Log.d(TAG, "BUTTON CLICKED======>>>>>>>> " + mLatitude);
                Log.d(TAG, "BUTTON CLICKED======>>>>>>>> " + mLongitude);
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
        mAutoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.main_autocomplete_textView);
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

            Log.d(TAG, "onResult:======>>>>>>>> "+ mLatitude);
            Log.d(TAG, "onResult:======>>>>>>>> "+ mLongitude);
//            mNameTextView.setText(Html.fromHtml(place.getName() + ""));
//            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
//            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
//            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
//            mWebTextView.setText(place.getWebsiteUri() + "");
//            if (attributions != null) {
//                mAttTextView.setText(Html.fromHtml(attributions.toString()));
//            }
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
