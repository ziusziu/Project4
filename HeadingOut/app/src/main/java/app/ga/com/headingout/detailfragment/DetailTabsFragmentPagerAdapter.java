package app.ga.com.headingout.detailfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.ga.com.headingout.detailfragment.tabfragment.DetailFlightTabFragment;
import app.ga.com.headingout.detailfragment.tabfragment.DetailHotelTabFragment;
import app.ga.com.headingout.inputfragment.tabfragment.InputWeatherTabFragment;

/**
 * Created by samsiu on 4/29/16.
 */
public class DetailTabsFragmentPagerAdapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"FLIGHT", "HOTELS", "WEATHER"};

    public DetailTabsFragmentPagerAdapter(FragmentManager fm) {
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
                return DetailFlightTabFragment.newInstance(position + 1);
            case 1:
                return DetailHotelTabFragment.newInstance(position + 1);
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
