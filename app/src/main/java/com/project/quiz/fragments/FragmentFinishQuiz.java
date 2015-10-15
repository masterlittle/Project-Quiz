package com.project.quiz.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.project.quiz.R;
import com.project.quiz.activities.ActivityHomeScreen;
import com.project.quiz.activities.ActivityUpdateScore;
import com.project.quiz.adapters.CustomCheckboxSelectQuizmasterAdapter;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.database.StorePointsTable;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.utils.CommonLibs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentFinishQuiz extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static int countSelected = 0;
    public static String studentId;
    private int flag;
    private int teamSize =1;
    private String prevScore = "-1222";
    private CustomCheckboxSelectQuizmasterAdapter mAdapter;
    private String teamNumber;

    @OnClick(R.id.button_finishQuizandClose)
    public void OnClose(){
        flag = 0;
        if(countSelected != 1){
            Toast.makeText(getActivity(), "Please select one quizmaster", Toast.LENGTH_SHORT).show();
        }else {
            updateQuizmasterScore();
            getLoaderManager().initLoader(2, null, this);
            Intent intent = new Intent(getActivity(), ActivityHomeScreen.class);
            startActivity(intent);
            countSelected = 0;
            getActivity().finish();
        }
    }

    private void updateQuizmasterScore() {
        ContentValues values = new ContentValues();
        values.put(StudentRecords.STUDENT_SCORE, Integer.parseInt(teamNumber) * 2);
        getActivity().getContentResolver().update(DataContentProvider.CONTENT_UPDATE_SCORE_QUIZMASTER_URI, values, StudentRecords.STUDENT_ID + "=?", new String[]{studentId});
    }

    @OnClick(R.id.button_finishQuizAndRestart)
    public void onRestart(){
        flag = 0;
        if(countSelected != 1){
            Toast.makeText(getActivity(), "Please select one quizmaster", Toast.LENGTH_SHORT).show();
        }else {
            resetScores();
            updateQuizmasterScore();
            getLoaderManager().initLoader(2, null, this);
            Intent intent = new Intent(getActivity(), ActivityUpdateScore.class);
            startActivity(intent);
            countSelected = 0;
            getActivity().finish();
        }

    }

    private void resetScores() {
        ContentValues value = new ContentValues();
        value.put(StorePointsTable.CHANGED_SCORE, 0);
        value.put(StorePointsTable.CURRENT_SCORE, 0);
        getActivity().getContentResolver().update(DataContentProvider.CONTENT_STORE_URI, value, null, null);
        getActivity().getContentResolver().notifyChange(DataContentProvider.CONTENT_STORE_URI, null);
    }

    @Bind(android.R.id.list) ListView quizmasterList;


//    private OnFragmentInteractionListener mListener;
    public static FragmentFinishQuiz newInstance(String param1, String param2) {
        FragmentFinishQuiz fragment = new FragmentFinishQuiz();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentFinishQuiz() {
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
        teamNumber = preferences.getString(CommonLibs.SharedPrefsKeys.TEAM_NUMBER, "1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_finish_quiz, container, false);
        ButterKnife.bind(this, v);
        String[] from = new String[]{StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME, StudentRecords.STUDENT_ID};
        int[] to = new int[]{R.id.student_name_position, R.id.student_name_field, R.id.student_id_field};
        // TODO: Change Adapter to display your content
        mAdapter = new CustomCheckboxSelectQuizmasterAdapter(getActivity(),
                R.layout.custom_fragment_select_quizmaster, null, from, to, 0);
        quizmasterList.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
        return  v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
//            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 2) {
            CursorLoader cl = null;
            String[] projection = {StorePointsTable.COLUMN_ID, StorePointsTable.TEAM_NUMBER, StorePointsTable.CURRENT_SCORE};
            cl = new CursorLoader(getActivity(), DataContentProvider.CONTENT_STORE_URI, projection, null, null, StorePointsTable.CURRENT_SCORE + " desc");
            return cl;
        }
        else if(id == 0){
            CursorLoader cl = null;
            String[] projection = {StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME, StudentRecords.STUDENT_ID , StudentRecords.STUDENT_SELECTED};
            cl = new CursorLoader(getActivity(), DataContentProvider.CONTENT_STORE_STUDENTS_URI, projection, StudentRecords.STUDENT_SELECTED + "=?", new String[]{"0"}, StudentRecords.STUDENT_NAME + " ASC");
            return cl;
        }
        else{
            return null;
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == 2) {
            teamSize = data.getCount();
            if (flag == 0) {
                String d = DatabaseUtils.dumpCursorToString(data);
                while (data.moveToNext()) {
                    ++flag;
                    if (data.getPosition() != 0 && !data.getString(data.getColumnIndex(StorePointsTable.CURRENT_SCORE)).equalsIgnoreCase(prevScore)) {
                        teamSize--;
                    }
                    ContentValues value = new ContentValues();
                    value.put(StudentRecords.STUDENT_SCORE, teamSize * 2);
                    getActivity().getContentResolver().update(DataContentProvider.CONTENT_UPDATE_SCORE_STUDENTS_URI, value, null, new String[]{data.getString(data.getColumnIndex(StorePointsTable.TEAM_NUMBER))});
                    prevScore = data.getString(data.getColumnIndex(StorePointsTable.CURRENT_SCORE));
                }
            }

        }
        else if(loader.getId() == 0){
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        public void onFragmentInteraction(Uri uri);
//    }

}
