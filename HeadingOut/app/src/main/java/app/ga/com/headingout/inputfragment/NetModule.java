package app.ga.com.headingout.inputfragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by samsiu on 6/10/16.
 */
@Module
public class NetModule {

    @Provides @Named("Hotwire") @Singleton
    Retrofit provideRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.hotwire.com/v1/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }



}
