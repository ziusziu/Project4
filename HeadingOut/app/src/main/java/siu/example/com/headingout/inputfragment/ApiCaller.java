package siu.example.com.headingout.inputfragment;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import siu.example.com.headingout.inputfragment.providers.FlightStatsService;
import siu.example.com.headingout.inputfragment.providers.ForecastService;
import siu.example.com.headingout.inputfragment.providers.GoogleHotelService;
import siu.example.com.headingout.inputfragment.providers.GoogleQPExpressService;
import siu.example.com.headingout.inputfragment.providers.HotwireService;
import siu.example.com.headingout.inputfragment.rvadapter.InputTabWeatherRVAdapter;
import siu.example.com.headingout.model.TestHotels;
import siu.example.com.headingout.model.airports.Airports;
import siu.example.com.headingout.model.flights.Flights;
import siu.example.com.headingout.model.flights.postrequest.Passengers;
import siu.example.com.headingout.model.flights.postrequest.PostSlice;
import siu.example.com.headingout.model.flights.postrequest.Request;
import siu.example.com.headingout.model.flights.postrequest.RequestJson;
import siu.example.com.headingout.model.forecast.Weather;
import siu.example.com.headingout.model.hotels.HotWireHotels;

/**
 * Created by samsiu on 5/9/16.
 */
public class ApiCaller extends AppCompatActivity{

    private static final String TAG = ApiCaller.class.getSimpleName();


    private static String mLatitude;
    private static String mLongitude;

    private static final String FORECAST_API_URL = "https://api.forecast.io/forecast/";
    private static final String FLIGTHSTATS_API_URL = "https://api.flightstats.com/flex/airports/rest/v1/json/withinRadius/";
    private static final String GOOGLE_HOTELS_API_URL = "https://www.googleapis.com/travelpartner/v1.2/";
    private static final String GOOGLE_QPEXPRESS_API_URL = "https://www.googleapis.com/qpxExpress/v1/trips/";
    private static final String HOTWIRE_API_URL = "http://api.hotwire.com/v1/search/";
    static Weather weather;
    static Airports airports;
    static HotWireHotels hotels;
    static Retrofit retrofit;
    static Flights flights;
    static TestHotels testHotels;


    public static void getHotWireApi(String hotwireApiKey){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(HOTWIRE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())  // CHANGE TO XML CONVERTER
                .client(client)
                .build();

        String responseFormat = "json";

        HotwireService service = retrofit.create(HotwireService.class);
        Call<HotWireHotels> call = service.getHotels(hotwireApiKey, responseFormat, "San%20Francisco,%20Ca.", "1", "2", "0", "05/20/2016", "05/23/2016");
        call.enqueue(new Callback<HotWireHotels>() {
            @Override
            public void onResponse(Call<HotWireHotels> call, Response<HotWireHotels> response) {
                if (response.isSuccessful()) {
                    hotels = response.body();
                    Log.d(TAG, "onResponse: ===>>>" + hotels.getResult().get(0).getTotalPrice());
                    Log.d(TAG, "onResponse: ====>>> RESPONSE BODY" + response.body().toString());


                } else {
                    Log.d(TAG, "onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                }
            }

            @Override
            public void onFailure(Call<HotWireHotels> call, Throwable t) {
                Log.d(TAG, "onFailure: onFailure UNSUCCESSFUL");
                t.printStackTrace();
            }
        });

    }


    public static void getQPExpressApi(String googlePlacesApiKey){

        Passengers passengers = new Passengers(1,0,0,0,0);
        PostSlice postSlice = new PostSlice("BOS", "LAX", "2016-05-10");
        ArrayList<PostSlice> slice = new ArrayList<>();
        slice.add(postSlice);
        Request request = new Request(slice, passengers, 20, false);
        RequestJson requestJson = new RequestJson(request);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(GOOGLE_QPEXPRESS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        GoogleQPExpressService service = retrofit.create(GoogleQPExpressService.class);
        Call<Flights> call = service.getFlights(googlePlacesApiKey, requestJson);
        call.enqueue(new Callback<Flights>() {
            @Override
            public void onResponse(Call<Flights> call, Response<Flights> response) {
                if (response.isSuccessful()) {
                    flights = response.body();
                    Log.d(TAG, "onResponse: ===>>>" + flights.getTrips().getTripOption().get(0).getSlice().get(0).getSegment().get(0).getCabin());
                    Log.d(TAG, "onResponse: ====>>> RESPONSE BODY" + response.body().toString());


                } else {
                    Log.d(TAG, "onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                }
            }

            @Override
            public void onFailure(Call<Flights> call, Throwable t) {
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



    public static void getAirportsApi(String flightStatsApiKey, String flightStatsAppId){
        String distance = "50";

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(FLIGTHSTATS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        FlightStatsService service = retrofit.create(FlightStatsService.class);
        Call<Airports> call = service.getAirports(mLongitude, mLatitude, distance, flightStatsAppId, flightStatsApiKey);
        call.enqueue(new Callback<Airports>() {
            @Override
            public void onResponse(Call<Airports> call, Response<Airports> response) {
                if (response.isSuccessful()) {
                    airports = response.body();


                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + airports.getAirports().get(0).getName());
                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + airports.getAirports().get(0).getRegionName());
                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + airports.getAirports().get(0).getCityCode());
                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + airports.getAirports().get(0).getCountryName());


                } else {
                    Log.d(TAG, "onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                }
            }

            @Override
            public void onFailure(Call<Airports> call, Throwable t) {
                Log.d(TAG, "onFailure: onFailure UNSUCCESSFUL");
            }
        });


    }


    public static void getWeatherApi(String forecastApiKey, String mLatitude, String mLongitude){

        String latLong = mLatitude+","+mLongitude;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(FORECAST_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ForecastService service = retrofit.create(ForecastService.class);
        Call<Weather> call = service.getWeather(forecastApiKey, latLong);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    weather = response.body();

                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + weather.getTimezone());
                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + weather.getHourly().getData().get(0).getApparentTemperature());
                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + weather.getDaily().getData().get(0).getOzone());
                    Log.d(TAG, "onResponse: RESPONSE SUCCESSFUL *****  " + weather.getMinutely().getData().get(0).getPrecipProbability());



                } else {
                    Log.d(TAG, "onResponse: RESPONSE UNSUCCESSFUL IN onResponse()    " + response);
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.d(TAG, "onFailure: onFailure UNSUCCESSFUL");
            }
        });
    }



}
