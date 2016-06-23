package app.ga.com.headingout.inputfragment;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.ArrayList;

import app.ga.com.headingout.HeadingOutApplication;
import app.ga.com.headingout.MainActivity;
import app.ga.com.headingout.inputfragment.providers.SitaAirportLocationService;
import app.ga.com.headingout.model.sitaairports.AirportResponse;
import app.ga.com.headingout.model.sitaairports.SitaAirportData;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import app.ga.com.headingout.inputfragment.providers.FlightStatsAirportLocationService;
import app.ga.com.headingout.inputfragment.providers.FlightStatsService;
import app.ga.com.headingout.inputfragment.providers.ForecastService;
import app.ga.com.headingout.inputfragment.providers.GoogleHotelService;
import app.ga.com.headingout.inputfragment.providers.GoogleQPExpressService;
import app.ga.com.headingout.inputfragment.providers.HotwireService;
import app.ga.com.headingout.model.TestHotels;
import app.ga.com.headingout.model.airports.AirportData;
import app.ga.com.headingout.model.airports.Airports;
import app.ga.com.headingout.model.flights.Flights;
import app.ga.com.headingout.model.flights.postrequest.Passengers;
import app.ga.com.headingout.model.flights.postrequest.PostSlice;
import app.ga.com.headingout.model.flights.postrequest.Request;
import app.ga.com.headingout.model.flights.postrequest.RequestJson;
import app.ga.com.headingout.model.forecast.Weather;
import app.ga.com.headingout.model.hotels.HotWireHotels;
import timber.log.Timber;

/**
 * Created by samsiu on 5/9/16.
 */
public class ApiManager {

    private static final String TAG = ApiManager.class.getSimpleName();

    private static String latitude;
    private static String longitude;

    private static final String FORECAST_API_URL = "https://api.forecast.io/forecast/";
    private static final String FLIGHTSTATS_API_URL = "https://api.flightstats.com/flex/airports/rest/v1/json/withinRadius/";
    private static final String FLIGHTSTATS_API_LOCATION_URL = "https://api.flightstats.com/flex/airports/rest/v1/json/";
    private static final String GOOGLE_HOTELS_API_URL = "https://www.googleapis.com/travelpartner/v1.2/";
    private static final String GOOGLE_QPEXPRESS_API_URL = "https://www.googleapis.com/qpxExpress/v1/trips/";
    private static final String HOTWIRE_API_URL = "http://api.hotwire.com/v1/search/";
    private static final String SITA_API_LOCATION_URL = "https://airport.api.aero/airport/";
    static Weather weather;
    static Airports airports;
    static HotWireHotels hotels;
    static Retrofit retrofit;
    static Flights flights;
    static TestHotels testHotels;
    static AirportData airport;
    static SitaAirportData sitaAirport;


    public static void getAirportLocation(Retrofit retrofit, final Bus bus, String flightStatsApiKey, String flightStatsAppId,
                                          String airportCode, String year, String month, String day){
        String codeType = "iata";

        FlightStatsAirportLocationService service = retrofit.create(FlightStatsAirportLocationService.class);
        Call<AirportData> call = service.getAirportLocation(codeType, airportCode, year, month, day, flightStatsAppId, flightStatsApiKey);
        call.enqueue(new Callback<AirportData>() {
            @Override
            public void onResponse(Call<AirportData> call, Response<AirportData> response) {
                if (response.isSuccessful()) {
                    airport = response.body();

                    try {
                        Log.d(TAG, "onResponse: OnSuccessAirportData " + airport.getAirport().getCity());
                        bus.post(airport);
                    }catch(Exception e){
                        e.printStackTrace();
                        Timber.d("NO AIRPORT LOCATION DATA");
                    }

                } else {
                    Log.d(TAG, "getAirportsApi:  RESPONSE UNSUCCESSFUL IN onResponse()  ==  " + response);
                }
            }

            @Override
            public void onFailure(Call<AirportData> call, Throwable t) {
                Log.d(TAG, "onFailure: onFailure UNSUCCESSFUL");
            }
        });
    }

    public static void getSitaAirportLocation(Retrofit retrofit, final Bus bus, String sitaApiKey,
                                          String airportCode){
        SitaAirportLocationService service = retrofit.create(SitaAirportLocationService.class);
        Timber.d("SITA RETOFIT SERVICE CREATED");

        Call<ResponseBody> call = service.getSitaAirportLocation(airportCode, sitaApiKey);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String parsedResponse = response.body().string().replace("callback(","").replace(")","");
                    Timber.d("SITA AIRPORT RESPONSEBODY: " +parsedResponse);

                    Gson gson = new Gson();
                    SitaAirportData sitaAirportData = gson.fromJson(parsedResponse, SitaAirportData.class);
                    Timber.d("SITA RETURNED DATA: mills "+sitaAirportData.getProcessingDurationMillis());
                    Timber.d("SITA RETURNED DATA: mills "+sitaAirportData.getAirports().get(0).getCity());

                    bus.post(sitaAirportData);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });



