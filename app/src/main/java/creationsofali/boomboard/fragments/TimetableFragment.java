package creationsofali.boomboard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import creationsofali.boomboard.R;

/**
 * Created by ali on 5/8/17.
 */

public class TimetableFragment extends Fragment {
    private String args;

    // constructor for creating a fragment with arguments
    public static TimetableFragment newInstance(String someArgs) {
        TimetableFragment timetableFragment = new TimetableFragment();
        Bundle args = new Bundle();
        args.putString("args", someArgs);
        timetableFragment.setArguments(args);

        return timetableFragment;
    }

    // get items only once
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments().getString("args");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_timetable, container, false);

        TextView textView = fragmentView.findViewById(R.id.someText);
        textView.setText(args);

        return fragmentView;
    }

}
