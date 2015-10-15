package com.project.quiz.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.project.quiz.R;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.database.StorePointsTable;
import com.project.quiz.utils.CommonLibs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityTeamDetails extends AppCompatActivity {
    @Bind(R.id.edit_quiz_tag) EditText editQuizTag;
    @Bind(R.id.quiz_tag_layout) TextInputLayout quizTagLayout;
    @Bind(R.id.edit_quizmaster) EditText editQuizMaster;
    @Bind(R.id.quizmaster_layout) TextInputLayout quizMasterLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @OnClick(R.id.button_next)
    public void onClick(){
        emptyDatabase();
        if(editQuizTag.getText().length() <=0){
            quizTagLayout.setError("Please enter the quiz tag");
        }
        else if(editQuizMaster.getText().length() <= 0){
            quizMasterLayout.setError("Please enter the name of the Quizmaster");
        } else{
            SharedPreferences preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            insertTeams(preferences.getString(CommonLibs.SharedPrefsKeys.TEAM_NUMBER, "0"));
            Intent intent = new Intent(this, ActivityUpdateScore.class);
//            intent.putExtra(CommonLibs.TeamDetails.TEAM_NUMBER, editTeamNumber.getText().toString());
            startActivity(intent);
            finish();
        }
    }

    private void insertTeams(String teams) {
        int counter = 0;
        ContentValues[] values = new ContentValues[Integer.parseInt(teams)];
        while(counter!=values.length){
            ContentValues value = new ContentValues();
            value.put(StorePointsTable.TEAM_NUMBER, counter+1);
            value.put(StorePointsTable.CURRENT_SCORE, 0);
            value.put(StorePointsTable.CHANGED_SCORE, 0);
            values[counter++] = value;
        }
        getContentResolver().bulkInsert(DataContentProvider.CONTENT_BULK_INSERT_URI, values);
    }

    private void emptyDatabase(){
        getContentResolver().delete(DataContentProvider.CONTENT_DELETE_URI, null, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);
        ButterKnife.bind(this);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_team_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.swapStudents) {
            Intent intent = new Intent(this, ActivitySwapStudentsTeams.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
