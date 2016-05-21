package app.ga.com.headingout.inputfragment.providers;

import retrofit2.Call;
import retrofit2.http.GET;
import app.ga.com.headingout.model.TestHotels;

/**
 * Created by samsiu on 5/7/16.
 */
public interface GoogleHotelService {

    @GET("hotels?type=ALL")
    Call<TestHotels> getHotels();

}