//        Call<SitaAirportData> call = service.getSitaAirportLocation(airportCode, sitaApiKey);
//        call.enqueue(new Callback<SitaAirportData>() {
//            @Override
//            public void onResponse(Call<SitaAirportData> call, Response<SitaAirportData> response) {
//                if (response.isSuccessful()) {
//                    sitaAirport = response.body();
//
//                    try {
//                        Timber.d("onResponse: OnSuccessSitaAirportData " + sitaAirport.getAirline());
//                        Timber.d("onResponse: OnSuccessSitaAirportData " + sitaAirport.getErrorMessage());
//                        bus.post(sitaAirport);
//                    }catch(Exception e){
//                        e.printStackTrace();
//                        Timber.d("NO AIRPORT LOCATION DATA");
//                    }
//
//                } else {
//                    Timber.d("getAirportsApi:  RESPONSE UNSUCCESSFUL IN onResponse()  ==  " + response);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SitaAirportData> call, Throwable t) {
//                Timber.d("onFailure: onFailure UNSUCCESSFUL: SITA AIRPORT");
//                t.printStackTrace();
//            }
//        });
    }


    public static void getHotWireHotels(Retrofit retrofit, final Bus bus, String hotwireApiKey, String startDate, String endDate, String destination) {

        Timber.d("getHotWireHotels API Call: Destination: " + destination);
        String responseFormat = "json";
        //String destination = "San%20Francisco,%20Ca."; //Only having city input is okay
        String rooms = "1";
        String adults = "2";
        String children = "0";

        HotwireService service = retrofit.create(HotwireService.class);
        Call<HotWireHotels> call = service.getHotels(hotwireApiKey,
                responseFormat,
                destination,
                rooms,
                adults,
                children,
                startDate,
                endDate);

        call.enqueue(new Callback<HotWireHotels>() {
            @Override
            public void onResponse(Call<HotWireHotels> call, Response<HotWireHotels> response) {
                if (response.isSuccessful()) {
                    hotels = response.body();

                    try {


//                        Timber.d("Hotel Tab Fragment Errors: " + hotels.getErrors().getErrors());
                        if (hotels.getResult().get(0).getHWRefNumber() == null) {
                            Timber.d("Hotel result, item 0 is NULL");
                        }

                        bus.post(hotels);
                        //inputTabsFragmentPagerAdapter.setHotels(hotels);
                        // inputTabsFragmentPagerAdapter.notifyDataSetChanged();
                    }catch(Exception e){
                        e.printStackTrace();
                        Timber.d("NO HOTEL DATA");
                    }

                } else {
                    Timber.d("Hotel - onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                }
            }

            @Override
            public void onFailure(Call<HotWireHotels> call, Throwable t) {
                Timber.d("HotWire - onFailure: onFailure UNSUCCESSFUL");
                t.printStackTrace();
            }
        });
    }



    public static void getQPExpressFlights(Retrofit retrofit, final Bus bus, String googlePlacesApiKey, String origin, String destination, String date) {

        int adultCount = 1;
        int infantInLapCount = 0;
        int infantInSeatCount = 0;
        int childCount = 0;
        int seniorCount = 0;
        int solutions = 20;
        boolean refundable = false;

        Passengers passengers = new Passengers(adultCount,
                infantInLapCount,
                infantInSeatCount,
                childCount,
                seniorCount);
        PostSlice postSlice = new PostSlice(origin, destination, date);
        ArrayList<PostSlice> slice = new ArrayList<>();
        slice.add(postSlice);
        Request request = new Request(slice, passengers, solutions, refundable);
        RequestJson requestJson = new RequestJson(request);


        GoogleQPExpressService service = retrofit.create(GoogleQPExpressService.class);
        Call<Flights> call = service.getFlights(googlePlacesApiKey, requestJson);
        call.enqueue(new Callback<Flights>() {
            @Override
            public void onResponse(Call<Flights> call, Response<Flights> response) {
                if (response.isSuccessful()) {
                    flights = response.body();
                    Timber.d("onResponse: ===>>>" + flights.getTrips().getTripOption().get(0).getSlice().get(0).getSegment().get(0).getCabin());
                    Timber.d("onResponse: ====>>> RESPONSE BODY" + response.body().toString());

                    try {

                        bus.post(flights);
                    }catch(Exception e){
                        e.printStackTrace();
                        Timber.d("NO FLIGHTS DATA");

                    }

                } else {
                    Timber.d("QPXExpress - onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                }
            }

            @Override
            public void onFailure(Call<Flights> call, Throwable t) {
                Timber.d("QPXEXPRESS - onFailure: onFailure UNSUCCESSFUL");
            }
        });
    }


    public static void getForecastWeather(Retrofit retrofit, final Bus bus,String forecastApiKey, String latitude, String longitude) {

        Timber.d("PRINT LATITUDE AND LONGITUDE " + latitude + " " + longitude);
        if(latitude.equals("0.0") & longitude.equals("0.0")){
            latitude = "Nothing";
            longitude = "Nothing";
        }
        String latLong = latitude+","+longitude;

        ForecastService service = retrofit.create(ForecastService.class);
        Call<Weather> call = service.getWeather(forecastApiKey, latLong);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    weather = response.body();

                    try {
                        bus.post(weather);
                        Timber.d("onResponse: RESPONSE SUCCESSFUL *****  " + weather.getTimezone());
                        Timber.d("onResponse: RESPONSE SUCCESSFUL *****  " + weather.getHourly().getData().get(0).getApparentTemperature());
                        Timber.d("onResponse: RESPONSE SUCCESSFUL *****  " + weather.getDaily().getData().get(0).getOzone());
                    }catch(Exception e){
                        e.printStackTrace();
                        Timber.d("NO WEATHER DATA");
                    }

                } else {
                    Timber.d("Weather - onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Timber.d("Weather - onFailure: onFailure UNSUCCESSFUL");
            }
        });

    }










    public static void getAirportsApi(final Bus bus,
                                      final String googlePlacesApiKey,
                                      String latitude,
                                      String longitude,
                                      String distance,
                                      String flightStatsApiKey,
                                      String flightStatsAppId,
                                      final String startDate,
                                      final String tripDestination){


        Log.d(TAG, "getAirportsApi: ===>>>> CALLING API  " +latitude + "    " + longitude);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(FLIGHTSTATS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        FlightStatsService service = retrofit.create(FlightStatsService.class);
        Call<Airports> call = service.getAirports(longitude, latitude, distance, flightStatsAppId, flightStatsApiKey);
        call.enqueue(new Callback<Airports>() {
            @Override
            public void onResponse(Call<Airports> call, Response<Airports> response) {
                if (response.isSuccessful()) {
                    airports = response.body();


                    int adultCount = 1;
                    int infantInLapCount = 0;
                    int infantInSeatCount = 0;
                    int childCount = 0;
                    int seniorCount = 0;
                    int solutions = 20;
                    boolean refundable = false;


                    //           Log.d(TAG, "onResponse: APICALLER CityCode " + airports.getAirports().get(0).getCityCode());

                    String origin = "SFO";
                    String destination = tripDestination;//airports.getAirports().get(0).getCityCode();
                    String date = startDate;

                    Log.d(TAG, "onResponse: DESTINATION CODE ***" + tripDestination);
                    Log.d(TAG, "onResponse: StartDate *** "+ startDate);

                    //getQPExpressAPI(bus, googlePlacesApiKey, origin, destination, date);


                    Log.d(TAG, "getAirportsApi:  RESPONSE SUCCESSFUL *****  " + airports.getAirports().get(0).getName());
                    Log.d(TAG, "getAirportsApi:  RESPONSE SUCCESSFUL *****  " + airports.getAirports().get(0).getRegionName());
                    Log.d(TAG, "getAirportsApi:  RESPONSE SUCCESSFUL ***** CITYCODE  " + airports.getAirports().get(0).getCityCode());
                    Log.d(TAG, "getAirportsApi:  RESPONSE SUCCESSFUL *****  " + airports.getAirports().get(0).getIata());
                    Log.d(TAG, "getAirportsApi:  RESPONSE SUCCESSFUL *****  " + airports.getAirports().get(0).getFaa());
                    Log.d(TAG, "getAirportsApi:  RESPONSE SUCCESSFUL *****  " + airports.getAirports().get(0).getFs());


                } else {
                    Log.d(TAG, "getAirportsApi:  RESPONSE UNSUCCESSFUL IN onResponse()  ==  " + response);
                }
            }

            @Override
            public void onFailure(Call<Airports> call, Throwable t) {
                Log.d(TAG, "onFailure: onFailure UNSUCCESSFUL");
            }
        });
    }


    // Stopped - Switched to HotWireApi
    public static void getGoogleHotleApi(){

        String queryType = "type";

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(GOOGLE_HOTELS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        GoogleHotelService service = retrofit.create(GoogleHotelService.class);
        Call<TestHotels> call = service.getHotels();
        call.enqueue(new Callback<TestHotels>() {
            @Override
            public void onResponse(Call<TestHotels> call, Response<TestHotels> response) {
                if (response.isSuccessful()) {
                    testHotels = response.body();
                    Log.d(TAG, "onResponse: ===>>>" + testHotels.getName());

                    Log.d(TAG, "onResponse: ====>>> RESPONSE BODY" + response.body().toString());


                } else {
                    Log.d(TAG, "onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                }
            }

            @Override
            public void onFailure(Call<TestHotels> call, Throwable t) {
                Log.d(TAG, "onFailure: onFailure UNSUCCESSFUL");
            }
        });


    }

}
