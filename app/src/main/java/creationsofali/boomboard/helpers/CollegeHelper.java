package creationsofali.boomboard.helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import creationsofali.boomboard.datamodels.Constant;

/**
 * Created by ali on 5/14/17.
 */

public class CollegeHelper {

    private static final String TAG = "CollegeHelper";

    public static List<String> getCollegeList() {
        List<String> collegeList = new ArrayList<>();
        collegeList.add("CHSS");
        collegeList.add("CIVE");
        collegeList.add("CNMS");
        collegeList.add("COED");
        collegeList.add("COES");
        collegeList.add("COHAS");

        return collegeList;
    }

    public static List<String> getFacultyList(String college) {
        List<String> facultyList = new ArrayList<>();
        switch (college) {
            case Constant.COLLEGE_CHSS:
                // add chss faculties
                break;

            case Constant.COLLEGE_CIVE:
                // add cive faculties
                facultyList.add(Constant.FAC_BIS);
                facultyList.add(Constant.FAC_CE);
                facultyList.add(Constant.FAC_CIS);
                facultyList.add(Constant.FAC_CS);
                facultyList.add(Constant.FAC_HIS);
                facultyList.add(Constant.FAC_ICT_MCD);
                facultyList.add(Constant.FAC_IS);
                facultyList.add(Constant.FAC_MTA);
                facultyList.add(Constant.FAC_SE);
                facultyList.add(Constant.FAC_TE);
                facultyList.add(Constant.FAC_VE);
                break;

            case Constant.COLLEGE_CNMS:
                // add cnms faculties
                break;

            case Constant.COLLEGE_COED:
                // add coed faculties
                break;

            case Constant.COLLEGE_COES:
                // add coes faculties
                break;

            case Constant.COLLEGE_COHAS:
                // add cohas faculties
                break;
        }

        Log.d(TAG, "facultyList = " + facultyList.size());
        return facultyList;
    }

    public static List<String> getYearList() {
        List<String> yearList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            yearList.add(String.valueOf(i + 1));
        }

        return yearList;
    }
}
