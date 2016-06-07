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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by samsiu on 5/4/16.
 */
public class InputFragment extends Fragment{

    @BindView(R.id.input_continue_fab) FloatingActionButton inputContinueFabButton;

    // region View Declarations
    private static TabLayout tabLayout;
    private static ViewPager viewPager;
    public static InputTabsFragmentPagerAdapter inputTabsFragmentPagerAdapter;
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
    private Bus bus;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null){
            int tabPagePosition = savedInstanceState.getInt(Utilities.POSITION);
            viewPager.setCurrentItem(tabPagePosition);
        }
        View view = inflater.inflate(R.layout.input_content, container, false);

        unbinder = ButterKnife.bind(this, view);

        createBus();

        initViewPager(view);
        initFab();
        initGoogleMaps(view, savedInstanceState);

        getSharedPreferences();

        onFabContinueButtonClick();

        makeApiCall();

        return view;
    }


    private void makeApiCall(){

        Timber.d("onCreateView: ====>>> InputFragment - makeApiCall");

        String googlePlacesApiKey = getResources().getString(R.string.google_places_key);
        String startDate = startYear + "-" + startMonth + "-" + startDay;


        // Get Airport Data
        ApiManager.getQPExpressApi(bus, googlePlacesApiKey,
                originAirportCode, destinationAirportCode,
                startDate);

        Timber.d("makeApiCall: latitude " + latitude);
        // Get Weather Data
        forecastApiKey = getResources().getString(R.string.forecast_api_key);
        ApiManager.getWeatherApi(bus, forecastApiKey, latitude, longitude);


        // API that returns lat, long from airportcode, then gets Hotel Data after location returned
        String flightStatsApiKey = getResources().getString(R.string.flightStats_api_key);
        String flightStatsAppId = getResources().getString(R.string.flightStats_app_id);
        ApiManager.getAirportLocation(bus, flightStatsApiKey, flightStatsAppId,
                destinationAirportCode, startYear, startMonth, startDay);


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
            Timber.d("onHotelData: " + amenities.getName());
            //TODO put id into HashMap, for easy find
        }

        // Gets the api Lat, Longs and info for markers
        List<HWNeighborhoods> hwNeighborHoods = hotWireHotels.getMetaData().getHotelMetaData().getNeighborhoods();
        for(int position = 0; position < hwNeighborHoods.size(); position++){
            Timber.d("onHotelData: " + hwNeighborHoods.get(position).getName());

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

        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
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

    @Subscribe
    public void onAirportData(AirportData airport){
        this.airport = airport;

        Timber.d("onAirportData: ===>>> OnAirportDataReturned   " + airport.getAirport().getCity());
        // Create destination in format "<city>,<state>" for HotwireSearch
        String destination = airport.getAirport().getCity()+","+airport.getAirport().getStateCode();
        destinationSharedPref = destination;

        String hotwireApiKey = getResources().getString(R.string.hotwire_api_key);
        String hotwireStartDate = startMonth + "/" + startDay + "/" + startYear;
        String hotwireEndDate = endMonth + "/" + endDay + "/" + endYear;
        ApiManager.getHotWireApi(bus, hotwireApiKey, hotwireStartDate, hotwireEndDate, destinationSharedPref);
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

    /**
     * Initialize FragmentStatePagerAdapter
     * @param view
     */
    private void initViewPager(View view){
        viewPager = (ViewPager)view.findViewById(R.id.input_viewPager);
        inputTabsFragmentPagerAdapter = new InputTabsFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(inputTabsFragmentPagerAdapter);
        tabLayout = (TabLayout)view.findViewById(R.id.input_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        //mTabLayout.setScrollbarFadingEnabled(true);

        // adapter.setMyValue()
    }

    /**
     * Get the Shared Preferences
     */
    private void getSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        latitude = sharedPref.getString(LATITUDE, "Default");
        longitude = sharedPref.getString(LONGITUDE, "Default");
        startDay = sharedPref.getString(STARTDAY, "Default");
        startMonth = sharedPref.getString(STARTMONTH, "Default");
        startYear = sharedPref.getString(STARTYEAR, "Default");
        endDay = sharedPref.getString(ENDDAY, "Default");
        endMonth = sharedPref.getString(ENDMONTH, "Default");
        endYear = sharedPref.getString(ENDYEAR, "Default");
        flightPosition = sharedPref.getInt(FLIGHTPOSITION, 0);
        hotelPosition = sharedPref.getInt(HOTELPOSITION, 0);
        weatherPosition = sharedPref.getInt(WEATHERPOSITION, 0);

        destinationAirportCode = sharedPref.getString(DESTINATIONAIRPORTCODE, "JFK");
        originAirportCode = sharedPref.getString(ORIGINAIRPORTCODE, "SFO");

        Timber.d("INPUT FRAGMENT CREATED======>>>>>>>> Origin SharedPref " + originAirportCode);

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

    @Override
    public void onDestroyView() {
        Timber.d("INPUTFRAGMENT View Destroyed");
        bus.unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Initizlize FAB Button
     */
    private void initFab(){
        setFabIconColor(inputContinueFabButton, Utilities.FAB_BUTTON_COLOR);
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
        inputContinueFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String flightDescription = "FLIGHT";
                String flightTotalCost = "Price: " + flights.getTrips().getTripOption().get(flightPosition).getSaleTotal();
                int duration = flights.getTrips().getTripOption().get(flightPosition).getSlice().get(0).getDuration();
                String flightDuration = "Total Duration: " + convertMinToHours(duration);

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
                    if (segment.getConnectionDuration() != 0) {
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
                HWResult result = hotWireHotels.getResult().get(hotelPosition);
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


                String startDate = startMonth + "/" + startDay + "/" + startYear;
                String endDate = endMonth + "/" + endDay + "/" + endYear;

                String startDescription = "Hello,\nBelow are your trip details: \n";
                String endDescription = "Sincerely, \n HeadingOut Team";

                String description = startDescription + "\n" +
                        flightDescription + "\n" +
                        hotelDescription + "\n" +
                        weatherDescription + "\n" +
                        endDescription;
                String title = "HeadingOut: " + originAirportCode + " | " + startDate + " to " + endDate;
                String location = destinationSharedPref;
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
    public void onPause() {
        Timber.d("onPause: ==>> InputFragment OnPause");
        super.onPause();
        mapView.onPause();
    }

    //TODO REMOVE
    @Subscribe
    public void onSizeData(Integer size){
        this.size = size;
        Timber.d("onSizeData: === Posting Data from adapter" + size);
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
        Timber.d("onCreateView: hours " + durationString);
        return durationString;
    }
}
