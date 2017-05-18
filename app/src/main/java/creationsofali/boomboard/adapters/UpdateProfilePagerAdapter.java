package creationsofali.boomboard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import creationsofali.boomboard.fragments.UpdateCollegeFragment;
import creationsofali.boomboard.fragments.UpdateFacultyFragment;
import creationsofali.boomboard.helpers.CollegeHelper;

/**
 * Created by ali on 5/14/17.
 */

public class UpdateProfilePagerAdapter extends FragmentPagerAdapter {

    public UpdateProfilePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // college fragment
                return UpdateCollegeFragment.newInstance(CollegeHelper.getCollegeList(), position);
            case 1:
                return new UpdateFacultyFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
