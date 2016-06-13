package app.ga.com.headingout;

import android.app.Application;

import com.squareup.otto.Bus;

import app.ga.com.headingout.inputfragment.DaggerNetComponent;
import app.ga.com.headingout.inputfragment.NetComponent;
import timber.log.Timber;

public class HeadingOutApplication extends Application {

    private NetComponent netComponent;

    //TODO Inject Bus with Dagger2
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

        netComponent = DaggerNetComponent.create();
    }

    public Bus provideBus() {
        return bus;
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

}
