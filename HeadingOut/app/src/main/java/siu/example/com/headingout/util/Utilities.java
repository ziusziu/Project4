package siu.example.com.headingout.util;

import android.app.Activity;
import android.view.WindowManager;


/**
 * Created by samsiu on 5/3/16.
 */
public class Utilities {

    public static void hideKeyboard(Activity activity){
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

}
