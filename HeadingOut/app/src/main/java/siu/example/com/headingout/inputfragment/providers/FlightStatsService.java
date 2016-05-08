package siu.example.com.headingout.inputfragment.providers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import siu.example.com.headingout.model.airports.Airports;


/**
 * Created by samsiu on 5/6/16.
 */
public interface FlightStatsService {

    @GET("{longitude}/{latitude}/{distance}?")
    Call<Airports> getAirports(@Path("longitude") String key,
                             @Path("latitude") String term,
                              @Path("distance") String distance,
                              @Query("appId") String appId,
                              @Query("appKey") String appKey
    );


}
