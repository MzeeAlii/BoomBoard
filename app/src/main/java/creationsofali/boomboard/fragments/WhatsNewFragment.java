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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import creationsofali.boomboard.adapters.CollapseNotesAdapter;
import creationsofali.boomboard.adapters.NotesAdapter;
import creationsofali.boomboard.datamodels.Note;
import creationsofali.boomboard.helpers.NetworkHelper;
import creationsofali.boomboard.helpers.TopBottomSpaceDecoration;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhatsNewFragment extends Fragment {


    RecyclerView whatsNewRecycler;
    RecyclerView.LayoutManager layoutManager;
    NotesAdapter notesAdapter;
    ProgressBar progressBar;
    LinearLayout layoutDeviceOffline;
    TextView textOnBoardThisWeek;

    List<Note> noteList = new ArrayList<>();

    DatabaseReference notesDatabaseReference;
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

        progressBar = (ProgressBar) fragmentView.findViewById(R.id.progressBar);
        textOnBoardThisWeek = (TextView) fragmentView.findViewById(R.id.textOnBoardThisWeek);
        textOnBoardThisWeek.setText("Loading...");
        layoutDeviceOffline = (LinearLayout) fragmentView.findViewById(R.id.layoutDeviceOffline);
        //layoutDeviceOffline.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        whatsNewRecycler = (RecyclerView) fragmentView.findViewById(R.id.whatsNewRecyclerList);
        whatsNewRecycler.setLayoutManager(layoutManager);

        scaleInTopAnimator = new ScaleInTopAnimator(new OvershootInterpolator(1f));
        scaleInTopAnimator.setAddDuration(1000);
        scaleInTopAnimator.setRemoveDuration(1000);
        scaleInTopAnimator.setMoveDuration(1000);
        scaleInTopAnimator.setChangeDuration(1000);
        whatsNewRecycler.setItemAnimator(scaleInTopAnimator);

        notesAdapter = new NotesAdapter(noteList, getContext());
        whatsNewRecycler.setAdapter(notesAdapter);

        // decorations
        int topSpace = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                getResources().getDisplayMetrics());
        int bottomSpace = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                64,
                getResources().getDisplayMetrics());

        whatsNewRecycler.addItemDecoration(new TopBottomSpaceDecoration(bottomSpace, topSpace));

        notesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        // listeners
        notesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Note note = dataSnapshot.getValue(Note.class);
                noteList.add(note);
                //scaleInAnimationAdapter.notifyItemInserted(noteList.size());
                notesAdapter.notifyItemInserted(noteList.size());
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
                onRetrieveComplete();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        // attach listeners and retrieve from database
        startAsyncTask();

//        fragmentView.findViewById(R.id.fabRefresh).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "Refreshing...", Toast.LENGTH_SHORT).show();
//                startAsyncTask();
//                // for appBarRecycler
//                ((MainActivity) getActivity()).startAsyncTaskForAppBarRecycler();
//            }
//        });

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
            notesDatabaseReference.removeEventListener(notesValueEventListener);

        if (notesChildEventListener != null)
            notesDatabaseReference.removeEventListener(notesChildEventListener);
    }

    private class NotesRetrieveTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            noteList.clear();
            //scaleInAnimationAdapter.notifyDataSetChanged();
            notesAdapter.notifyDataSetChanged();
            textOnBoardThisWeek.setText("Loading...");

            if (progressBar.getVisibility() != View.VISIBLE)
                progressBar.setVisibility(View.VISIBLE);

            if (layoutDeviceOffline.getVisibility() == View.VISIBLE)
                layoutDeviceOffline.setVisibility(View.GONE);

        }


        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (NetworkHelper.isOnline(getContext().getApplicationContext())) {
                // device online
                // attach listeners to database ref
                notesDatabaseReference.addChildEventListener(notesChildEventListener);
                notesDatabaseReference.addValueEventListener(notesValueEventListener);
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
                progressBar.setVisibility(View.GONE);
            }
        }
    }


    private void onRetrieveComplete() {
        progressBar.setVisibility(View.GONE);
        textOnBoardThisWeek.setText("This Week On Board");
    }

    public void startAsyncTask() {
        new NotesRetrieveTask().execute();
    }

}
