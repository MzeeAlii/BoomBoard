package creationsofali.boomboard.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.gson.Gson;

import creationsofali.boomboard.R;
import creationsofali.boomboard.adapters.UpdateProfilePagerAdapter;
import creationsofali.boomboard.appfonts.MyCustomAppFont;
import creationsofali.boomboard.datamodels.Student;
import creationsofali.boomboard.helpers.CollegeHelper;
import creationsofali.boomboard.helpers.SharedPreferenceEditor;

/**
 * Created by ali on 5/13/17.
 */

public class UpdateProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;
    ViewPager viewPager;
    TabLayout tabs;
    Animation animationFabShow, animationFabHide;

    Student student = new Student();
    private static final String TAG = "UpdateProfileActivity";
    private boolean isFromMain;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        isFromMain = getIntent().getBooleanExtra("isFromMain", false);

        if (isFromMain) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            }
        } else {
            // isFromMain:true | user runs the app for the very 1st time
            // show welcome dialog
            showWelcomeDialog();
        }

        animationFabShow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fab_show_fast);
        animationFabHide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fab_hide_fast);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.startAnimation(animationFabHide);
        fab.setEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new UpdateProfilePagerAdapter(getSupportFragmentManager(), UpdateProfileActivity.this));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected = " + position);
                switch (position) {
                    case 0:
                        fab.startAnimation(animationFabHide);
                        fab.setEnabled(false);
                        break;
                    case 1:
                        fab.startAnimation(animationFabShow);
                        fab.setEnabled(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.getTabAt(0).setIcon(R.drawable.ic_temple);
        tabs.getTabAt(1).setIcon(R.drawable.ic_graduation_hat);

        //noinspection deprecation
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // icon color for selected tab
                tab.getIcon().setColorFilter(
                        ContextCompat.getColor(getApplicationContext(), R.color.color_white),
                        PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // icon color for unselected tab
                tab.getIcon().setColorFilter(
                        ContextCompat.getColor(getApplicationContext(), R.color.color_primary_light_amber),
                        PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isProfileComplete()) {
                    // update profile
                    new SharedPreferenceEditor(UpdateProfileActivity.this).execute(student);

                    if (!isFromMain) {
                        Toast.makeText(UpdateProfileActivity.this,
                                "Profile set up complete.",
                                Toast.LENGTH_SHORT).show();
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
                        }, 1000);
                    } else
                        // update successful!
                        Toast.makeText(UpdateProfileActivity.this,
                                "Successful! Profile updated.",
                                Toast.LENGTH_SHORT).show();
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


    private void showWelcomeDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_welcome, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateProfileActivity.this);
        dialogBuilder
                .setView(dialogView)
                .setCancelable(false);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialogView.findViewById(R.id.textOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // close dialog
                dialog.dismiss();
            }
        });
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
            student.setCollegeFull(CollegeHelper.getCollegeFull(college));
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
            student.setFacultyFull(CollegeHelper.getFacultyFull(facultyAbr));
        else
            student.setFacultyFull(null);

        Log.d(TAG, "setStudentFaculty: facultyAbbr = " + student.getFacultyAbr()
                + ", facultyFull = " + student.getFacultyFull());
    }

    public void showSignInDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_dialog_sign_in, null);

        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(UpdateProfileActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(true);
//                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // isOkClicked = false;
//                        dialogInterface.dismiss();
//                       // showMessageSnackbar("Sign in cancelled!");
//                    }
//                });

        final android.app.AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialogView.findViewById(R.id.cardButtonUsingGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // isOkClicked = true;
                dialog.dismiss();
                //Intent authUiIntent = getAuthUiIntent();
                // Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent();

                // startActivityForResult(getGoogleAuthIntent(), RC_SIGN_IN);
            }
        });
    }


}
