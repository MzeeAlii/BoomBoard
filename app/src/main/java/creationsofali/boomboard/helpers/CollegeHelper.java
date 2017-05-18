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

    public static String getCollegeFull(String collegeAbbr) {
        switch (collegeAbbr) {
            case Constant.COLLEGE_CHSS:
                return Constant.COLLEGE_CHSS_FULL;

            case Constant.COLLEGE_CIVE:
                return Constant.COLLEGE_CIVE_FULL;

            case Constant.COLLEGE_CNMS:
                return Constant.COLLEGE_CNMS_FULL;

            case Constant.COLLEGE_COED:
                return Constant.COLLEGE_COED_FULL;

            case Constant.COLLEGE_COES:
                return Constant.COLLEGE_COES_FULL;

            case Constant.COLLEGE_COHAS:
                return Constant.COLLEGE_COHAS_FULL;

            default:
                return null;
        }
    }


    public static String getFacultyFull(String facultyAbbr) {

        switch (facultyAbbr) {

            // cive faculties
            case Constant.FAC_BIS:
                return Constant.FAC_BIS_FULL;

            case Constant.FAC_CE:
                return Constant.FAC_CE_FULL;

            case Constant.FAC_CIS:
                return Constant.FAC_CIS_FULL;

            case Constant.FAC_CS:
                return Constant.FAC_CS_FULL;

            case Constant.FAC_HIS:
                return Constant.FAC_HIS_FULL;

            case Constant.FAC_ICT_MCD:
                return Constant.FAC_ICT_MCD_FULL;

            case Constant.FAC_IS:
                return Constant.FAC_IS_FULL;

            case Constant.FAC_MTA:
                return Constant.FAC_MTA_FULL;

            case Constant.FAC_SE:
                return Constant.FAC_SE_FULL;

            case Constant.FAC_TE:
                return Constant.FAC_TE_FULL;

            case Constant.FAC_VE:
                return Constant.FAC_VE_FULL;
            // end cive faculties

            default:
                return null;

        }
    }
}
