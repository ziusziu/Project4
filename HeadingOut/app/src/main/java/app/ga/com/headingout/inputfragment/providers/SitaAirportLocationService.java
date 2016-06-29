package app.ga.com.headingout.inputfragment.providers;

import okhttp3.ResponseBody;
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
    Call<ResponseBody> getSitaAirportLocation(@Path("airportCode") String key,
                                              @Query("user_key") String appKey
    );



}
