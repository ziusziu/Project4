package app.ga.com.headingout.inputfragment.providers;

import app.ga.com.headingout.model.airports.AirportData;
import app.ga.com.headingout.model.sitaairports.SitaAirportData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by samsiu on 6/22/16.
 */
public interface SitaAirportLocationService {

    //"https://airport.api.aero/airport/<AirportCode>?user_key=<key>"
    @GET("{airportCode}/?")
    Call<SitaAirportData> getSitaAirportLocation(@Path("airportCode") String key,
                                                 @Query("user_key") String appKey
    );



}
