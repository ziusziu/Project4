package app.ga.com.headingout.inputfragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.ga.com.headingout.HeadingOutApplication;
import app.ga.com.headingout.R;

import app.ga.com.headingout.model.airports.AirportData;
import app.ga.com.headingout.model.flights.Flights;

import app.ga.com.headingout.model.flights.Leg;
import app.ga.com.headingout.model.flights.Segment;
import app.ga.com.headingout.model.forecast.Weather;
import app.ga.com.headingout.model.forecast.WeatherInfoDaily;
import app.ga.com.headingout.model.hotels.HWAmenities;
import app.ga.com.headingout.model.hotels.HWNeighborhoods;
import app.ga.com.headingout.model.hotels.HWResult;
import app.ga.com.headingout.model.hotels.HotWireHotels;
import app.ga.com.headingout.util.FragmentUtil;
import app.ga.com.headingout.util.Utilities;

/**
 * Created by samsiu on 5/4/16.
 */
public class InputFragment extends Fragment{

    private static final String TAG = InputFragment.class.getSimpleName();

    // region View Declarations
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;
    private static FloatingActionButton mInputContinueFabButton;
    private static EditText mFlightEditText;
    public static InputTabsFragmentPagerAdapter mInputTabsFragmentPagerAdapter;
    //endregion
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
    public static final String DESTINATION = "destination";
    public static final String FLIGHTPOSITION = "flightPosition";
    public static final String HOTELPOSITION = "hotelPosition";
    public static final String WEATHERPOSITION = "weatherPosition";
    //endregion
    //region SharedPreferences Variables
    private static String mLatitude;
    private static String mLongitude;
    private static String mStartDay;
    private static String mStartMonth;
    private static String mStartYear;
    private static String mEndDay;
    private static String mEndMonth;
    private static String mEndYear;
    private static String forecastApiKey;
    private static String mDestinationAirportCode;
    private static String mOriginAirportCode;
    private static String mDestination;
    private static int mFlightPosition;
    private static int mHotelPosition;
    private static int mWeatherPosition;
    //endregion
    //region API Objects
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private HotWireHotels hotWireHotels;
    private Weather weather;
    private Flights flights;
    private AirportData airport;
    //endregion

