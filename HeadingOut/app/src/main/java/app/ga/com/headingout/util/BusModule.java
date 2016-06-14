package app.ga.com.headingout.util;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by samsiu on 6/14/16.
 */
@Module
public class BusModule {

    @Provides @Singleton
    Bus provideBus(){
        Bus bus = new Bus();
        return bus;
    }

}
