package creationsofali.boomboard.activities;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import creationsofali.boomboard.R;
import creationsofali.boomboard.adapters.TimetableAdapter;
import creationsofali.boomboard.appfonts.MyCustomAppFont;
import creationsofali.boomboard.datamodels.ClassSession;

public class TimetableActivity extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressBar;
    ViewPager viewPager;
    // CircleIndicator circleIndicator;

    DatabaseReference timetableDatabaseReference;

    List<ClassSession> sessionList = new ArrayList<>();

    static String TAG = TimetableActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        toolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        viewPager = findViewById(R.id.viewPager);

        final TimetableAdapter timetableAdapter = new TimetableAdapter(
                getSupportFragmentManager(),
                getArgsList());

        viewPager.setAdapter(timetableAdapter);
        // circleIndicator = (CircleIndicator) findViewById(R.id.circleIndicator);
        // circleIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        toolbar.setTitle("Monday");
                        break;
                    case 1:
                        toolbar.setTitle("Tuesday");
                        break;
                    case 2:
                        toolbar.setTitle("Wednesday");
                        break;
                    case 3:
                        toolbar.setTitle("Thursday");
                        break;
                    case 4:
                        toolbar.setTitle("Friday");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // root/timetable/cive/se/
        timetableDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("timetable")
                .child("cive")
                .child("se");

        timetableDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ClassSession session = snapshot.getValue(ClassSession.class);
                    session.setCode(snapshot.getKey());
                    sessionList.add(session);
                }

                timetableAdapter.notifyDataSetChanged();
                // hide progressBar
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

    @Override
    protected void onStart() {
        super.onStart();
        new WeekDayTask().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class WeekDayTask extends AsyncTask<Void, Void, String> {

        long today;

        @Override
        protected String doInBackground(Void... voids) {

            today = System.currentTimeMillis();
            SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);

            return weekDayFormat.format(today);
        }

        @Override
        protected void onPostExecute(String weekDay) {
            super.onPostExecute(weekDay);

            if (weekDay.equals("Saturday") || weekDay.equals("Sunday"))
                toolbar.setTitle("Monday");
            else
                toolbar.setTitle(weekDay);

            // 1 = mon, 7 = sun
            SimpleDateFormat dayNumbOfWeekFormat = new SimpleDateFormat("u", Locale.ENGLISH);
            int dayNumOfWeek = Integer.valueOf(dayNumbOfWeekFormat.format(today));

            if (dayNumOfWeek > 5) {
                // for saturday and sunday
                viewPager.setCurrentItem(0, true);  // show timetable of the next monday
            } else {
                // for other days, -1 since day number is 1 based   and viewPager is 0 based
                viewPager.setCurrentItem(dayNumOfWeek - 1, true);
            }

            Log.d(TAG, "WeekDayTask#onPostExecute: " + dayNumOfWeek + ":" + weekDay);
        }
    }

    private List<String> getArgsList() {
        List<String> list = new ArrayList<>();
        list.add("Monday Timetable!");
        list.add("Tuesday Timetable!");
        list.add("Wednesday Timetable!");
        list.add("Thursday Timetable!");
        list.add("Friday Timetable!");

        return list;
    }


}
