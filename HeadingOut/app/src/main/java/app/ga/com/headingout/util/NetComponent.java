package app.ga.com.headingout.util;

import javax.inject.Singleton;

import app.ga.com.headingout.inputfragment.InputFragment;
import app.ga.com.headingout.inputfragment.tabfragment.InputFlightTabFragment;
import app.ga.com.headingout.inputfragment.tabfragment.InputHotelTabFragment;
import app.ga.com.headingout.inputfragment.tabfragment.InputWeatherTabFragment;

import dagger.Component;

/**
 * Created by samsiu on 6/10/16.
 */
@Singleton
@Component(modules={NetModule.class, BusModule.class})
public interface NetComponent {

    void inject(InputHotelTabFragment fragment);
    void inject(InputFlightTabFragment fragment);
    void inject(InputWeatherTabFragment fragment);
    void inject(InputFragment fragment);

}
