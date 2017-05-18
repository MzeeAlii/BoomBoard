package creationsofali.boomboard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import creationsofali.boomboard.R;
import creationsofali.boomboard.activities.UpdateProfileActivity;
import creationsofali.boomboard.helpers.CollegeHelper;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by ali on 5/15/17.
 */

public class UpdateFacultyFragment extends Fragment {

    MaterialSpinner spinnerFaculty;
    ArrayAdapter<String> facultyAdapter;
    UpdateProfileActivity updateProfileActivity;

    List<String> facultyList = new ArrayList<>();
    private static final String TAG = "UpdateFacultyFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facultyAdapter = UpdateCollegeFragment.facultyAdapter;
        updateProfileActivity = (UpdateProfileActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_update_faculty, container, false);

        spinnerFaculty = (MaterialSpinner) fragmentView.findViewById(R.id.spinnerFaculty);
        spinnerFaculty.setAdapter(facultyAdapter);

        spinnerFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    // get selected faculty and update student object
                    updateProfileActivity.setStudentFaculty(adapterView.getSelectedItem().toString());
                } else {
                    // null
                    updateProfileActivity.setStudentFaculty(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.addAll(CollegeHelper.getYearList());
        MaterialSpinner yearSpinner = (MaterialSpinner) fragmentView.findViewById(R.id.spinnerYear);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    // do magic
                    updateProfileActivity.setStudentYear(Integer.valueOf(adapterView.getSelectedItem().toString()));
                } else {
                    // set 0
                    updateProfileActivity.setStudentYear(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return fragmentView;
    }

}
