package com.project.quiz.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.project.quiz.R;
import com.project.quiz.fragments.FragmentDisplayScore;
import com.project.quiz.fragments.FragmentHomeScreen;
import com.project.quiz.interfaces.ChangeFragment;
import com.project.quiz.utils.CommonLibs;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityHomeScreen extends AppCompatActivity implements ChangeFragment,NavigationView.OnNavigationItemSelectedListener{
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind((R.id.navigation))
    NavigationView navigationView;
    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;
    private int mNavItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        loadFragment(CommonLibs.FragmentId.ID_FRAGMENT_HOME_SCREEN, null);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.addStudents).setChecked(true);
//        navigate(R.id.addStudents);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_home_screen, menu);
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

    private void navigate(final int itemId) {
        // perform the actual navigation logic, updating the main content fragment etc
        if(itemId == R.id.addStudents){
            startActivity(new Intent(this, ActivityAddStudentRecords.class));
        }
        if(itemId == R.id.createTeams){
            startActivity(new Intent(this, ActivitySelectTeams.class));
        }
        if(itemId == R.id.displayScores){
//            startActivity(new Intent(this, ActivityTeamDetails.class));
            loadFragment(CommonLibs.FragmentId.ID_FRAGMENT_DISPLAY_STUDENTS, null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadFragment(int id, Bundle bundle) {
        if(id == CommonLibs.FragmentId.ID_FRAGMENT_HOME_SCREEN){
            FragmentHomeScreen frag = new FragmentHomeScreen();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, frag, "FragmentHomeScreen").commit();
        }
        else if(id == CommonLibs.FragmentId.ID_FRAGMENT_DISPLAY_STUDENTS){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentDisplayScore displayScore = new FragmentDisplayScore();
            fragmentTransaction.replace(R.id.frame, displayScore, "Display Score").addToBackStack("Display Score").commit();
        }
    }
}
