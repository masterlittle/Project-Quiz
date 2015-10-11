package com.project.quiz.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.project.quiz.R;
import com.project.quiz.adapters.CustomStudentEditCursorAdapter;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.database.StudentRecords;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 **/
public class FragmentEditStudentDetails extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String studentId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    private View view;
    @Bind(R.id.container)
    CoordinatorLayout container;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private CustomStudentEditCursorAdapter mAdapter;

    @OnClick(R.id.floating_action_button)
    public void OnClickButton() {
        HashMap<String, Integer> studentScores = mAdapter.getStudentScores();
        setScore(studentScores);
    }

    // TODO: Rename and change types of parameters
    public static FragmentDisplayScore newInstance(String param1, String param2) {
        FragmentDisplayScore fragment = new FragmentDisplayScore();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentEditStudentDetails() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        String[] from = new String[]{StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME, StudentRecords.STUDENT_SCORE};
        int[] to = new int[]{R.id.student_name_position, R.id.student_name_field, R.id.student_score_field};

        // TODO: Change Adapter to display your content
        mAdapter = new CustomStudentEditCursorAdapter(getActivity(),
                R.layout.fragment_edit_student_details, null, from, to, 0);
        mAdapter.getInstance(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        ButterKnife.bind(this, view);
        container.requestFocus();
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        if(view!=null) {
            hideKeyboard(view);
        }
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = new String[]{StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME, StudentRecords.STUDENT_SCORE, StudentRecords.STUDENT_ID};
        return new CursorLoader(getActivity(), DataContentProvider.CONTENT_STORE_STUDENTS_URI, projection, null, null, StudentRecords.STUDENT_NAME + " asc");

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void setScore(final HashMap<String, Integer> studentScores) {
        final int length = studentScores.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < length; i++) {
                    Map.Entry<String, Integer> entry = studentScores.entrySet().iterator().next();
                    ContentValues values = new ContentValues();
                    values.put(StudentRecords.STUDENT_SCORE, entry.getValue());
                    getActivity().getContentResolver().update(DataContentProvider.CONTENT_STORE_STUDENTS_URI, values, StudentRecords.STUDENT_ID + "=?", new String[]{entry.getKey()});
                    studentScores.remove(entry.getKey());
                }
                getActivity().getContentResolver().notifyChange(DataContentProvider.CONTENT_STORE_STUDENTS_URI, null);
            }
        }, 100);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
}
