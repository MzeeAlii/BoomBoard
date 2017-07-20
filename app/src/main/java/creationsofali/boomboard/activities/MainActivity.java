package creationsofali.boomboard.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import creationsofali.boomboard.R;
import creationsofali.boomboard.adapters.CollapsingToolbarAdapter;
import creationsofali.boomboard.appfonts.MyCustomAppFont;
import creationsofali.boomboard.datamodels.Notice;
import creationsofali.boomboard.datamodels.RequestCode;
import creationsofali.boomboard.datamodels.Student;
import creationsofali.boomboard.fragments.AllOnBoardFragment;
import creationsofali.boomboard.fragments.ProfileFragment;
import creationsofali.boomboard.fragments.WhatsNewFragment;
import creationsofali.boomboard.helpers.DirectoriesHelper;
import creationsofali.boomboard.helpers.DrawerTypefaceHelper;
import creationsofali.boomboard.helpers.EmailHelper;
import creationsofali.boomboard.helpers.NetworkHelper;
import creationsofali.boomboard.helpers.PackageInfoHelper;
import creationsofali.boomboard.helpers.PermissionsHelper;
import creationsofali.boomboard.helpers.SharedPreferenceHelper;
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
    TextView textNavName, textNavEmail, textToolbarTitle;
    ImageView imageNavDp;
    Animation animationFabShow, animationFabHide;

    RecyclerView appBarRecycler;
    RecyclerView.LayoutManager layoutManagerHorizontal;
    CollapsingToolbarAdapter whatsNewAdapter;
    WhatsNewFragment whatsNewFragment;

    List<Notice> appBarNoticeList = new ArrayList<>();

    DatabaseReference noticesReference;
    ChildEventListener noticesChildEventListener;
    ValueEventListener notesValueEventListener;
    FirebaseAuth firebaseAuth;

    ScaleInAnimationAdapter scaleInAnimationAdapter;

    boolean doubleBackToExitPressedOnce = false, isTimeTableReady = false;
    int DELAY_TIME = 2000;
    final String TAG = this.getClass().getSimpleName();

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("");
        }

        textToolbarTitle = (TextView) findViewById(R.id.textToolbarTitle);

        firebaseAuth = FirebaseAuth.getInstance();

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setExpandedTitleColor(
                ContextCompat.getColor(MainActivity.this, android.R.color.transparent));

        animationFabShow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fab_show);
        animationFabHide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fab_hide);

        fragmentManager = getSupportFragmentManager();
        // by default, set checked item onCreate
        whatsNewFragment = new WhatsNewFragment();
        fragmentManager.beginTransaction().replace(
                R.id.mainContentView,
                whatsNewFragment,
                whatsNewFragment.getTag()).commit();

        appBarLayout.setExpanded(true, true);

        fabRefresh = (FloatingActionButton) findViewById(R.id.fabRefresh);
        fabRefresh.startAnimation(animationFabShow);
        fabRefresh.setEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawer = (NavigationView) findViewById(R.id.navigationDrawer);
        View headerView = navigationDrawer.getHeaderView(0);
        textNavName = (TextView) headerView.findViewById(R.id.textNavName);
        textNavEmail = (TextView) headerView.findViewById(R.id.textNavEmail);
        imageNavDp = (ImageView) headerView.findViewById(R.id.imageNavDp);
        textNavEmail.setText("");
        textNavName.setText("");

        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            textNavEmail.setText(user.getEmail());
            textNavName.setText(user.getDisplayName());
            Glide.with(MainActivity.this).load(user.getPhotoUrl()).into(imageNavDp);
        }


        collapsingToolbar.setTitle("");

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
                        // collapsingToolbar.setTitle(getString(R.string.app_name));
                        textToolbarTitle.setText(getString(R.string.app_name));
                        if (!fabRefresh.isEnabled()) {
                            // fabRefresh.show();
                            fabRefresh.startAnimation(animationFabShow);
                            fabRefresh.setEnabled(true);
                        }
                        break;

                    case R.id.navAllOnBoard:
                        AllOnBoardFragment allOnBoardFragment = new AllOnBoardFragment();
                        fragmentManager.beginTransaction().replace(
                                R.id.mainContentView,
                                allOnBoardFragment,
                                allOnBoardFragment.getTag()).commit();

                        appBarLayout.setExpanded(false, true);
                        // collapsingToolbar.setTitle(item.getTitle());
                        textToolbarTitle.setText(item.getTitle());

                        fabRefresh.startAnimation(animationFabHide);
                        // fabRefresh.hide();
                        fabRefresh.setEnabled(false);
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
                        // collapsingToolbar.setTitle("Student's Profile");
                        textToolbarTitle.setText(item.getTitle());

                        fabRefresh.startAnimation(animationFabHide);
                        //fabRefresh.hide();
                        fabRefresh.setEnabled(false);
                        break;

                    case R.id.navBookmarks:
                        // go to bookmark activity, pass user object
                        startActivity(new Intent(MainActivity.this, BookmarksActivity.class));
                        break;

                    case R.id.navAbout:
                        showAboutDialog();
                        break;

                    case R.id.navHint:
                        showHintDialog();
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
        new DrawerTypefaceHelper(MainActivity.this).executeTask(navigationDrawer.getMenu());
        // new DrawerTypefaceSpanTask().execute(navigationDrawer.getMenu());

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

        whatsNewAdapter = new CollapsingToolbarAdapter(appBarNoticeList, MainActivity.this);
        //appBarRecycler.setAdapter(whatsNewAdapter);

        scaleInAnimationAdapter = new ScaleInAnimationAdapter(whatsNewAdapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(1f));
        scaleInAnimationAdapter.setDuration(1000);
        // set animationAdapter
        appBarRecycler.setAdapter(scaleInAnimationAdapter);

        // get student profile from shared preferences
        Student student = SharedPreferenceHelper.getStudentProfile(MainActivity.this);

        if (student != null)
            noticesReference = FirebaseDatabase.getInstance().getReference()
                    .child("notices")
                    .child(student.getCollegeAbr())
                    .child("global");

        // listeners
        noticesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Notice notice = dataSnapshot.getValue(Notice.class);
                appBarNoticeList.add(notice);
                scaleInAnimationAdapter.notifyItemInserted(appBarNoticeList.size());
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
                if (!fabRefresh.isEnabled()) {
                    fabRefresh.setEnabled(true);
                    fabRefresh.startAnimation(animationFabShow);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        // attach listeners and retrieve from database
        startAsyncTaskForAppBarRecycler();
    }


    public void startAsyncTaskForAppBarRecycler() {
        new NoticesRetrieveTask().execute();
    }


    private class NoticesRetrieveTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            appBarNoticeList.clear();
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
                noticesReference.addChildEventListener(noticesChildEventListener);
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
                //showSnackbarAction("Failed!");
                if (fabRefresh.isEnabled()) {
                    fabRefresh.setEnabled(false);
                    fabRefresh.startAnimation(animationFabHide);
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (fabRefresh.isEnabled())
            fabRefresh.startAnimation(animationFabShow);
    }

    private void detachDatabaseListeners() {
        if (notesValueEventListener != null)
            noticesReference.removeEventListener(notesValueEventListener);

        if (noticesChildEventListener != null)
            noticesReference.removeEventListener(noticesChildEventListener);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            switch (requestCode) {
                case RequestCode.RC_READ_STORAGE:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: read permission granted.");
                        openBoomBoardFolder();
                    } else
                        Log.d(TAG, "onRequestPermissionsResult: read permission denied.");
                    break;
            }
        }
    }

    public void showAboutDialog() {
        final View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_about, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(true);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        TextView textVersion = (TextView) dialogView.findViewById(R.id.textVersion);
        textVersion.setText(PackageInfoHelper.getVersionName(MainActivity.this));

        TextView textRights = (TextView) dialogView.findViewById(R.id.textRights);
        String unicodeCopyRight = "\u00A9"; // or (char) 169 = 10101001 = 0x00A9
        String unicodeTradeMark = "\u2122"; // or (char) 8482 = 0x2122
        // (c) 2017 TINTech Apps tm
        //      All Rights Reserved.
        textRights.setText(unicodeCopyRight);
        textRights.append(" 2017 " + "TINTech Apps" + unicodeTradeMark);
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


    public void showHintDialog() {
        final View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_downloads_hint, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(true);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialogView.findViewById(R.id.textOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.layoutDirectory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do magic
                // TODO: 6/10/17 show waiting dialog while checking folder
                if (PermissionsHelper.hasReadStoragePermission(MainActivity.this)) {
                    // open folder
                    openBoomBoardFolder();

                } else
                    PermissionsHelper.requestReadStoragePermission(MainActivity.this);

                // close dialog
                dialog.dismiss();
            }
        });
    }


