package com.project.quiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.quiz.R;
import com.project.quiz.customviews.CustomDialogClass;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.fragments.FragmentDistributeTeams;
import com.project.quiz.fragments.FragmentSelectStudents;
import com.project.quiz.interfaces.AuthenticateUserInterface;
import com.project.quiz.interfaces.ChangeFragment;
import com.project.quiz.interfaces.DialogBoxListener;
import com.project.quiz.utils.CommonLibs;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivitySelectTeams extends AuthActivity implements DialogBoxListener, ChangeFragment, FragmentSelectStudents.OnFragmentInteraction, AuthenticateUserInterface {
    private CustomDialogClass dialog;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.authenticateText)
    TextViewRegularFont authenticateText;
    private int count;
    private static final String[] roles= {CommonLibs.Roles.ROLE_ADMINISTRATOR, CommonLibs.Roles.ROLE_MODERATOR};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_teams);

        ButterKnife.bind(this);
        toolbar.setTitle("Create Teams");
        checkRole(roles, this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_select_teams, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadFragment(int id, Bundle bundle) {
        if (id == CommonLibs.FragmentId.ID_FRAGMENT_SELECT_STUDENTS) {
            if(!isDestroyed()) {
                FragmentSelectStudents fragmentSelectStudents = new FragmentSelectStudents();
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragmentSelectStudents, "FRAGMENT_SELECT_STUDENTS").commit();
            }
        } else if (id == CommonLibs.FragmentId.ID_FRAGMENT_DISTRIBUTE_STUDENTS) {
            if(!isDestroyed()) {
                FragmentDistributeTeams fragmentDistributeTeams = new FragmentDistributeTeams();
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragmentDistributeTeams, "FRAGMENT_DISTRIBUTE_STUDENTS").addToBackStack("FRAGMENT_DISTRIBUTE_STUDENTS").commit();
            }
        }
    }


    @Override
    public void showEditDialog(int position, String info) {
        if(count!=0) {
            dialog = new CustomDialogClass(this, (int) count);
            dialog.show();
        }
        else{
            Toast.makeText(this,"Please select atleast one student",Toast.LENGTH_SHORT).show();
        }
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int width = metrics.widthPixels;
//        int height = metrics.heightPixels;
//        dialog.getWindow().setLayout((width * 6) / 7, (height * 5) / 7);
    }

    @Override
    public void onDialogPositivePressed() {
        FragmentSelectStudents f = (FragmentSelectStudents) getSupportFragmentManager().findFragmentByTag("FRAGMENT_SELECT_STUDENTS");
        f.doWork();
        dialog.dismiss();
    }

    @Override
    public void onDialogCancelPressed() {

    }

    @Override
    public void doWork(int value) {
        count = value;
        showEditDialog(0, "");
    }

    @Override
    public void authenticateUser(boolean isAuthorized) {
        if (isLoggedIn) {
            if (isAuthorized) {
                progress.setVisibility(View.GONE);
                authenticateText.setVisibility(View.GONE);
                loadFragment(CommonLibs.FragmentId.ID_FRAGMENT_SELECT_STUDENTS, null);
            } else {
                finish();
            }
        } else {
            startLogin(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                checkRole(roles, this);
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }
}
