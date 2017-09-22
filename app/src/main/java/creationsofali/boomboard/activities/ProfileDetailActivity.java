package creationsofali.boomboard.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.Map;

import creationsofali.boomboard.R;
import creationsofali.boomboard.datamodels.Constant;
import creationsofali.boomboard.datamodels.Student;
import creationsofali.boomboard.helpers.CollegeHelper;

public class ProfileDetailActivity extends AppCompatActivity {


    boolean isProfileAvailable = false;

    TextView textProfileName, textProfileEmail, textCollegeAbr,
            textCollegeFull, textFacultyAbr, textFacultyFull, textYearFull, textToolbarTitle;
    Button buttonSignOut;
    ImageView imageProfileDp;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;

    private FirebaseAuth firebaseAuth;

    private boolean isToolbarTitleShown;
    private static final String TAG = ProfileDetailActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        textProfileEmail = findViewById(R.id.textEmail);
        textProfileName = findViewById(R.id.textUsername);
        textCollegeAbr = findViewById(R.id.textCollegeAbr);
        textCollegeFull = findViewById(R.id.textCollegeFull);
        textFacultyAbr = findViewById(R.id.textFacultyAbr);
        textFacultyFull = findViewById(R.id.textFacultyFull);
        textYearFull = findViewById(R.id.textYearFull);
        imageProfileDp = findViewById(R.id.imageDisplayPhoto);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        textToolbarTitle = findViewById(R.id.textToolbarTitle);
        textToolbarTitle.setText("");

        setInitialProfileDetails();

        final Animation animFadeIn = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.anim_fade_in);
        final Animation animFadeOut = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.anim_fade_out);

        textToolbarTitle.startAnimation(animFadeOut);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG, "offset = " + verticalOffset);
                // verticalOffset == 0 : fully expanded
                // Math.abs(verticalOffset) == appBarLayout.getTotalScrollingRange : fully collapsed

                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    Log.d(TAG, "onOffsetChanged: appBarLayout fully collapsed");
                    Log.d(TAG, "onOffsetChanged: totalScrollRange = " + appBarLayout.getTotalScrollRange());

                    // show title with animation
                    isToolbarTitleShown = true;
                    textToolbarTitle.startAnimation(animFadeIn);

                } else if (verticalOffset == 0) {
                    Log.d(TAG, "onOffsetChanged: appBarLayout fully expanded");

                } else {

                    // hide title with animation
                    if (isToolbarTitleShown) {
                        isToolbarTitleShown = false;
                        textToolbarTitle.startAnimation(animFadeOut);
                    }
                }
            }
        });


        findViewById(R.id.fabEditProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show dialog
                // if (isProfileAvailable)
                //     showEditProfileDialog();
                //  else
                // start ProfileSetupActivity
                startActivity(new Intent(ProfileDetailActivity.this, ProfileSetupActivity.class)
                        .putExtra("hasParentActivity", true));

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        new SharedPreferenceReader().execute();
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


    private class SharedPreferenceReader extends AsyncTask<Void, Void, String> {
        //        private Context context;
        private static final String TAG = "SharedPreferenceReader";

//        SharedPreferenceReader(Context context) {
//            this.context = context;
//        }

        @Override
        protected String doInBackground(Void... params) {
            SharedPreferences sharedPreferences = getSharedPreferences(
                    getString(R.string.app_name),
                    Context.MODE_PRIVATE);
            String student = sharedPreferences.getString("student", Constant.NO_PROFILE_FOUND);

            Log.d(TAG, "doInBackground:studentProfile: " + student);

            return student;
        }

        @Override
        protected void onPostExecute(String gsonStudent) {
            super.onPostExecute(gsonStudent);

            updateProfileView(gsonStudent);
        }
    }

    public void setInitialProfileDetails() {
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            textProfileName.setText(user.getDisplayName());
            textProfileEmail.setText(user.getEmail());
            textToolbarTitle.setText(user.getDisplayName());
            Glide.with(ProfileDetailActivity.this).load(user.getPhotoUrl()).into(imageProfileDp);
        }
    }

    private void updateProfileView(String gsonStudent) {

        if (gsonStudent.equals(Constant.NO_PROFILE_FOUND)) {
            isProfileAvailable = false;

            textCollegeAbr.setText(R.string.college);
            textCollegeFull.setText(R.string.unspecified);

            textFacultyAbr.setText(R.string.faculty);
            textFacultyFull.setText(R.string.unspecified);

            textYearFull.setText(R.string.unspecified);

        } else {
            isProfileAvailable = true;
            Student student = new Gson().fromJson(gsonStudent, Student.class);

            textCollegeAbr.setText(student.getCollegeAbr());
            textCollegeFull.setText(student.getCollegeFull());
            textFacultyAbr.setText(student.getFacultyAbr());
            Map<String, String> facultyMap =
                    CollegeHelper.getCollegeFacultiesMap(student.getCollegeAbr());
            textFacultyFull.setText(facultyMap.get(student.getFacultyAbr()));

            int year = student.getYearOfStudy();
            if (year == 1)
                textYearFull.setText(R.string.first_year);

            else if (year == 2)
                textYearFull.setText(R.string.second_year);

            else if (year == 3)
                textYearFull.setText(R.string.third_year);

            else if (year == 4)
                textYearFull.setText(R.string.fourth_year);

            else if (year == 5)
                textYearFull.setText(R.string.fifth_year);
        }
    }

    public void showSignOutDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_sign_out, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileDetailActivity.this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        TextView textHeaderName = dialogView.findViewById(R.id.textHeaderName);
        ImageView imageDialogDp = dialogView.findViewById(R.id.imageDialogDp);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            textHeaderName.setText(user.getDisplayName());
            Glide.with(ProfileDetailActivity.this).load(user.getPhotoUrl()).into(imageDialogDp);
        }
        // positive button
        dialogView.findViewById(R.id.textOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sign out the user
                signOut();
            }
        });
        // negative button
        dialogView.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dismiss dialog
                dialog.dismiss();
            }
        });

    }

    private void signOut() {
        firebaseAuth.signOut();
        // go signInActivity
        startActivity(new Intent(ProfileDetailActivity.this, SignInActivity.class)
                .putExtra("isToForceClose", true));
        finish();
    }


}
