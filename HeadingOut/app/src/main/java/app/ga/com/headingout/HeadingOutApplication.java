package app.ga.com.headingout;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.squareup.otto.Bus;

import app.ga.com.headingout.util.DaggerNetComponent;
import app.ga.com.headingout.util.NetComponent;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class HeadingOutApplication extends Application {

    private NetComponent netComponent;

    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        netComponent = DaggerNetComponent.create();

        // Only run in debug build, when in debug build, plant a tree, info, warning, debug. Won't be released app
        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

}
