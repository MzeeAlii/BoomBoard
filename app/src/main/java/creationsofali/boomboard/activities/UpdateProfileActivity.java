package creationsofali.boomboard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import creationsofali.boomboard.R;
import creationsofali.boomboard.adapters.UpdateProfilePagerAdapter;
import creationsofali.boomboard.appfonts.MyCustomAppFont;
import creationsofali.boomboard.datamodels.Constant;
import creationsofali.boomboard.datamodels.Student;
import creationsofali.boomboard.helpers.SharedPreferenceEditor;

/**
 * Created by ali on 5/13/17.
 */

public class UpdateProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;
    ViewPager viewPager;

    Student student = new Student();
    private static final String TAG = "UpdateProfileActivity";
    private boolean isFromSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        isFromSplash = getIntent().getBooleanExtra("isFromSplash", false);

        if (!isFromSplash) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            }
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new UpdateProfilePagerAdapter(getSupportFragmentManager()));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected = " + position);
                switch (position) {
                    case 0:
                        fab.setVisibility(View.GONE);
                        break;
                    case 1:
                        fab.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isProfileComplete()) {
                    // update profile
                    new SharedPreferenceEditor(UpdateProfileActivity.this).execute(student);

                    if (isFromSplash) {
                        Toast.makeText(UpdateProfileActivity.this, "Successful! Profile set up complete.", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // go to main activity
                                String gsonStudentProfile = new Gson().toJson(student);
                                startActivity(new Intent(UpdateProfileActivity.this, MainActivity.class)
                                        .putExtra("student", gsonStudentProfile));
                                // kill activity
                                finish();
                            }
                        }, 2000);
                    } else
                        // update successful!
                        Toast.makeText(UpdateProfileActivity.this, "Successful! Profile updated.", Toast.LENGTH_SHORT).show();
                } else
                    // show snackbar
                    showSnackbar("Please provide all required information.");
            }
        });


        // set my custom app font
        View rootView = findViewById(android.R.id.content);
        new MyCustomAppFont(getApplicationContext(), rootView);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean isProfileComplete() {

        if (student.getCollegeAbr() != null && student.getFacultyAbr() != null && student.getYearOfStudy() != 0) {
            return true;
        } else
            return false;

    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.coordinatorLayoutProfile),
                message,
                Snackbar.LENGTH_LONG).show();
    }

    public void setStudentCollege(String college) {
        // set up student profile
        student.setCollegeAbr(college);

        if (college != null)
            switch (college) {
                case Constant.COLLEGE_CHSS:
                    student.setCollegeFull(Constant.COLLEGE_CHSS_FULL);
                    break;

                case Constant.COLLEGE_CIVE:
                    student.setCollegeFull(Constant.COLLEGE_CIVE_FULL);
                    break;

                case Constant.COLLEGE_CNMS:
                    student.setCollegeFull(Constant.COLLEGE_CNMS_FULL);
                    break;

                case Constant.COLLEGE_COED:
                    student.setCollegeFull(Constant.COLLEGE_COED_FULL);
                    break;

                case Constant.COLLEGE_COES:
                    student.setCollegeFull(Constant.COLLEGE_COES_FULL);
                    break;

                case Constant.COLLEGE_COHAS:
                    student.setCollegeFull(Constant.COLLEGE_COHAS_FULL);
                    break;
            }
        else
            student.setCollegeFull(null);

        Log.d(TAG, "setStudentCollege: collegeAbr = " + student.getCollegeAbr()
                + ", collegeFull = " + student.getCollegeFull());
    }

    public void setStudentYear(int year) {
        student.setYearOfStudy(year);
        Log.d(TAG, "setStudentYear: yearOfStudy = " + student.getYearOfStudy());
    }

    public void setStudentFaculty(String facultyAbr) {
        student.setFacultyAbr(facultyAbr);

        if (facultyAbr != null)
            switch (facultyAbr) {
                // cive faculties
                case Constant.FAC_BIS:
                    student.setFacultyFull(Constant.FAC_BIS_FULL);
                    break;

                case Constant.FAC_CE:
                    student.setFacultyFull(Constant.FAC_CE_FULL);
                    break;

                case Constant.FAC_CIS:
                    student.setFacultyFull(Constant.FAC_CIS_FULL);
                    break;

                case Constant.FAC_CS:
                    student.setFacultyFull(Constant.FAC_CS_FULL);
                    break;

                case Constant.FAC_HIS:
                    student.setFacultyFull(Constant.FAC_HIS_FULL);
                    break;

                case Constant.FAC_ICT_MCD:
                    student.setFacultyFull(Constant.FAC_ICT_MCD_FULL);
                    break;

                case Constant.FAC_IS:
                    student.setFacultyFull(Constant.FAC_IS_FULL);
                    break;

                case Constant.FAC_MTA:
                    student.setFacultyFull(Constant.FAC_MTA_FULL);
                    break;

                case Constant.FAC_SE:
                    student.setFacultyFull(Constant.FAC_SE_FULL);
                    break;

                case Constant.FAC_TE:
                    student.setFacultyFull(Constant.FAC_TE_FULL);
                    break;

                case Constant.FAC_VE:
                    student.setFacultyFull(Constant.FAC_VE_FULL);
                    break;
                // end cive faculties
            }
        else
            student.setFacultyFull(null);


        Log.d(TAG, "setStudentFaculty: facultyAbbr = " + student.getFacultyAbr()
                + ", facultyFull = " + student.getFacultyFull());
    }
}
