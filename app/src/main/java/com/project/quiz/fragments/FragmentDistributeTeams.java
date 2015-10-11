package com.project.quiz.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.project.quiz.R;
import com.project.quiz.activities.ActivityTeamDetails;
import com.project.quiz.adapters.CustomTeamDisplayAdapter;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.utils.CommonLibs;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDistributeTeams#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDistributeTeams extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CustomTeamDisplayAdapter adapter;
    private ListView listView;
    private ArrayList<String> selectedStudents = new ArrayList<>();
    private HashMap<Integer, ArrayList<String>> teams = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> tempTeams = new HashMap<>();
    private int numberOfTeams;
    private String mParam1;
    private String mParam2;

    @OnClick(R.id.floating_action_button)
    public void clickButton() {
        updateTeams();
    }

    private void updateTeams() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < tempTeams.size(); i++) {
                    int length = tempTeams.get(i + 1).size();
                    for (int j = 0; j < length; j++) {
                        ContentValues values = new ContentValues();
                        values.put(StudentRecords.TEAM_NUMBER, i + 1);
                        getContext().getContentResolver().update(DataContentProvider.CONTENT_STORE_STUDENTS_URI, values, StudentRecords.STUDENT_NAME + "=?", new String[]{tempTeams.get(i + 1).get(j)});
                    }
                }
                Intent intent = new Intent(getActivity(), ActivityTeamDetails.class);
                startActivity(intent);
                getActivity().finish();
            }
        }, 100);

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDistributeTeams.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDistributeTeams newInstance(String param1, String param2) {
        FragmentDistributeTeams fragment = new FragmentDistributeTeams();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentDistributeTeams() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
        SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        String teamNumber = preferences.getString(CommonLibs.SharedPrefsKeys.TEAM_NUMBER, "0");
        numberOfTeams = Integer.parseInt(teamNumber);

        // TODO: Change Adapter to display your content
        adapter = new CustomTeamDisplayAdapter(getActivity(),
                R.layout.custom_fragment_distribute_teams, teams);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_distribute_students, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if((item.getItemId() == R.id.random)){
            startDistributing();
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_item, container, false);
        listView = (ListView) v.findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        ButterKnife.bind(this, v);
        startDistributing();

        return v;
    }

    private void startDistributing() {
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = new String[]{StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME};
        return new CursorLoader(getActivity(), DataContentProvider.CONTENT_STORE_STUDENTS_URI, projection, StudentRecords.STUDENT_SELECTED + "=?", new String[]{"1"}, StudentRecords.STUDENT_SCORE + " desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == 0) {
//            adapter.swapCursor(data);
        } else {
            int random = 0;
            int low = numberOfTeams;
            while (data.moveToNext()) {
                selectedStudents.add(data.getString(data.getColumnIndex(StudentRecords.STUDENT_NAME)));
            }

            for (int i = 0; i < numberOfTeams; i++) {
                teams.put(i + 1, new ArrayList<String>());
                tempTeams.put(i + 1, new ArrayList<String>());
            }

            while (low != selectedStudents.size()) {
                for (int i = 0; i < numberOfTeams; i++) {
                    if (low != selectedStudents.size()) {
                        random = randomGenerator(low, selectedStudents.size());
                    } else {
                        break;
                    }
                    String student = selectedStudents.get(random);
                    teams.get(i + 1).add(student);
                    tempTeams.get(i + 1).add(student);
                    selectedStudents.remove(random);
//                    tempTeamsStudents.put(random, String.valueOf(random));
                }
            }

            for (int i = 0; i < numberOfTeams; i++) {
                random = randomGenerator(0, selectedStudents.size());
                String student = selectedStudents.get(random);
                teams.get(i + 1).add(student);
                tempTeams.get(i + 1).add(student);
                selectedStudents.remove(random);
            }
            adapter.changeData(teams);
            adapter.notifyDataSetChanged();
        }
        data.moveToPosition(-1);
    }

    private int randomGenerator(int low, int high) {
        Random rand = new Random();
        return rand.nextInt(high - low) + low;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == 0) {
        }
    }
}
