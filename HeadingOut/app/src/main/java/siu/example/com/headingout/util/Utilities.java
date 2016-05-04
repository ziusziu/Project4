package siu.example.com.headingout.util;

import android.app.Activity;
import android.view.WindowManager;


/**
 * Created by samsiu on 5/3/16.
 */
public class Utilities {
    public static final String POSITION = "POSITION";
    public static final String FAB_BUTTON_COLOR = "#00C853"; //"#558B2F"
    public static final String INTENT_FLIGHT_KEY = "location_terms";
    public static final String SHARED_PREFERENCES_FLIGHTTERM = "shared_pref_location_term";

    public static void hideKeyboard(Activity activity){
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

}
