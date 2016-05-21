package app.ga.com.headingout.inputfragment.providers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import app.ga.com.headingout.model.airports.AirportData;


/**
 * Created by samsiu on 5/18/16.
 */
public interface FlightStatsAirportLocationService {

    @GET("{code}/{airportCode}/{year}/{month}/{day}?")
    Call<AirportData> getAirportLocation(@Path("code") String codeType,
                                @Path("airportCode") String key,
                               @Path("year") String year,
                               @Path("month") String month,
                               @Path("day") String day,
                               @Query("appId") String appId,
                               @Query("appKey") String appKey
    );


}
