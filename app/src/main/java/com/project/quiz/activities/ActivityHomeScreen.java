package com.project.quiz.activities;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.parse.ParseUser;
import com.project.quiz.R;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.customclasses.SyncData;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.fragments.FragmentDisplayScore;
import com.project.quiz.fragments.FragmentHomeScreen;
import com.project.quiz.interfaces.ChangeFragment;
import com.project.quiz.utils.CommonLibs;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityHomeScreen extends AppCompatActivity implements ChangeFragment, NavigationView.OnNavigationItemSelectedListener, Animation.AnimationListener {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind((R.id.navigation))
    NavigationView navigationView;
    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;
    private int mNavItemId;
    private BroadcastReceiver broadcast;
    private Menu menu;
    ImageView iv;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAnimations();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        updateTeams();

        loadFragment(CommonLibs.FragmentId.ID_FRAGMENT_HOME_SCREEN, null);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.login).setChecked(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void setAnimations() {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setTransitionBackgroundFadeDuration(700);
        getWindow().setAllowReturnTransitionOverlap(true);
        getWindow().setExitTransition(new Fade());
    }

    public void setCommunication(String msg) {
        // TODO Auto-generated method stub
        FragmentHomeScreen recFragment = (FragmentHomeScreen) getFragmentManager().findFragmentByTag("FragmentHomeScreen");
        if (null != recFragment) {
            recFragment.setUsername(msg);
        }

    }

    private void updateTeams() {
        ContentValues values = new ContentValues();
        values.put(StudentRecords.TEAM_NUMBER, -1);
        getContentResolver().update(DataContentProvider.CONTENT_STORE_STUDENTS_URI, values, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            getMenuInflater().inflate(R.menu.menu_activity_home_screen, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_activity_logon, menu);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        // update highlighted item in the navigation menu
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();

        // allow some time after closing the drawer before performing real navigation
        // so the user can see what is happening
        drawerLayout.closeDrawer(GravityCompat.START);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
            }
        }, 100);
        return true;
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.project.quiz.LOGIN_SUCCESS");
        registerReceiver(broadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ParseUser user = ParseUser.getCurrentUser();
                if (user != null) {
                    setCommunication(user.get("name").toString());
                    menu.clear();
                    getMenuInflater().inflate(R.menu.menu_activity_home_screen, menu);
                } else {
                    getMenuInflater().inflate(R.menu.menu_activity_logon, menu);
                }
            }
        }, intentFilter);
    }

    private void navigate(final int itemId) {
        // perform the actual navigation logic, updating the main content fragment etc
        if (itemId == R.id.login) {
//            startActivity(new Intent(this, ActivityTeamDetails.class));
            startActivity(new Intent(this, ActivityLogon.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
        if (itemId == R.id.addStudents) {
            startActivity(new Intent(this, ActivityAddStudentRecords.class));
        }
        if (itemId == R.id.createTeams) {
            startActivity(new Intent(this, ActivitySelectTeams.class));
        }
        if (itemId == R.id.displayScores) {
            loadFragment(CommonLibs.FragmentId.ID_FRAGMENT_DISPLAY_STUDENTS, null);
        }
        if (itemId == R.id.events) {
//            startActivity(new Intent(this, ActivityTeamDetails.class));
            startActivity(new Intent(this, ActivityEvents.class));
        }
    }

    private void syncAnimation(final MenuItem item) {
        this.item = item;
        LayoutInflater inflater = (LayoutInflater) getApplication()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         iv = (ImageView) inflater.inflate(R.layout.image_sync_animation,
                null);
        final Animation rotation = AnimationUtils.loadAnimation(getApplication(),
                R.anim.rotate_icon);
        iv.startAnimation(rotation);
        item.setActionView(iv);
        rotation.setAnimationListener(this);
    }


    @Override
    protected void onResume() {
        registerReceiver();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcast);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this, ActivityHomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        if (id == R.id.sync) {
            syncAnimation(item);
            new SyncData().sync(getApplicationContext(), this);
        }

        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void loadFragment(int id, Bundle bundle) {
        if (id == CommonLibs.FragmentId.ID_FRAGMENT_HOME_SCREEN) {
            FragmentHomeScreen frag = new FragmentHomeScreen();
            getFragmentManager().beginTransaction().replace(R.id.frame, frag, "FragmentHomeScreen").commit();
        } else if (id == CommonLibs.FragmentId.ID_FRAGMENT_DISPLAY_STUDENTS) {
            FragmentDisplayScore displayScore = new FragmentDisplayScore();
            getFragmentManager().beginTransaction().replace(R.id.frame, displayScore, "DisplayScore").addToBackStack("DisplayScore").commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getSupportActionBar().setTitle("Home");
            getFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        item.setActionView(null);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
