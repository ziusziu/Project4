package siu.example.com.headingout.inputfragment.providers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import siu.example.com.headingout.model.hotels.Hotels;

/**
 * Created by samsiu on 5/7/16.
 */
public interface HotwireService {



    @GET("hotel?")
    Call<Hotels> getHotels(@Query("dest") String destination,
                          @Query("rooms") String rooms,
                          @Query("adults") String adults,
                          @Query("children") String children,
                          @Query("startdate") String startDate,
                          @Query("enddate") String endDate
                          );

}
