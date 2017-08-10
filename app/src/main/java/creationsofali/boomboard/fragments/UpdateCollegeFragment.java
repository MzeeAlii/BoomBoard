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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import creationsofali.boomboard.R;
import creationsofali.boomboard.activities.ProfileSetupActivity;
import creationsofali.boomboard.datamodels.Constant;
import creationsofali.boomboard.helpers.CollegeHelper;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by ali on 5/14/17.
 */

public class UpdateCollegeFragment extends Fragment {

    MaterialSpinner spinnerCollege;
    TextView textChoose;
    ArrayAdapter<String> collegeAdapter;
    public static ArrayAdapter<String> facultyAdapter;
    private static Gson gson = new Gson();
    private ProfileSetupActivity profileSetupActivity;

    List<String> collegeList;
    int position;
    private static final String TAG = "UpdateCollegeFragment";

    public UpdateCollegeFragment() {
        // empty constructor
    }

    // constructor for creating a fragment with arguments
    public static UpdateCollegeFragment newInstance(List<String> itemList, int position) {
        UpdateCollegeFragment updateCollegeFragment = new UpdateCollegeFragment();
        Bundle args = new Bundle();
        String spinnerItems = gson.toJson(itemList);
        args.putString("items", spinnerItems);
        args.putInt("position", position);
        updateCollegeFragment.setArguments(args);

        return updateCollegeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");

        String gsonItems = getArguments().getString("items");
        collegeList = gson.fromJson(gsonItems, new TypeToken<List<String>>() {
        }.getType());
        Log.d(TAG, "onCreate: collegeList = " + collegeList.size() + ", position = " + position);

        facultyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item);
        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        profileSetupActivity = (ProfileSetupActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_update_college, container, false);

        collegeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, collegeList);
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCollege = (MaterialSpinner) fragmentView.findViewById(R.id.spinnerCollege);
        spinnerCollege.setAdapter(collegeAdapter);
        Log.d(TAG, "onCreateView: adapter = collegeAdapter, position = " + position);

        spinnerCollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    // get selected college
                    String college = adapterView.getSelectedItem().toString();
                    Log.d(TAG, "onItemSelected: selectedItem = "
                            + college
                            + ", index = " + i);

                    facultyAdapter.clear();
                    facultyAdapter.addAll(CollegeHelper.getFacultyList(college));
                    Log.d(TAG, "onCreateView: adapter = facultyAdapter#dataSetChanged:items = " + facultyAdapter.getCount());

                    // update profile object
                    profileSetupActivity.setStudentCollege(college);
                    profileSetupActivity.updateFacultyMap(college);

                    // change floating label
                    switch (college) {
                        case Constant.COLLEGE_CHSS:
                            spinnerCollege.setFloatingLabelText(Constant.COLLEGE_CHSS_FULL);
                            break;

                        case Constant.COLLEGE_CIVE:
                            spinnerCollege.setFloatingLabelText(Constant.COLLEGE_CIVE_FULL);
                            break;

                        case Constant.COLLEGE_CNMS:
                            spinnerCollege.setFloatingLabelText(Constant.COLLEGE_CNMS_FULL);
                            break;

                        case Constant.COLLEGE_COED:
                            spinnerCollege.setFloatingLabelText(Constant.COLLEGE_COED_FULL);
                            break;

                        case Constant.COLLEGE_COES:
                            spinnerCollege.setFloatingLabelText(Constant.COLLEGE_COES_FULL);
                            break;

                        case Constant.COLLEGE_COHAS:
                            spinnerCollege.setFloatingLabelText(Constant.COLLEGE_COHAS_FULL);
                            break;
                    }
                } else {
                    // nothing selected
                    facultyAdapter.clear();

                    Log.d(TAG, "onItemSelected: selectedItem = "
                            + adapterView.getSelectedItem()
                            + ", index = " + i);

                    // update profile object
                    ((ProfileSetupActivity) getActivity())
                            .setStudentCollege(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return fragmentView;
    }
}
