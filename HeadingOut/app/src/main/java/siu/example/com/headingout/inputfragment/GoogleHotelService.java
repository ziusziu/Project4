package siu.example.com.headingout.inputfragment;

import retrofit2.Call;
import retrofit2.http.GET;
import siu.example.com.headingout.model.TestHotels;

/**
 * Created by samsiu on 5/7/16.
 */
public interface GoogleHotelService {

    @GET("hotels?type=ALL")
    Call<TestHotels> getHotels();

}
