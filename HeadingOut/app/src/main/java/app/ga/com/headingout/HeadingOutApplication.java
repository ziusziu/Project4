package app.ga.com.headingout;

import android.app.Application;

import com.squareup.otto.Bus;

import timber.log.Timber;

public class HeadingOutApplication extends Application {

    Bus bus;

    /**
     * Define a bus of Otto events
     */
    public void onCreate() {
        super.onCreate();

        bus = new Bus();

        // Only run in debug build, when in debug build, plant a tree, info, warning, debug. Won't be released app
        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }


    }

    public Bus provideBus() {
        return bus;
    }


}
