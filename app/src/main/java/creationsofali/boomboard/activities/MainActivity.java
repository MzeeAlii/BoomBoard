package creationsofali.boomboard.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
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
import creationsofali.boomboard.adapters.CollapseNotesAdapter;
import creationsofali.boomboard.appfonts.MyCustomAppFont;
import creationsofali.boomboard.datamodels.Note;
import creationsofali.boomboard.fragments.AllOnBoardFragment;
import creationsofali.boomboard.fragments.ProfileFragment;
import creationsofali.boomboard.fragments.WhatsNewFragment;
import creationsofali.boomboard.helpers.DrawerTypefaceSpan;
import creationsofali.boomboard.helpers.EmailHelper;
import creationsofali.boomboard.helpers.NetworkHelper;
import creationsofali.boomboard.helpers.StartEndSpaceDecoration;
import creationsofali.boomboard.helpers.TwitterHelper;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationDrawer;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbar;
    FloatingActionButton fabRefresh;

    RecyclerView appBarRecycler;
    RecyclerView.LayoutManager layoutManagerHorizontal;
    CollapseNotesAdapter whatsNewAdapter;

    List<Note> appBarNoteList = new ArrayList<>();

    DatabaseReference notesDatabaseReference;
    ChildEventListener notesChildEventListener;
    ValueEventListener notesValueEventListener;

    ScaleInAnimationAdapter scaleInAnimationAdapter;

    boolean doubleBackToExitPressedOnce = false, isTimeTableReady = false;
    int DELAY_TIME = 2000;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setExpandedTitleColor(
                ContextCompat.getColor(MainActivity.this, android.R.color.transparent));

        fragmentManager = getSupportFragmentManager();
        // by default, set checked item onCreate
        final WhatsNewFragment whatsNewFragment = new WhatsNewFragment();
        fragmentManager.beginTransaction().replace(
                R.id.mainContentView,
                whatsNewFragment,
                whatsNewFragment.getTag()).commit();

        appBarLayout.setExpanded(true, true);

        fabRefresh = (FloatingActionButton) findViewById(R.id.fabRefresh);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawer = (NavigationView) findViewById(R.id.navigationDrawer);
        navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navWhatsNew:
                        // whatsNewFragment = new WhatsNewFragment();
                        fragmentManager.beginTransaction().replace(
                                R.id.mainContentView,
                                whatsNewFragment,
                                whatsNewFragment.getTag()).commit();

                        appBarLayout.setExpanded(true, true);
                        collapsingToolbar.setTitle(getString(R.string.app_name));
                        fabRefresh.setVisibility(View.VISIBLE);
                        break;

                    case R.id.navAllOnBoard:
                        AllOnBoardFragment allOnBoardFragment = new AllOnBoardFragment();
                        fragmentManager.beginTransaction().replace(
                                R.id.mainContentView,
                                allOnBoardFragment,
                                allOnBoardFragment.getTag()).commit();

                        appBarLayout.setExpanded(false, true);
                        collapsingToolbar.setTitle(item.getTitle());
                        fabRefresh.setVisibility(View.GONE);
                        break;

                    case R.id.navTimetable:
                        startActivity(new Intent(MainActivity.this, TimetableActivity.class));
                        break;

                    case R.id.navProfile:
                        ProfileFragment profileFragment = new ProfileFragment();
                        fragmentManager.beginTransaction().replace(
                                R.id.mainContentView,
                                profileFragment).commit();

                        appBarLayout.setExpanded(false, true);
                        collapsingToolbar.setTitle("Student's Profile");
