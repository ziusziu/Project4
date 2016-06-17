package app.ga.com.headingout;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;



/**
 * Created by samsiu on 4/29/16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {




    @Rule
    public ActivityTestRule<MainActivity> activityRule =
        new ActivityTestRule<MainActivity>(MainActivity.class);


    @Test
    public void checkCalendarIconDisplayed(){
        onView(withId(R.id.main_calendar_imageView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkStartDateEditTextDisplayed(){
        onView(withId(R.id.main_startDate_editText))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkEndDateEditTextDisplayed(){
        onView(withId(R.id.main_endDate_editText))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkOriginAutoCompleteTextDisplayed(){
        onView(withId(R.id.main_origin_autocomplete_textView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkDestinationAutoCompleteTextDisplayed(){
        onView(withId(R.id.main_destination_autocomplete_textView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkDestinationRecyclerViewDisplayed(){
        onView(withId(R.id.main_destination_recyclerView))
                .check(matches(isDisplayed()));
    }



    @Test
    public void checkFabDisplayed(){
        onView(withId(R.id.main_searchButton_fab))
                .check(matches(isDisplayed()));
    }






    @Test
    public void testCardViewClick(){
        onView(withId(R.id.main_trip_item_cardView)).perform(click());
        onView(withId(R.id.main_destination_autocomplete_textView))
                .check(matches(withText(TestStrings.CARDVIEW_ITEM)));//R.id.main_card_tripItem_textView)));
    }

    @Test
    public void checkEditTextError(){
        onView(withId(R.id.main_searchButton_fab)).perform(click());
        onView(withId(R.id.main_destination_autocomplete_textView))
                .check(matches(hasErrorText(TestStrings.DESTINATION_ERROR_TEXT)));
    }

}