    private int size;
    private Bus bus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null){
            int tabPagePosition = savedInstanceState.getInt(Utilities.POSITION);
            mViewPager.setCurrentItem(tabPagePosition);
        }
        View view = inflater.inflate(R.layout.input_content, container, false);

        createBus();

        initializeViews(view);
        initViewPager(view);
        initFab();
        initGoogleMaps(view, savedInstanceState);

        getSharedPreferences();

        onFabContinueButtonClick();

        makeApiCall();

        return view;
    }


    private void makeApiCall(){

        Log.d(TAG, "onCreateView: ====>>> InputFragment - makeApiCall");

        String googlePlacesApiKey = getResources().getString(R.string.google_places_key);
        String startDate = mStartYear + "-" + mStartMonth + "-" + mStartDay;


        // Get Airport Data
        ApiManager.getQPExpressApi(bus, googlePlacesApiKey,
                mOriginAirportCode, mDestinationAirportCode,
                startDate);

        Log.d(TAG, "makeApiCall: latitude " + mLatitude);
        // Get Weather Data
        forecastApiKey = getResources().getString(R.string.forecast_api_key);
        ApiManager.getWeatherApi(bus, forecastApiKey, mLatitude, mLongitude);


        // API that returns lat, long from airportcode, then gets Hotel Data after location returned
        String flightStatsApiKey = getResources().getString(R.string.flightStats_api_key);
        String flightStatsAppId = getResources().getString(R.string.flightStats_app_id);
        ApiManager.getAirportLocation(bus, flightStatsApiKey, flightStatsAppId,
                mDestinationAirportCode, mStartYear, mStartMonth, mStartDay);


          // API to search for airports near a specified lat long
//        String distance = "5";
//        ApiManager.getAirportsApi(bus, googlePlacesApiKey, mLatitude, mLongitude, distance, flightStatsApiKey, flightStatsAppId, startDate, mDestinationAirportCode);

    }


    //--------------------------------------------------------------------------------------------
    // ApiManger Posts Data Objects to Event Bus, after response is received after Retrofit API call
    // InputFragment Subscribes to Objects
    //   - InputFragment holds api data for the case when AdapterFragments are not created
    //   - InputFragment holds api data to plot on google maps
    // InputFragment then Produces them for the Fragments inside the PagerStateAdapter RecyclerView
    //--------------------------------------------------------------------------------------------
    @Subscribe
    public void onHotelData(HotWireHotels hotWireHotels) {
        this.hotWireHotels = hotWireHotels;

        List<HWAmenities> hwAmenities = hotWireHotels.getMetaData().getHotelMetaData().getAmenities();
        for(HWAmenities amenities: hwAmenities){
            Log.d(TAG, "onHotelData: " + amenities.getName());
            //TODO put id into HashMap, for easy find
        }

        // Gets the api Lat, Longs and info for markers
        List<HWNeighborhoods> hwNeighborHoods = hotWireHotels.getMetaData().getHotelMetaData().getNeighborhoods();
        for(int position = 0; position < hwNeighborHoods.size(); position++){
            Log.d(TAG, "onHotelData: " + hwNeighborHoods.get(position).getName());

            // Pull Latitude and Longitude Data
            String centroid = hwNeighborHoods.get(position).getCentroid();
            String[] centroidList = centroid.split(",", 2);
            double latitude = Double.parseDouble(centroidList[0]);
            double longitude = Double.parseDouble(centroidList[1]);

            // Pull Hotel Reference Number Data
            String hwRefNum = hotWireHotels.getResult().get(position).getHWRefNumber();
            String hwCurrency = hotWireHotels.getResult().get(position).getCurrencyCode();
            String hwPrice = hotWireHotels.getResult().get(position).getTotalPrice();
            String hwRating = hotWireHotels.getResult().get(position).getStarRating();


            // Plot Marker on Google Maps
            plotGoogleMaps(latitude, longitude, hwRefNum, hwCurrency, hwPrice, hwRating);
        }

    }

    /**
     * Plot markers on Google Maps
     * @param latitude
     * @param longitude
     * @param hWRefNum
     * @param hwCurrency
     * @param hwPrice
     * @param hwRating
     */
    private void plotGoogleMaps(double latitude, double longitude, String hWRefNum, String hwCurrency, String hwPrice, String hwRating){

        String snippetText = "Price: " + hwCurrency+hwPrice + " | Rating " + hwRating;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude))
                .title("HotWire Ref Num: " + hWRefNum)
                .snippet(snippetText);

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        mGoogleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

    }


    @Produce
    public HotWireHotels produceHotwireHotels() {
        return hotWireHotels;
    }

    @Subscribe
    public void onWeatherData(Weather weather){
        this.weather = weather;
    }

    @Produce
    public Weather produceWeather(){
        return weather;
    }

    @Subscribe
    public void onFlightData(Flights flights){
        this.flights = flights;
    }

    @Produce
    public Flights produceFlights(){
        return flights;
    }

    @Subscribe
    public void onAirportData(AirportData airport){
        this.airport = airport;

        Log.d(TAG, "onAirportData: ===>>> OnAirportDataReturned   " + airport.getAirport().getCity());
        // Create destination in format "<city>,<state>" for HotwireSearch
        String destination = airport.getAirport().getCity()+","+airport.getAirport().getStateCode();
        mDestination = destination;

        String hotwireApiKey = getResources().getString(R.string.hotwire_api_key);
        String hotwireStartDate = mStartMonth + "/" + mStartDay + "/" + mStartYear;
        String hotwireEndDate = mEndMonth + "/" + mEndDay + "/" + mEndYear;
        ApiManager.getHotWireApi(bus, hotwireApiKey, hotwireStartDate, hotwireEndDate, mDestination);
    }


    /**
     * Store FragmentPagerAdapter tab position to outState Bundle
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Utilities.POSITION, mTabLayout.getSelectedTabPosition());
    }

    /**
     * Declare layout views
     * @param view
     */
    private void initializeViews(View view){
        mFlightEditText = (EditText)view.findViewById(R.id.input_flight_editText);
        mInputContinueFabButton = (FloatingActionButton)view.findViewById(R.id.input_continue_fab);
    }

    /**
     * Initialize FragmentStatePagerAdapter
     * @param view
     */
    private void initViewPager(View view){
        mViewPager = (ViewPager)view.findViewById(R.id.input_viewPager);
        mInputTabsFragmentPagerAdapter = new InputTabsFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mInputTabsFragmentPagerAdapter);
        mTabLayout = (TabLayout)view.findViewById(R.id.input_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
        //mTabLayout.setScrollbarFadingEnabled(true);

        // adapter.setMyValue()
    }

    /**
     * Get the Shared Preferences
     */
    private void getSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        mLatitude = sharedPref.getString(LATITUDE, "Default");
        mLongitude = sharedPref.getString(LONGITUDE, "Default");
        mStartDay = sharedPref.getString(STARTDAY, "Default");
        mStartMonth = sharedPref.getString(STARTMONTH, "Default");
        mStartYear = sharedPref.getString(STARTYEAR, "Default");
        mEndDay = sharedPref.getString(ENDDAY, "Default");
        mEndMonth = sharedPref.getString(ENDMONTH, "Default");
        mEndYear = sharedPref.getString(ENDYEAR, "Default");
        mFlightPosition = sharedPref.getInt(FLIGHTPOSITION, 0);
        mHotelPosition = sharedPref.getInt(HOTELPOSITION, 0);
        mWeatherPosition = sharedPref.getInt(WEATHERPOSITION, 0);

        mDestinationAirportCode = sharedPref.getString(DESTINATIONAIRPORTCODE, "JFK");
        mOriginAirportCode = sharedPref.getString(ORIGINAIRPORTCODE, "SFO");

        Log.d(TAG, "INPUT FRAGMENT CREATED======>>>>>>>> Origin SharedPref " + mOriginAirportCode);

    }

    /**
     * Create an Otto event bus
     */
    private void createBus(){
        HeadingOutApplication headingOutApplication = (HeadingOutApplication)getActivity().getApplication();
        bus = headingOutApplication.provideBus();
        bus.register(this);
    }

    /**
     * Initialize Google Maps
     * @param view
     * @param savedInstanceState
     */
    private void initGoogleMaps(View view, Bundle savedInstanceState){
        mMapView = (MapView) view.findViewById(R.id.input_fragment_mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        mGoogleMap = mMapView.getMap();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "INPUTFRAGMENT View Destroyed");
        bus.unregister(this);
        super.onDestroyView();
    }

    /**
     * Initizlize FAB Button
     */
    private void initFab(){
        setFabIconColor(mInputContinueFabButton, Utilities.FAB_BUTTON_COLOR);
    }

    /**
     * Set the color of FAB Button
     * @param searchFab
     * @param fabColor
     */
    protected static void setFabIconColor(FloatingActionButton searchFab, String fabColor) {
        int color = Color.parseColor(fabColor);
        //searchFab.setImageResource(R.drawable.ic_arrow_forward_24dp);
        searchFab.setImageResource(R.drawable.ic_share_24dp);
        searchFab.setColorFilter(color);
    }

    /**
     * Switch to Detail Fragment when FAB button clicked
     */
    private void onFabContinueButtonClick() {
        mInputContinueFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String flightDescription = "FLIGHT";
                String flightTotalCost = "Price: " + flights.getTrips().getTripOption().get(mFlightPosition).getSaleTotal();
                int duration = flights.getTrips().getTripOption().get(mFlightPosition).getSlice().get(0).getDuration();
                String flightDuration = "Total Duration: " + convertMinToHours(duration);

                List<Leg> listLeg;
                List<Segment> listSegment = flights.getTrips().getTripOption().get(mFlightPosition).getSlice().get(0).getSegment();
                String flightStops = "Stops: " + String.valueOf(listSegment.size() - 1);

                flightDescription = flightDescription + "\n" +
                        flightTotalCost + "\n" +
                        flightDuration + "\n" +
                        flightStops + "\n";

                String flightSegmentDescription = "";
                for (Segment segment : listSegment) {
                    String flightCarrier = segment.getFlight().getCarrier();
                    String flightNumber = segment.getFlight().getNumber();
                    String flightInfo = "Flight: " + flightCarrier + flightNumber;
                    String flightCabin = "Cabin: " + segment.getCabin();
                    String segmentDuration = "Flight Duration: " + convertMinToHours(segment.getDuration());
                    listLeg = segment.getLeg();
                    String departureTime = "";
                    String arrivalTime = "";
                    String origin = "";
                    String originTerminal = "";
                    String destination = "";
                    String destinationTerminal = "";
                    for (Leg leg : listLeg) {
                        origin = "Departing From: " + leg.getOrigin();
                        originTerminal = "Departing Terminal: " + leg.getOriginTerminal();
                        departureTime = "Departing: " + leg.getDepartureTime().substring(0, leg.getDepartureTime().length() - 6);
                        destination = "Arriving To: " + leg.getDestination();
                        destinationTerminal = "Arriving Terminal: " + leg.getDestinationTerminal();
                        arrivalTime = "Arriving: " + leg.getArrivalTime().substring(0, leg.getArrivalTime().length() - 6);
                    }

                    String connectionDuration = "";
                    if(segment.getConnectionDuration() != 0 ){
                        connectionDuration = "Connection Time: " + convertMinToHours(segment.getConnectionDuration());
                    }

                    flightSegmentDescription = flightSegmentDescription + "\n" +
                            flightInfo + "\n" +
                            flightCabin + "\n" +
                            segmentDuration + "\n" +
                            origin + "\n" +
                            originTerminal + "\n" +
                            departureTime + "\n" +
                            destination + "\n" +
                            destinationTerminal + "\n" +
                            arrivalTime + "\n" +
                            "\n" + connectionDuration + "\n";

                }

                flightDescription = flightDescription + flightSegmentDescription;

                String hotelDescription = "HOTWIRE_HOTEL ";
                HWResult result = hotWireHotels.getResult().get(mHotelPosition);
                String destination = "Destination: " + mDestination;
                String hwRefNumber = "HotWire Reference: " + result.getHWRefNumber();
                String hwStartDate = "Check In: " + result.getCheckInDate();
                String hwEndDate = "Check Out: " + result.getCheckOutDate();
                String hwNights = "Nights: " + result.getNights();
                String hwCurrency = result.getCurrencyCode();
                String hwPrice = result.getTotalPrice();
                String price = "Price: " + hwCurrency + hwPrice;
                String hwLink = "DeepLink: " + result.getDeepLink();
                hotelDescription = hotelDescription + "\n" +
                        destination + "\n" +
                        hwRefNumber + "\n" +
                        hwStartDate + "\n" +
                        hwEndDate + "\n" +
                        hwNights + "\n" +
                        price + "\n" +
                        hwLink + "\n";


                String weatherDescription = "WEATHER ";
                WeatherInfoDaily weatherDaily = weather.getDaily().getData().get(mWeatherPosition);

                int time = weatherDaily.getTime();
                String formattedTime = new SimpleDateFormat("MM/dd/yyyy").format(new Date(time * 1000L));

                String weatherTime = "Date: " + formattedTime;
                String weatherSummary = "Summary: " + weatherDaily.getSummary();

                String weatherHumidity = "Humidity: " + String.valueOf(weatherDaily.getHumidity());
                String weatherDewPoint = "Dew Point: " + String.valueOf(weatherDaily.getDewPoint());
                String weatherOzone = "Ozone: " + String.valueOf(weatherDaily.getOzone());


                String weatherSunRise = "Sunrise: " + String.valueOf(weatherDaily.getSunriseTime());
                String weatherSunSet = "Sunset: " + String.valueOf(weatherDaily.getSunsetTime());

                String weatherPrecipIntensity = "Precipitation Intensity: " + String.valueOf(weatherDaily.getPrecipIntensity());
                String weatherPrecipProbability = "Precipitation Probability: " + String.valueOf(weatherDaily.getPrecipProbability());

                String weatherTempMax = "Temperature Max: " + String.valueOf(weatherDaily.getApparentTemperatureMax());
                String weatherTempMaxTime = "Temperature Max Time: " + String.valueOf(weatherDaily.getApparentTemperatureMaxTime());

                String weatherTempMin = "Temperature Min: " + String.valueOf(weatherDaily.getApparentTemperatureMin());
                String weatherTempMinTime = "Temperature Min Time: " + String.valueOf(weatherDaily.getApparentTemperatureMinTime());

                weatherDescription = weatherDescription + "\n" +
                        weatherTime + "\n" +
                        weatherSummary + "\n" +
                        weatherHumidity + "\n" +
                        weatherDewPoint + "\n" +
                        //                                    weatherOzone + "\n" +
                        //weatherSunRise + "\n" +
                        //weatherSunSet + "\n" +
                        //                                    weatherPrecipIntensity + "\n" +
                        //                                    weatherPrecipProbability + "\n" +
                        weatherTempMax + "\n" +
                        //                                   weatherTempMaxTime + "\n" +
                        weatherTempMin + "\n" +
                        //                                   weatherTempMinTime + "\n";
                        "";


                String startDate = mStartMonth + "/" + mStartDay + "/" + mStartYear;
                String endDate = mEndMonth + "/" + mEndDay + "/" + mEndYear;

                String startDescription = "Hello,\nBelow are your trip details: \n";
                String endDescription = "Sincerely, \n HeadingOut Team";

                String description = startDescription + "\n" +
                                     flightDescription + "\n" +
                                     hotelDescription + "\n" +
                                     weatherDescription + "\n" +
                                     endDescription;
                String title = "HeadingOut: " + mOriginAirportCode + " | " + startDate + " to " + endDate;
                String location = mDestination;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, description);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, title + ": " + location);

                v.getContext().startActivity(Intent.createChooser(intent, "Share to"));

//                DetailFragment detailFragment = new DetailFragment();
//                detailFragment.setArguments(InputTabHotelRVAdapter.hotelBundle);
//
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.home_fragment_container, detailFragment).addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentUtil fragInfo = (FragmentUtil)getActivity();
        fragInfo.setFragmentToolBar(InputFragment.class.getSimpleName());

        Log.d(TAG, "onResume: ===>>>>  InputFragment On RESUME");

        mMapView.onResume();
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ==>> InputFragment OnDestroy");
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ==>> InputFragment OnPause");
        super.onPause();
        mMapView.onPause();
    }

    //TODO REMOVE
    @Subscribe
    public void onSizeData(Integer size){
        this.size = size;
        Log.d(TAG, "onSizeData: === Posting Data from adapter" + size);
    }


    /**
     * Converts minutes to a string in format "HH hours mm mins"
     * @param duration
     * @return
     */
    private String convertMinToHours(int duration){
        Long longVal = new Long(duration);
        int hours = (int) longVal.longValue() / 60;
        int mins = (int) longVal.longValue() - (hours * 60);
        String durationString = hours + " hours " + mins + " mins ";
        Log.d(TAG, "onCreateView: hours " + durationString);
        return durationString;
    }

}
