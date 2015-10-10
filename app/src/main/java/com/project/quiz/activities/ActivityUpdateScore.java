package com.project.quiz.activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.project.quiz.R;
import com.project.quiz.adapters.CardArrayAdapter;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.database.StorePointsTable;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.fragments.FragmentDisplayScore;
import com.project.quiz.interfaces.ChangeFragment;
import com.project.quiz.interfaces.UpdateScoreCallback;
import com.project.quiz.utils.CommonLibs;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityUpdateScore extends AppCompatActivity implements UpdateScoreCallback, LoaderManager.LoaderCallbacks<Cursor>, ChangeFragment {
    private CardArrayAdapter cardArrayAdapter;
    private ArrayList<Integer> listOfCurrentScores;
    public static float currentPoints;
    private static int team = -1;
    private Cursor mCursor;

    private int flag;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.card_listView) RecyclerView listView;
    @Bind(R.id.container) CoordinatorLayout container;
    @Bind(R.id.appBarLayout) AppBarLayout appBarLayout;
    private String numberOfTeams;
    private AppBarLayout.Behavior behavior;

    @OnClick(R.id.floating_action_button)
    public void onClickFloating() {
        flag = 0;
        collapseToolbar();
        getLoaderManager().initLoader(1, null, this);
        team = -1;
    }

    @OnClick({R.id.plus_five, R.id.plus_one, R.id.plus_half, R.id.plus_two, R.id.plus_ten, R.id.minus_half, R.id.minus_one, R.id.minus_two, R.id.minus_five, R.id.minus_ten})
    public void clickPoints(TextView v) {
        final String points = v.getText().toString();
        if (team != -1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateTeams(String.valueOf(team + 1), Float.parseFloat(points));
                }
            }, 10);

        } else {
            Toast.makeText(this, "Please select a team", Toast.LENGTH_SHORT).show();
        }
    }
//    @OnItemClick(R.id.card_listView)
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        CardScore teamCard = (CardScore)adapterView.getItemAtPosition(i);
//        Snackbar.make(findViewById(R.id.container),"Hello",Snackbar.LENGTH_INDEFINITE);
//    }

    private void insertTeams(String team) {
        ContentValues value = new ContentValues();
        value.put(StorePointsTable.TEAM_NUMBER, team);
        value.put(StorePointsTable.CURRENT_SCORE, 0);
        value.put(StorePointsTable.CHANGED_SCORE, 0);
        getContentResolver().insert(DataContentProvider.CONTENT_STORE_URI, value);
    }

    private void updateTeams(String team, float points) {
        ContentValues value = new ContentValues();
        currentPoints = currentPoints + points;
        value.put(StorePointsTable.CHANGED_SCORE, currentPoints);
        getContentResolver().update(DataContentProvider.CONTENT_STORE_URI, value, StorePointsTable.TEAM_NUMBER + "=?", new String[]{team});
        getContentResolver().notifyChange(DataContentProvider.CONTENT_STORE_URI, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        ButterKnife.bind(this);

        toolbar.setTitle("Update Scores");
        setSupportActionBar(toolbar);

        numberOfTeams = getIntent().getStringExtra("numberOfTeams");

        cardArrayAdapter = new CardArrayAdapter(this, mCursor);

        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(cardArrayAdapter);
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_update_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_display_scores) {
            loadFragment(CommonLibs.FragmentId.ID_FRAGMENT_DISPLAY_STUDENTS, null);
            return true;
        } else if (id == R.id.action_finish) {
            flag =0;
            getLoaderManager().initLoader(2, null, this);
            Intent intent = new Intent(this, ActivityHomeScreen.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void expandToolbar(int team) {
        ActivityUpdateScore.team = team;
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        behavior = (AppBarLayout.Behavior) params.getBehavior();
        if(behavior!=null) {
            behavior.onNestedFling(container, appBarLayout, null, 0, -5000, false);
            appBarLayout.requestLayout();
        }
    }

    @Override
    public void collapseToolbar() {
        appBarLayout.setExpanded(false);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        behavior = (AppBarLayout.Behavior) params.getBehavior();
        if(behavior!=null) {
            behavior.onNestedFling(container, appBarLayout, null, 0, 5000, true);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id == 0 || id == 3 || id == 1) {
            CursorLoader cl = null;
            String[] projection = {StorePointsTable.COLUMN_ID, StorePointsTable.CURRENT_SCORE, StorePointsTable.CHANGED_SCORE, StorePointsTable.TEAM_NUMBER};
            cl = new CursorLoader(this, DataContentProvider.CONTENT_STORE_URI, projection, null, null, null);
            return cl;
        }
        if (id == 2) {
            CursorLoader cl = null;
            String[] projection = {StorePointsTable.COLUMN_ID, StorePointsTable.CURRENT_SCORE, StorePointsTable.TEAM_NUMBER};
            cl = new CursorLoader(this, DataContentProvider.CONTENT_STORE_URI, projection, null, null, null);
            return cl;
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        if (loader.getId() == 1) {
            if (flag == 0) {
                String d = DatabaseUtils.dumpCursorToString(cursor);
//                ContentValues[] v = new ContentValues[cursor.getCount()];
                while (cursor.moveToNext()) {
                    ++flag;
                    ContentValues value = new ContentValues();
                    value.put(StorePointsTable.CHANGED_SCORE, 0);
                    value.put(StorePointsTable.CURRENT_SCORE, Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(StorePointsTable.CURRENT_SCORE))) + Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(StorePointsTable.CHANGED_SCORE))));
//                            v[flag++] = value;
                    getContentResolver().update(DataContentProvider.CONTENT_STORE_URI, value, StorePointsTable.TEAM_NUMBER + "=?", new String[]{cursor.getString(cursor.getColumnIndex(StorePointsTable.TEAM_NUMBER))});
                }
                getContentResolver().notifyChange(DataContentProvider.CONTENT_STORE_URI, null);
                queryData();
            }
        } else if (loader.getId() == 2) {
            if (flag == 0) {
                String d = DatabaseUtils.dumpCursorToString(cursor);
                while (cursor.moveToNext()) {
                    ++flag;
                    ContentValues value = new ContentValues();
                    value.put(StudentRecords.STUDENT_SCORE, Float.parseFloat(cursor.getString(cursor.getColumnIndex(StorePointsTable.CURRENT_SCORE))));
                    getContentResolver().update(DataContentProvider.CONTENT_STORE_STUDENTS_URI, value, StudentRecords.TEAM_NUMBER + "=?", new String[]{cursor.getString(cursor.getColumnIndex(StorePointsTable.TEAM_NUMBER))});
                }
                getContentResolver().notifyChange(DataContentProvider.CONTENT_STORE_STUDENTS_URI, null);
            }
        } else {
            cardArrayAdapter.swapCursor(cursor);
//            String d = DatabaseUtils.dumpCursorToString(cursor);
            cardArrayAdapter.notifyDataSetChanged();
        }
//        mCursor = cursor;
    }

    private void queryData() {
        getLoaderManager().initLoader(3, null, this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void loadFragment(int id, Bundle bundle) {
        if (id == CommonLibs.FragmentId.ID_FRAGMENT_DISPLAY_STUDENTS) {
            FragmentDisplayScore display = new FragmentDisplayScore();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, display, "FragmentDisplayScore").addToBackStack("FragmentDisplayScore").commit();
        }
    }
}
