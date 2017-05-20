package creationsofali.boomboard.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import creationsofali.boomboard.R;
import creationsofali.boomboard.activities.UpdateProfileActivity;
import creationsofali.boomboard.datamodels.Constant;
import creationsofali.boomboard.datamodels.Student;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    boolean isProfileAvailable = false;

    TextView textCollegeAbr, textCollegeFull, textFacultyAbr, textFacultyFull, textYearFull;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        textCollegeAbr = (TextView) fragmentView.findViewById(R.id.textCollegeAbr);
        textCollegeFull = (TextView) fragmentView.findViewById(R.id.textCollegeFull);
        textFacultyAbr = (TextView) fragmentView.findViewById(R.id.textFacultyAbr);
        textFacultyFull = (TextView) fragmentView.findViewById(R.id.textFacultyFull);
        textYearFull = (TextView) fragmentView.findViewById(R.id.textYearFull);


        fragmentView.findViewById(R.id.fabEditProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show dialog
                if (isProfileAvailable)
                    showEditProfileDialog();
                else
                    // start UpdateProfileActivity
                    startActivity(new Intent(getContext(), UpdateProfileActivity.class));

            }
        });

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // read student's profile from shared preferences
        new SharedPreferenceReader(getContext()).execute();
    }

    private void showEditProfileDialog() {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.layout_dialog_edit_profile, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialogView.findViewById(R.id.textOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to edit profile activity
                startActivity(new Intent(getContext(), UpdateProfileActivity.class));
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dismiss dialog
                dialog.dismiss();
            }
        });

    }

    private class SharedPreferenceReader extends AsyncTask<Void, Void, String> {
        private Context context;
        private static final String TAG = "SharedPreferenceReader";

        SharedPreferenceReader(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    context.getString(R.string.app_name),
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
            textFacultyFull.setText(student.getFacultyFull());
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


}