//    private class DrawerTypefaceSpanTask extends AsyncTask<Menu, Void, Void> {
//        @Override
//        protected Void doInBackground(Menu... menus) {
//            Menu drawerMenu = menus[0];
//            for (int i = 0; i < drawerMenu.size(); i++) {
//                // for each item in the drawer
//                setCustomDrawerFonts(drawerMenu.getItem(i));
//            }
//            return null;
//        }
//    }

    // custom drawer items fonts
//    private void setCustomDrawerFonts(MenuItem item) {
//        String fontFamily = "";
//        Typeface customTypeface = Typeface.createFromAsset(getAssets(), "fonts/hind-regular.ttf");
//
//        SpannableString newItemTitle = new SpannableString(item.getTitle());
//        newItemTitle.setSpan(
//                new DrawerTypefaceSpan(fontFamily, customTypeface),
//                0,
//                newItemTitle.length(),
//                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//        item.setTitle(newItemTitle);
//    }

    public void setProfileFragment(TextView textProfileName, TextView textProfileEmail,
                                   ImageView imageProfileDp) {

        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            textProfileName.setText(user.getDisplayName());
            textProfileEmail.setText(user.getEmail());
            Glide.with(MainActivity.this).load(user.getPhotoUrl()).into(imageProfileDp);
        }
    }


    public void showSnackbarAction(String message) {
        Snackbar.make(findViewById(R.id.mainCoordinatorLayout),
                message,
                Snackbar.LENGTH_INDEFINITE)
                // .setDuration(20000)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // start new retrieve task
                        // for app bar layout
                        new NoticesRetrieveTask().execute();
                        // for fragment
                        whatsNewFragment.startAsyncTask();
                    }
                }).show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.mainCoordinatorLayout),
                message,
                Snackbar.LENGTH_INDEFINITE)
                .setDuration(10000)
                .show();
    }

    public void showSignOutDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_sign_out, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        TextView textHeaderName = (TextView) dialogView.findViewById(R.id.textHeaderName);
        ImageView imageDialogDp = (ImageView) dialogView.findViewById(R.id.imageDialogDp);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            textHeaderName.setText(user.getDisplayName());
            Glide.with(MainActivity.this).load(user.getPhotoUrl()).into(imageDialogDp);
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
        startActivity(new Intent(MainActivity.this, SignInActivity.class));
        finish();
    }

    private void openBoomBoardFolder() {
        // open folder
        // TODO: 7/19/17 show loading dialog while opening the folder
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/BoomBoard");
        if (folder.exists()) {
            // open folder
            Log.d(TAG, "folder exists: " + folder.getAbsolutePath());
            DirectoriesHelper.openDirectory(MainActivity.this, folder.getAbsolutePath());

        } else
            showSnackbar("You haven't downloaded any file yet.");

    }
}
