package app.ga.com.headingout.inputfragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

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

import javax.inject.Inject;
import javax.inject.Named;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by samsiu on 5/4/16.
 */
public class InputFragment extends Fragment{

    // region View Declarations
    @BindView(R.id.input_tabLayout) TabLayout tabLayout;
    @BindView(R.id.input_viewPager) ViewPager viewPager;
    @BindView(R.id.input_continue_fab) FloatingActionButton inputContinueFabButton;
    //endregion

    //region SharedPreferences Variables
    private static String latitude;
    private static String longitude;
    private static String startDay;
    private static String startMonth;
    private static String startYear;
    private static String endDay;
    private static String endMonth;
    private static String endYear;
    private static String forecastApiKey;
    private static String destinationAirportCode;
    private static String originAirportCode;
    private static String destinationSharedPref;
    private static int flightPosition;
    private static int hotelPosition;
    private static int weatherPosition;
    //endregion

    //region API Objects
    private MapView mapView;
    private GoogleMap googleMap;
    private HotWireHotels hotWireHotels;
    private Weather weather;
    private Flights flights;
    private AirportData airport;
    //endregion

    private int size;
    private Unbinder unbinder;

    @Inject @Named("Hotwire") Retrofit retrofitHotwire;
    @Inject @Named("QPXExpress") Retrofit retrofitQPXExpress;
    @Inject @Named("Forecast") Retrofit retrofitForecast;
    @Inject @Named("FlightStats") Retrofit retrofitFlightStats;
    @Inject Bus bus;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null){
            int tabPagePosition = savedInstanceState.getInt(Utilities.POSITION);
            viewPager.setCurrentItem(tabPagePosition);
        }

        View view = inflater.inflate(R.layout.input_content, container, false);

        unbinder = ButterKnife.bind(this, view);
        ((HeadingOutApplication)getActivity().getApplication()).getNetComponent().inject(this);

        registerOttoBus();

        getSharedPreferences();

        makeApiCall();

        setViewProperties(view);

        initGoogleMaps(view, savedInstanceState);

        setShareFABListener();

        return view;
    }


    /**
     * Create an Otto event bus
     */
    private void registerOttoBus(){
        bus.register(this);
    }

    /**
     * Get the Shared Preferences
     */
    private void getSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences(Utilities.PLACESPREFERENCES, Context.MODE_PRIVATE);
        latitude = sharedPref.getString(Utilities.LATITUDE, "Default");
        longitude = sharedPref.getString(Utilities.LONGITUDE, "Default");
        startDay = sharedPref.getString(Utilities.STARTDAY, "Default");
        startMonth = sharedPref.getString(Utilities.STARTMONTH, "Default");
        startYear = sharedPref.getString(Utilities.STARTYEAR, "Default");
        endDay = sharedPref.getString(Utilities.ENDDAY, "Default");
        endMonth = sharedPref.getString(Utilities.ENDMONTH, "Default");
        endYear = sharedPref.getString(Utilities.ENDYEAR, "Default");
        flightPosition = sharedPref.getInt(Utilities.FLIGHTPOSITION, 0);
        hotelPosition = sharedPref.getInt(Utilities.HOTELPOSITION, 0);
        weatherPosition = sharedPref.getInt(Utilities.WEATHERPOSITION, 0);

        destinationAirportCode = sharedPref.getString(Utilities.DESTINATIONAIRPORTCODE, "JFK");
        originAirportCode = sharedPref.getString(Utilities.ORIGINAIRPORTCODE, "SFO");

        Timber.d("SHARED PREFERENCES: OriginAirportCOde ====>>> " + originAirportCode);
    }


//---------------------------------- Call API START ----------------------------------------//


//TODO Make the api call from MainFragment
    /**
     * Use Shared Preferences to make api calls in order to populate fragments
     */
    private void makeApiCall(){
        Timber.d("====>>> makeApiCall() ");

        getFlightData();
        getHotelData();
        getForecastData();

        // API to search for airports near a specified lat long
//        String distance = "5";
//        ApiManager.getAirportsApi(bus, googlePlacesApiKey, mLatitude, mLongitude, distance, flightStatsApiKey, flightStatsAppId, startDate, mDestinationAirportCode);

    }

    private void getFlightData(){
        String googlePlacesApiKey = getResources().getString(R.string.google_places_key);
        String startDate = startYear + "-" + startMonth + "-" + startDay;
        ApiManager.getQPExpressFlights(retrofitQPXExpress, bus, googlePlacesApiKey,
                originAirportCode, destinationAirportCode,
                startDate);
    }

    /**
     * Hotels Search Requires a full city name and not airport code
     * API that returns lat, long from airportcode, then gets Hotel Data after location returned
     */
    private void getHotelData(){
        String flightStatsApiKey = getResources().getString(R.string.flightStats_api_key);
        String flightStatsAppId = getResources().getString(R.string.flightStats_app_id);
        ApiManager.getAirportLocation(retrofitFlightStats, bus, flightStatsApiKey, flightStatsAppId,
                destinationAirportCode, startYear, startMonth, startDay);

    }

    private void getForecastData(){
        forecastApiKey = getResources().getString(R.string.forecast_api_key);
        ApiManager.getForecastWeather(retrofitForecast, bus, forecastApiKey, latitude, longitude);
    }

