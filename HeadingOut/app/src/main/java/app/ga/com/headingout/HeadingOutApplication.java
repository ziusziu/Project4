package app.ga.com.headingout;

import android.app.Application;

import com.squareup.otto.Bus;

public class HeadingOutApplication extends Application {

    Bus bus;

    /**
     * Define a bus of Otto events
     */
    public void onCreate() {
        super.onCreate();

        bus = new Bus();
    }

    public Bus provideBus() {
        return bus;
    }

}
