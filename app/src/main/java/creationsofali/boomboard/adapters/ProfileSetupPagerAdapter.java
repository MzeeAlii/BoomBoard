package creationsofali.boomboard.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import creationsofali.boomboard.R;
import creationsofali.boomboard.fragments.UpdateCollegeFragment;
import creationsofali.boomboard.fragments.UpdateFacultyFragment;
import creationsofali.boomboard.helpers.CollegeHelper;

/**
 * Created by ali on 5/14/17.
 */

public class ProfileSetupPagerAdapter extends FragmentPagerAdapter {

    Context context;

    public ProfileSetupPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
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

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.college);
            case 1:
                return context.getString(R.string.faculty);
            default:
                return super.getPageTitle(position);
        }
    }
}
