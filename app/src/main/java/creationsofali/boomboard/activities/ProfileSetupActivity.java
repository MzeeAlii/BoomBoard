package creationsofali.boomboard.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import creationsofali.boomboard.R;
import creationsofali.boomboard.adapters.ProfileSetupPagerAdapter;
import creationsofali.boomboard.appfonts.MyCustomAppFont;
import creationsofali.boomboard.datamodels.Student;
import creationsofali.boomboard.helpers.CollegeHelper;
import creationsofali.boomboard.helpers.NetworkHelper;
import creationsofali.boomboard.helpers.SharedPreferenceEditor;
import dmax.dialog.SpotsDialog;

/**
 * Created by ali on 5/13/17.
 */

public class ProfileSetupActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fabDone;
    ViewPager viewPager;
    TabLayout tabs;
    Animation animationFabShow, animationFabHide;

    DatabaseReference studentProfileReference;
    FirebaseAuth firebaseAuth;

    Student student = new Student();
    private static final String TAG = "ProfileSetupActivity";
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

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            // initialize database ref for student profile
            FirebaseUser user = firebaseAuth.getCurrentUser();
            studentProfileReference = FirebaseDatabase.getInstance().getReference()
                    .child("students")
                    .child(user.getUid())
                    .child("profile");

            student.setEmail(user.getEmail());
            student.setUid(user.getUid());
        }

        animationFabShow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fab_show_fast);
        animationFabHide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fab_hide_fast);

        fabDone = (FloatingActionButton) findViewById(R.id.fab);
        fabDone.startAnimation(animationFabHide);
        fabDone.setEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ProfileSetupPagerAdapter(getSupportFragmentManager(), ProfileSetupActivity.this));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected = " + position);
                switch (position) {
                    case 0:
                        fabDone.startAnimation(animationFabHide);
                        fabDone.setEnabled(false);
                        break;
                    case 1:
                        fabDone.startAnimation(animationFabShow);
                        fabDone.setEnabled(true);
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


        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isProfileComplete()) {
                    // update profile in firebase database
                    if (NetworkHelper.isOnline(ProfileSetupActivity.this)) {
                        // online
                        // show progress
                        final SpotsDialog spotsDialog = new SpotsDialog(
                                ProfileSetupActivity.this,
                                getString(R.string.updating_profile),
                                R.style.SignInDialogStyle);
                        spotsDialog.setCancelable(false);
                        spotsDialog.show();

                        // saving profile to database
                        studentProfileReference.setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // check if college has changed before updating
                                boolean isCollegeChanged = isCollegeChanged();

                                // update profile in shared preference
                                updateProfileSharedPreferences();
                                spotsDialog.dismiss();


                                if (!isFromMain) {
                                    showSnackbar("Profile set up complete.");
                                    // go to main
                                    gotoMain();

                                } else
                                    // update successful
                                    showSnackbar("Successful! Profile updated.");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                spotsDialog.dismiss();
                                // update failed
                                showSnackbar("Failed!" + e.getMessage());
                            }
                        });
                    } else  // offline
                        showSnackbar("Device is offline.");

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

        return (student.getCollegeAbr() != null
                && student.getFacultyAbr() != null
                && student.getYearOfStudy() != 0);
    }


    private void showWelcomeDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_welcome, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileSetupActivity.this);
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
                Snackbar.LENGTH_INDEFINITE)
                .setDuration(4000)
                .show();
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

        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(ProfileSetupActivity.this);
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


    private void gotoMain() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // go to main activity
                String gsonStudentProfile = new Gson().toJson(student);
                startActivity(new Intent(ProfileSetupActivity.this, MainActivity.class)
                        .putExtra("student", gsonStudentProfile));

                // kill activity
                finish();
            }
        }, 1000);
    }


    // start task to update profile in shared preferences
    private void updateProfileSharedPreferences() {
        new SharedPreferenceEditor(ProfileSetupActivity.this).execute(student);
    }


    // check if college has changed
    private boolean isCollegeChanged() {
        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        String gsonStudent = sharedPreferences.getString("student", null);

        if (gsonStudent != null) {
            Student oldProfile = new Gson().fromJson(gsonStudent, Student.class);

            if (oldProfile.getCollegeAbr().equals(student.getCollegeAbr())) {
                Log.d(TAG, "same college " + student.getCollegeAbr());
                return false;

            } else {
                Log.d(TAG, "different college " + oldProfile.getCollegeAbr() + " -> " + student.getCollegeAbr());
                return true;
            }
        }

        return true;
    }


}
