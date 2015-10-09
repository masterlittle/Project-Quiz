package com.project.quiz.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.project.quiz.R;
import com.project.quiz.adapters.CustomTeamDisplayAdapter;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.utils.CommonLibs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
    private HashMap<Integer, String> tempTeamsStudents = new HashMap<>();
    private int numberOfTeams;
    private String mParam1;
    private String mParam2;


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

        SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        String teamNumber = preferences.getString(CommonLibs.SharedPrefsKeys.TEAM_NUMBER, "0");
        numberOfTeams = Integer.parseInt(teamNumber);

        // TODO: Change Adapter to display your content
        adapter = new CustomTeamDisplayAdapter(getActivity(),
                R.layout.custom_fragment_distribute_teams, teams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_item, container, false);
        listView = (ListView) v.findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(1, null, this);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = new String[]{StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME};
        return new CursorLoader(getActivity(), DataContentProvider.CONTENT_STORE_STUDENTS_URI , projection, StudentRecords.STUDENT_SELECTED + "=?", new String[]{"1"}, StudentRecords.STUDENT_SCORE);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == 0) {
//            adapter.swapCursor(data);
        }
        else {
            ArrayList<String> selectedStudentsInTeams = new ArrayList<>();
            int size,studentsPerTeam = 0;
            int low = 0;
            while (data.moveToNext()) {
                selectedStudents.add(data.getString(data.getColumnIndex(StudentRecords.STUDENT_NAME)));
            }
            size = selectedStudents.size();
            if((size%numberOfTeams) == 0){
                studentsPerTeam = (size/numberOfTeams);
            }
            else{
                studentsPerTeam = (size/numberOfTeams) + 1;
            }
            int high = studentsPerTeam;
            for (int i = 0; i < numberOfTeams; i++) {
                teams.put(i+1, new ArrayList<String>());
            }
            for(int i =0;i<numberOfTeams;i++){
                for(int j =0;j<studentsPerTeam; j++) {
                    int random = randomGenerator(low, high);
                    while(tempTeamsStudents.size() != size){
                        if(!tempTeamsStudents.containsKey(random) && random < size){
                            selectedStudentsInTeams.add(selectedStudents.get(random));
                            tempTeamsStudents.put(random, String.valueOf(random));
                            low+= studentsPerTeam;
                            high+= studentsPerTeam;
                            break;
                        }
                        else {
                            random = randomGenerator(low, high);
                        }

                    }
                }
                teams.put(i+1, selectedStudentsInTeams);
                low = 0;
                high = studentsPerTeam;
                selectedStudentsInTeams = new ArrayList<>();
            }
            adapter.changeData(teams);
            adapter.setNotifyOnChange(true);
            adapter.notifyDataSetChanged();
        }
    }

    private int randomGenerator(int low, int high){
        Random rand = new Random();
        return rand.nextInt(high - low) + low;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId() == 0) {
        }
    }
}
