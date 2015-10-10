package com.project.quiz.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.project.quiz.R;
import com.project.quiz.customClasses.CustomDialogClass;
import com.project.quiz.fragments.FragmentDistributeTeams;
import com.project.quiz.fragments.FragmentSelectStudents;
import com.project.quiz.interfaces.ChangeFragment;
import com.project.quiz.interfaces.DialogBoxListener;
import com.project.quiz.utils.CommonLibs;

public class ActivitySelectTeams extends AppCompatActivity implements DialogBoxListener, ChangeFragment, FragmentSelectStudents.OnFragmentInteraction {
    private CustomDialogClass dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_teams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_select_teams, menu);
        return true;
    }

    @Override
    protected void onResume() {
        loadFragment(CommonLibs.FragmentId.ID_FRAGMENT_SELECT_STUDENTS, null);
        super.onResume();
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadFragment(int id, Bundle bundle) {
        if (id == CommonLibs.FragmentId.ID_FRAGMENT_SELECT_STUDENTS) {
            FragmentSelectStudents fragmentSelectStudents = new FragmentSelectStudents();
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragmentSelectStudents, "FRAGMENT_SELECT_STUDENTS").commit();
        } else if (id == CommonLibs.FragmentId.ID_FRAGMENT_DISTRIBUTE_STUDENTS) {
            FragmentDistributeTeams fragmentDistributeTeams = new FragmentDistributeTeams();
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragmentDistributeTeams, "FRAGMENT_SELECT_STUDENTS").commit();
        }
    }


    @Override
    public void showEditDialog(int position, String info) {
        dialog = new CustomDialogClass(this);
        dialog.show();
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
    public void doWork() {
        showEditDialog(0, "");
    }
}
