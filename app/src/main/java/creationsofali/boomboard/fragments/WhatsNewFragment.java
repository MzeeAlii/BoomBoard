package creationsofali.boomboard.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import creationsofali.boomboard.R;
import creationsofali.boomboard.activities.MainActivity;
import creationsofali.boomboard.adapters.NoticeAdapter;
import creationsofali.boomboard.datamodels.Notice;
import creationsofali.boomboard.datamodels.Student;
import creationsofali.boomboard.helpers.NetworkHelper;
import creationsofali.boomboard.helpers.SharedPreferenceHelper;
import creationsofali.boomboard.helpers.TopBottomSpaceDecoration;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhatsNewFragment extends Fragment {


    RecyclerView whatsNewRecycler;
    RecyclerView.LayoutManager layoutManager;
    NoticeAdapter noticeAdapter;
    LinearLayout layoutDeviceOffline, layoutNoNotices, layoutLoadingView;

    List<Notice> noticeList = new ArrayList<>();

    DatabaseReference noticesReference;
    ChildEventListener notesChildEventListener;
    ValueEventListener notesValueEventListener;

    // ScaleInAnimationAdapter scaleInAnimationAdapter;
    ScaleInTopAnimator scaleInTopAnimator;

    public WhatsNewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_whats_new, container, false);

        layoutDeviceOffline = fragmentView.findViewById(R.id.layoutDeviceOffline);
        layoutNoNotices = fragmentView.findViewById(R.id.layoutNoNotices);
        layoutLoadingView = fragmentView.findViewById(R.id.layoutLoadingView);

        layoutNoNotices.setVisibility(View.GONE);
        layoutDeviceOffline.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        whatsNewRecycler = fragmentView.findViewById(R.id.whatsNewRecyclerList);
        whatsNewRecycler.setLayoutManager(layoutManager);

        scaleInTopAnimator = new ScaleInTopAnimator(new OvershootInterpolator(1f));
        scaleInTopAnimator.setAddDuration(800);
        scaleInTopAnimator.setRemoveDuration(800);
        scaleInTopAnimator.setMoveDuration(800);
        scaleInTopAnimator.setChangeDuration(800);
        whatsNewRecycler.setItemAnimator(scaleInTopAnimator);

        noticeAdapter = new NoticeAdapter(noticeList, getContext());
        whatsNewRecycler.setAdapter(noticeAdapter);

        // decorations
        int topSpace = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                getResources().getDisplayMetrics());
        int bottomSpace = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                64,
                getResources().getDisplayMetrics());

        whatsNewRecycler.addItemDecoration(new TopBottomSpaceDecoration(bottomSpace, topSpace));

        // get student profile from shared preferences
        Student student = SharedPreferenceHelper.getStudentProfile(getContext());

        if (student != null)
            noticesReference = FirebaseDatabase.getInstance().getReference()
                    .child("notices")
                    .child(student.getCollegeAbr())
                    .child("global");

        // listeners
        notesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Notice notice = dataSnapshot.getValue(Notice.class);
                noticeList.add(notice);
                //scaleInAnimationAdapter.notifyItemInserted(noticeList.size());
                noticeAdapter.notifyItemInserted(noticeList.size());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        notesValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // onRetrieveComplete
                // hide progressBar and shit
                if ((int) dataSnapshot.getChildrenCount() == 0)
                    onNoNotices();
                else
                    onRetrieveComplete();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        // attach listeners and retrieve from database
        startAsyncTask();

        return fragmentView;
    }

    @Override
    public void onStop() {
        super.onStop();
        // detach listeners from database ref
        detachDatabaseListeners();
    }

    private void detachDatabaseListeners() {
        if (notesValueEventListener != null)
            noticesReference.removeEventListener(notesValueEventListener);

        if (notesChildEventListener != null)
            noticesReference.removeEventListener(notesChildEventListener);
    }

    private class NotesRetrieveTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            noticeList.clear();
            //scaleInAnimationAdapter.notifyDataSetChanged();
            noticeAdapter.notifyDataSetChanged();

            if (layoutLoadingView.getVisibility() != View.VISIBLE)
                layoutLoadingView.setVisibility(View.VISIBLE);

            if (layoutDeviceOffline.getVisibility() == View.VISIBLE)
                layoutDeviceOffline.setVisibility(View.GONE);

            if (layoutNoNotices.getVisibility() == View.VISIBLE)
                layoutNoNotices.setVisibility(View.GONE);

        }


        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (NetworkHelper.isOnline(getContext())) {
                // device online
                // attach listeners to database ref
                noticesReference.addChildEventListener(notesChildEventListener);
                noticesReference.addValueEventListener(notesValueEventListener);
                return true;
            } else {
                // device offline
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isOnline) {
            super.onPostExecute(isOnline);
            if (!isOnline) {
                // show message, device offline, can't load or something like that
                layoutDeviceOffline.setVisibility(View.VISIBLE);
                layoutLoadingView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackbarAction("Failed!");
            }
        }
    }


    private void onRetrieveComplete() {
        layoutLoadingView.setVisibility(View.GONE);
        layoutNoNotices.setVisibility(View.GONE);
    }

    private void onNoNotices() {
        layoutLoadingView.setVisibility(View.GONE);
        layoutDeviceOffline.setVisibility(View.GONE);
        layoutNoNotices.setVisibility(View.VISIBLE);
    }

    public void startAsyncTask() {
        new NotesRetrieveTask().execute();
    }

    public void clearNoticeList() {
        noticeList.clear();
    }

}