//                        collapsingToolbar.setElevation();
                        fabRefresh.setVisibility(View.GONE);
                        break;

                    case R.id.navAbout:
                        showAboutDialog();
                        break;
                }

                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        drawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                R.string.drawer_opened,
                R.string.drawer_closed);
        drawerLayout.setDrawerListener(drawerToggle);

        // drawer items typeface
        new DrawerTypefaceSpanTask().execute(navigationDrawer.getMenu());

        // recyclerView in  collapsingToolbar
        onCreateAppBarRecycler();


        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // for appBarRecycler
                startAsyncTaskForAppBarRecycler();

                //for fragmentRecycler
                whatsNewFragment.startAsyncTask();
            }
        });

        // set my custom app font
        View rootView = findViewById(android.R.id.content);
        new MyCustomAppFont(getApplicationContext(), rootView);
    }


    private void onCreateAppBarRecycler() {
        layoutManagerHorizontal = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);

        appBarRecycler = (RecyclerView) findViewById(R.id.appBarRecycler);
        appBarRecycler.setLayoutManager(layoutManagerHorizontal);

        // decorations
        int startEndSpace = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                getResources().getDisplayMetrics());

        appBarRecycler.addItemDecoration(new StartEndSpaceDecoration(startEndSpace));

        whatsNewAdapter = new CollapseNotesAdapter(appBarNoteList, MainActivity.this);
        //appBarRecycler.setAdapter(whatsNewAdapter);

        scaleInAnimationAdapter = new ScaleInAnimationAdapter(whatsNewAdapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(1f));
        scaleInAnimationAdapter.setDuration(1000);
        // set animationAdapter
        appBarRecycler.setAdapter(scaleInAnimationAdapter);

        notesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        // listeners
        notesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Note note = dataSnapshot.getValue(Note.class);
                appBarNoteList.add(note);
                scaleInAnimationAdapter.notifyItemInserted(appBarNoteList.size());
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
                //onRetrieveComplete();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        // attach listeners and retrieve from database
        startAsyncTaskForAppBarRecycler();
    }


    public void startAsyncTaskForAppBarRecycler() {
        new NotesRetrieveTask().execute();
    }


    private class NotesRetrieveTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            appBarNoteList.clear();
            scaleInAnimationAdapter.notifyDataSetChanged();

//            if (progressBar.getVisibility() != View.VISIBLE)
//                progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (NetworkHelper.isOnline(MainActivity.this)) {
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
            }
        }
    }


    private void detachDatabaseListeners() {
        if (notesValueEventListener != null)
            notesDatabaseReference.removeEventListener(notesValueEventListener);

        if (notesChildEventListener != null)
            notesDatabaseReference.removeEventListener(notesChildEventListener);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // close drawer if open onBackPressed
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // double back to exit
            getSupportFragmentManager().popBackStack();

        } else if (!doubleBackToExitPressedOnce) {

            this.doubleBackToExitPressedOnce = true;
            // now ask for the second backPress to exit
            Toast.makeText(
                    getApplicationContext(),
                    R.string.press_again_exit,
                    Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // reset values after time delay
                    doubleBackToExitPressedOnce = false;
                }
            }, DELAY_TIME);

        } else {
            // default
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // detach listeners from database ref
        detachDatabaseListeners();
    }

    public void showAboutDialog() {
        final View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_about, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(true);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView textVersion = (TextView) dialogView.findViewById(R.id.textVersion);
        if (packageInfo != null) {
            // get and set version
            textVersion.setText(packageInfo.versionName);
        }

        TextView textRights = (TextView) dialogView.findViewById(R.id.textRights);
        String unicodeCopyRight = "\u00A9"; // or (char) 169 = 10101001 = 0x00A9
        String unicodeTradeMark = "\u2122"; // or (char) 8482 = 0x2122
        // (c) 2017 Ace Code Labs tm
        //      All Rights Reserved.
        textRights.setText(unicodeCopyRight);
        textRights.append(" 2017 " + "Ace Quad Apps" + unicodeTradeMark);
        textRights.append("\n" + "All Rights Reserved.");


        dialogView.findViewById(R.id.iconTwitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterHelper.launchTwitter("AceThaGemini", getApplicationContext());
            }
        });

        dialogView.findViewById(R.id.iconEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailHelper.launchComposeEmail("allysuley@gmail.com", getApplicationContext());
            }
        });

        dialogView.findViewById(R.id.textClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  onPositiveButtonClick
                dialog.dismiss();
            }
        });
    }

    private class DrawerTypefaceSpanTask extends AsyncTask<Menu, Void, Void> {
        @Override
        protected Void doInBackground(Menu... menus) {
            Menu drawerMenu = menus[0];
            for (int i = 0; i < drawerMenu.size(); i++) {
                // for each item in the drawer
                setCustomDrawerFonts(drawerMenu.getItem(i));
            }
            return null;
        }
    }

    // custom drawer items fonts
    private void setCustomDrawerFonts(MenuItem item) {
        String fontFamily = "";
        Typeface customTypeface = Typeface.createFromAsset(getAssets(), "fonts/hind-regular.ttf");

        SpannableString newItemTitle = new SpannableString(item.getTitle());
        newItemTitle.setSpan(
                new DrawerTypefaceSpan(fontFamily, customTypeface),
                0,
                newItemTitle.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        item.setTitle(newItemTitle);
    }
}
