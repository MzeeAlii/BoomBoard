package creationsofali.boomboard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import creationsofali.boomboard.fragments.TimetableFragment;

/**
 * Created by ali on 5/8/17.
 */

public class TimetableAdapter extends FragmentPagerAdapter {
    List<String> argsList;

    public TimetableAdapter(FragmentManager fragmentManager, List<String> argsList) {
        super(fragmentManager);
        this.argsList = argsList;
    }

    private static final int NO_OF_DAYS = 5;

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                // monday
                return TimetableFragment.newInstance(argsList.get(position));
            case 1:
                // tuesday
                return TimetableFragment.newInstance(argsList.get(position));
            case 2:
                // wednesday
                return TimetableFragment.newInstance(argsList.get(position));
            case 3:
                // thursday
                return TimetableFragment.newInstance(argsList.get(position));
            case 4:
                // friday
                return TimetableFragment.newInstance(argsList.get(position));
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NO_OF_DAYS;
    }
}
