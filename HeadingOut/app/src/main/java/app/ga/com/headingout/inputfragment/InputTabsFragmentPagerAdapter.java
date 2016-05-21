package app.ga.com.headingout.inputfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.ga.com.headingout.inputfragment.tabfragment.InputFlightTabFragment;
import app.ga.com.headingout.inputfragment.tabfragment.InputHotelTabFragment;
import app.ga.com.headingout.inputfragment.tabfragment.InputWeatherTabFragment;

/**
 * Created by samsiu on 4/29/16.
 */
public class InputTabsFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = InputTabsFragmentPagerAdapter.class.getSimpleName();

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"FLIGHT", "HOTELS", "WEATHER"};

    public InputTabsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return InputFlightTabFragment.newInstance(position + 1);
            case 1:
                return InputHotelTabFragment.newInstance(position + 1);
            case 2:
                return InputWeatherTabFragment.newInstance(position + 1);
            default:
                return InputWeatherTabFragment.newInstance(position + 1);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
