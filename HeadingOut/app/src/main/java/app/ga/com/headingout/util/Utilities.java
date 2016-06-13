package app.ga.com.headingout.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import app.ga.com.headingout.HeadingOutApplication;
import app.ga.com.headingout.MainActivity;
import app.ga.com.headingout.R;
import app.ga.com.headingout.model.TripDestination;
import timber.log.Timber;


/**
 * Created by samsiu on 5/3/16.
 */
public class Utilities {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String POSITION = "POSITION";
    public static final String FAB_BUTTON_COLOR = "#00C853"; //"#558B2F"
    private static final int FAB_COLOR = R.color.colorAccentDark;
    public static final String INTENT_FLIGHT_KEY = "locationTerms";

    //region SharedPreferences Constants
    public static final String PLACESPREFERENCES = "placesPreferences";
    public static final String DESTINATIONAIRPORTCODE = "destinationAirportCode";
    public static final String ORIGINAIRPORTCODE = "originAirportCode";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String STARTDAY = "startDay";
    public static final String STARTMONTH = "startMonth";
    public static final String STARTYEAR = "startYear";
    public static final String ENDDAY = "endDay";
    public static final String ENDMONTH = "endMonth";
    public static final String ENDYEAR = "endYear";
    public static final String FLIGHTPOSITION = "flightPosition";
    public static final String HOTELPOSITION = "hotelPosition";
    public static final String WEATHERPOSITION = "weatherPosition";
    public static final String DESTINATION = "destination";
    //endregion


    public static int convertColorHexToResource(String hexColor){
        return Color.parseColor(hexColor);
    }

    /**
     * Hide the keyboard on activity load
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity){
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * Set the color and image of FAB Button
     * @param searchFab
     */
    public static void setFabButton(Context context, FloatingActionButton searchFab, int icon) {
        int color = ContextCompat.getColor(context, FAB_COLOR);

        searchFab.setImageResource(icon);
        searchFab.setColorFilter(color);
    }

    /**
     * Ensure there is a destination in textView
     * @param textView
     */
    public static boolean isTextViewEmpty(AutoCompleteTextView textView){
        String location = textView.getText().toString();
        if (location.isEmpty()) {
            textView.setError("Please input a location");
            return true;
        }
        return false;
    }

    /**
     * Converts minutes to a string in format "HH hours mm mins"
     * @param duration
     * @return
     */
    public static String convertMinToHours(int duration){
        Long longVal = new Long(duration);
        int hours = (int) longVal.longValue() / 60;
        int mins = (int) longVal.longValue() - (hours * 60);
        String durationString = hours + " hours " + mins + " mins ";
        Timber.d("onCreateView: hours " + durationString);
        return durationString;
    }


    public static TextView createTextView(Context context, String text){
        TextView textView = new TextView(context);
        textView.setText(text);

        return textView;
    }

    public static void addNewTextViewToLayout(Context context, LinearLayout layout, String text){
        TextView textView = new TextView(context);
        textView.setText(text);
        layout.addView(textView);
    }

    public static void addNewTextViewToLayout(Context context, LinearLayout layout, String text, int textSize){
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        layout.addView(textView);
    }

    public static void addNewTextViewToLayout(Context context, LinearLayout layout, String text, int textSize, Typeface style){
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        textView.setTypeface(style);
        layout.addView(textView);
    }

    /**
     * Creates a list of Trips with default data for popular locations
     *
     * @return List<TripDestination>
     */
    public static List<TripDestination> initTripDestinations(){
        List<TripDestination> tripList = new ArrayList<>();

        String urlLA = "https://upload.wikimedia.org/wikipedia/commons/5/57/LA_Skyline_Mountains2.jpg";
        String urlDC = "https://upload.wikimedia.org/wikipedia/commons/a/af/WhiteHouseSouthFacade.JPG";
        String urlNYC = "https://upload.wikimedia.org/wikipedia/commons/d/d3/Statue_of_Liberty%2C_NY.jpg";
        String urlHawaii = "https://upload.wikimedia.org/wikipedia/commons/8/8d/Na_Pali_Coast%2C_Kauai%2C_Hawaii.jpg";
        String urlMiami = "https://upload.wikimedia.org/wikipedia/commons/a/a5/Mouth_of_Miami_River_20100211.jpg";
        String urlSeattle = "https://upload.wikimedia.org/wikipedia/commons/3/3a/Seattle_Skyline-.jpg";
        String urlChicago = "https://upload.wikimedia.org/wikipedia/commons/2/26/Chicago_Theatre_blend.jpg";
        String urlVegas = "https://upload.wikimedia.org/wikipedia/commons/2/2b/Las_Vegas%2C_Planet_Hollywood.jpg";

        TripDestination trip1 = new TripDestination("Los Angeles (LAX)", "LAX", "33.941446", "-118.408702", urlLA);
        TripDestination trip2 = new TripDestination("Washington D.C. (DCA)", "DCA", "38.851125", "-77.040350", urlDC);
        TripDestination trip3 = new TripDestination("New York City (JFK)", "JFK", "40.641189", "-73.778214", urlNYC);
        TripDestination trip4 = new TripDestination("Hawaii (HNL)", "HNL", "21.324224", "-157.925262", urlHawaii);
        TripDestination trip5 = new TripDestination("Miami (MIA)", "MIA", "25.795884", "-80.287346", urlMiami);
        TripDestination trip6 = new TripDestination("Seattle (SEA)", "SEA", "47.450134", "-122.309031", urlSeattle);
        TripDestination trip7 = new TripDestination("Chicago (ORD)", "ORD", "41.974027", "-87.907579", urlChicago);
        TripDestination trip8 = new TripDestination("Las Vegas (LAS)", "LAS", "36.083974", "-115.154082", urlVegas);

        tripList.add(trip1);
        tripList.add(trip2);
        tripList.add(trip3);
        tripList.add(trip4);
        tripList.add(trip5);
        tripList.add(trip6);
        tripList.add(trip7);
        tripList.add(trip8);

        return tripList;
    }

}