//---------------------------------- Call API END ----------------------------------------//

// ---------------------------------- OTTO START --------------------------------------------- //

    //--------------------------------------------------------------------------------------------
    // ApiManger Posts Data Objects to Event Bus, response is received after Retrofit API call
    //
    // InputFragment Subscribes to Objects
    //   - InputFragment holds api data for the case when AdapterFragments are not created
    //   - InputFragment holds api data to plot on google maps
    // InputFragment then Produces them for the Fragments inside the PagerStateAdapter RecyclerView
    //--------------------------------------------------------------------------------------------
    /**
     * Uses airport code to find city (destination). Use destination to search hotels
     * @param airport
     */
    @Subscribe
    public void onAirportData(AirportData airport){
        this.airport = airport;

        Timber.d("onAirportData: ===>>> OnAirportDataReturned   " + airport.getAirport().getCity());
        // Create destination in format "<city>,<state>" for HotwireSearch
        String destination = airport.getAirport().getCity()+","+airport.getAirport().getStateCode();
        destinationSharedPref = destination;

        SharedPreferences sharedPref = getActivity()
                .getSharedPreferences(Utilities.PLACESPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Utilities.DESTINATION, destinationSharedPref);
        editor.apply();

        String hotwireApiKey = getResources().getString(R.string.hotwire_api_key);
        String hotwireStartDate = startMonth + "/" + startDay + "/" + startYear;
        String hotwireEndDate = endMonth + "/" + endDay + "/" + endYear;

        // Make API call for hotels after airport city name returned
        ApiManager.getHotWireHotels(retrofitHotwire, bus, hotwireApiKey,
                hotwireStartDate, hotwireEndDate, destination);
    }

    @Subscribe
    public void onHotelData(HotWireHotels hotWireHotels) {
        this.hotWireHotels = hotWireHotels;

        //TODO Add amenities to markers
        List<HWAmenities> hwAmenities = hotWireHotels.getMetaData().getHotelMetaData().getAmenities();
        for(HWAmenities amenities: hwAmenities){
            Timber.d("onHotelData: " + amenities.getName());
            //TODO put id into HashMap, for easy find
        }

        // Get the api Lat, Longs and info for plotting google map markers
        List<HWNeighborhoods> hwNeighborHoods = hotWireHotels.getMetaData().getHotelMetaData().getNeighborhoods();
        for(int position = 0; position < hwNeighborHoods.size(); position++){
            Timber.d("onHotelData: " + hwNeighborHoods.get(position).getName());

            // Pull Latitude and Longitude Data
            String centroid = hwNeighborHoods.get(position).getCentroid();
            String[] centroidList = centroid.split(",", 2);
            double latitude = Double.parseDouble(centroidList[0]);
            double longitude = Double.parseDouble(centroidList[1]);

            if (position == 0) {
                setGoogleMapCameraPosition(latitude, longitude);
            }

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

        googleMap.addMarker(marker);
    }

    private void setGoogleMapCameraPosition(double latitude, double longitude){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(10).build();
        googleMap.animateCamera(CameraUpdateFactory
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

    //TODO TESTING, Remove done, post adapter size from weather adapter
    @Subscribe
    public void onSizeData(Integer size){
        this.size = size;
        Timber.d("onSizeData: === Posting Data from Weather adapter" + size);
    }

// ---------------------------------- OTTO END --------------------------------------------- //

    /**
     * Set the views of FabButton, TabLayout and initialize FragmentStatePagerAdapter.
     * @param view
     */
    private void setViewProperties(View view){
        Utilities.setFabButton(getActivity(), inputContinueFabButton, R.drawable.ic_share_24dp);

        InputTabsFragmentPagerAdapter inputTabsFragmentPagerAdapter = new InputTabsFragmentPagerAdapter(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(inputTabsFragmentPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        //mTabLayout.setScrollbarFadingEnabled(true);

        // adapter.setMyValue()
    }

    /**
     * Store FragmentPagerAdapter tab position to outState Bundle
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Utilities.POSITION, tabLayout.getSelectedTabPosition());
    }


//TODO Convert MapView to MapFragment, MapView will be deprecated
    /**
     * Initialize Google Maps
     * @param view
     * @param savedInstanceState
     */
    private void initGoogleMaps(View view, Bundle savedInstanceState){
        mapView = (MapView) view.findViewById(R.id.input_fragment_mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();// needed to get the map to display immediately
        googleMap = mapView.getMap();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//---------------------------------- FAB Share Button START -----------------------------------//

    /**
     * Creates a Share Modal with data from models
     */
    private void setShareFABListener() {
        inputContinueFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startDate = startMonth + "/" + startDay + "/" + startYear;
                String endDate = endMonth + "/" + endDay + "/" + endYear;

                String title = "HeadingOut: " + originAirportCode + " | " + startDate + " to " + endDate;
                String location = destinationSharedPref;
                String description = getBodyDescription();

                setShareIntent(v, title, location, description);
            }
        });
    }

    private String getBodyDescription(){
        String startDescription = "Hello,\nBelow are your trip details: \n";

        String flightDescription = getFlightDescription();
        String hotelDescription = getHotelDescription();
        String weatherDescription = getWeatherDescription();

        String endDescription = "Sincerely, \n HeadingOut Team";

        String description = startDescription + "\n" +
                flightDescription + "\n" +
                hotelDescription + "\n" +
                weatherDescription + "\n" +
                endDescription;

        return description;

    }

    private void setShareIntent(View v, String title, String location, String description){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, description);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, title + ": " + location);

        v.getContext().startActivity(Intent.createChooser(intent, "Share to"));
    }

    /**
     * Parse Flight Model to get data
     * Segment describes origin A to destination B. Legs represents all the stops between A and B
     * @return
     */
    private String getFlightDescription(){
        String flightDescription = "FLIGHT";
        String flightTotalCost = "Price: " + flights.getTrips().getTripOption().get(flightPosition).getSaleTotal();
        int duration = flights.getTrips().getTripOption().get(flightPosition).getSlice().get(0).getDuration();
        String flightDuration = "Total Duration: " + Utilities.convertMinToHours(duration);

        List<Leg> listLeg;
        List<Segment> listSegment = flights.getTrips().getTripOption().get(flightPosition).getSlice().get(0).getSegment();
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
            String segmentDuration = "Flight Duration: " + Utilities.convertMinToHours(segment.getDuration());
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
            if (segment.getConnectionDuration() != 0) {
                connectionDuration = "Connection Time: " + Utilities.convertMinToHours(segment.getConnectionDuration());
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

        return flightDescription;
    }

    /**
     * Parse Hotel Model to get data
     * @return
     */
    private String getHotelDescription(){
        HWResult result = hotWireHotels.getResult().get(hotelPosition);

        String hotelDescription = "HOTWIRE_HOTEL ";
        String destination = "Destination: " + destinationSharedPref;
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

        return hotelDescription;
    }

    /**
     * Parse weather model to get data
     * @return
     */
    private String getWeatherDescription(){

        String weatherDescription = "WEATHER ";
        WeatherInfoDaily weatherDaily = weather.getDaily().getData().get(weatherPosition);

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

        return weatherDescription;
    }


//---------------------------------- FAB Share Button END -----------------------------------//


    public void setDetailFragment(){
//                DetailFragment detailFragment = new DetailFragment();
//                detailFragment.setArguments(InputTabHotelRVAdapter.hotelBundle);
//
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.home_fragment_container, detailFragment).addToBackStack(null);
//                fragmentTransaction.commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        FragmentUtil fragInfo = (FragmentUtil)getActivity();
        fragInfo.setFragmentToolBar(InputFragment.class.getSimpleName());

        Timber.d("onResume: ===>>>>  InputFragment On RESUME");

        mapView.onResume();
    }


    @Override
    public void onDestroy() {
        Timber.d("onDestroy: ==>> InputFragment OnDestroy");
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Timber.d("INPUTFRAGMENT ====>>> View Destroyed");
        bus.unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        Timber.d("onPause: ==>> InputFragment OnPause");
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideFragmentKeyboard();
    }

    private void hideFragmentKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

}

