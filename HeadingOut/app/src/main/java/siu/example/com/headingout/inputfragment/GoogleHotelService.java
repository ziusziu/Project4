package siu.example.com.headingout.inputfragment;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import siu.example.com.headingout.model.Hotels;

/**
 * Created by samsiu on 5/7/16.
 */
public interface GoogleHotelService {

    @GET("hotels?type=ALL")
    Call<Hotels> getHotels();

}
