package com.project.quiz.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.project.quiz.R;
import com.project.quiz.adapters.CustomCheckboxCursorAdapter;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.utils.CommonLibs;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 */
public class FragmentSelectStudents extends BaseFragment implements AbsListView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public  HashMap<String, Integer> selectedStudents = new HashMap<>();
    public  ArrayList<String> selectedStudentList = new ArrayList<>();
    private OnFragmentInteraction mListener;

    public interface OnFragmentInteraction{
        public void doWork(int value);
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @OnClick(R.id.floating_action_button)
    public void onFloatingButtonClick(){
        int count =0;
            for (String key : selectedStudentList) {
                if (selectedStudents.containsKey(key) && selectedStudents.get(key) == 1) {
                    count++;
                }
            }
        if(count != selectedStudentList.size())
            mListener.doWork(count);
        else{
            Toast.makeText(getActivity(), "The Quizmaster must not be selected", Toast.LENGTH_LONG).show();
        }
    }

    @OnItemClick(android.R.id.list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int i = 0;
        CheckBox c = (CheckBox) view.findViewById(R.id.student_select_checkbox);
        TextView t = (TextView) view.findViewById(R.id.student_id_field);
        ContentValues values = new ContentValues();
        if (c.isChecked())
            i = 1;
        values.put(StudentRecords.STUDENT_SELECTED, i);
        getActivity().getContentResolver().update(DataContentProvider.CONTENT_STORE_URI, values, StudentRecords.STUDENT_ID + "=?", new String[]{t.getText().toString()});
    }

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private CustomCheckboxCursorAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static FragmentSelectStudents newInstance(String param1, String param2) {
        FragmentSelectStudents fragment = new FragmentSelectStudents();
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
    public FragmentSelectStudents() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Toast.makeText(getActivity(), "Do not select the quizmaster", Toast.LENGTH_LONG).show();
        String[] from = new String[]{StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME, StudentRecords.STUDENT_SELECTED, StudentRecords.STUDENT_ID};
        int[] to = new int[]{R.id.student_name_position, R.id.student_name_field, R.id.student_select_checkbox, R.id.student_id_field};
        // TODO: Change Adapter to display your content
        mAdapter = new CustomCheckboxCursorAdapter(getActivity(),
                R.layout.custom_fragment_select_students, null, from, to, 0, selectedStudents, selectedStudentList);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_string, container, false);
        ButterKnife.bind(this, view);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteraction)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = new String[]{StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME, StudentRecords.STUDENT_SELECTED, StudentRecords.STUDENT_ID};
        return new CursorLoader(getActivity(), DataContentProvider.CONTENT_STORE_STUDENTS_URI , projection, null, null, StudentRecords.STUDENT_NAME + " asc");

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        data.moveToPosition(-1);
        while(data.moveToNext()) {
            String student_id = data.getString(data.getColumnIndex(StudentRecords.STUDENT_ID));
            selectedStudents.put(student_id, 1);
            selectedStudentList.add(student_id);
        }
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void doWork() {
        int i =0;
        int s;
        while(i != selectedStudentList.size()) {
            if(!selectedStudents.containsKey(selectedStudentList.get(i))) {
                s =1;
            }
            else{
                s = selectedStudents.get(selectedStudentList.get(i));
            }
            ContentValues values = new ContentValues();

            values.put(StudentRecords.STUDENT_SELECTED, s);
            getContext().getContentResolver().update(DataContentProvider.CONTENT_STORE_STUDENTS_URI, values, StudentRecords.STUDENT_ID + "=?", new String[]{selectedStudentList.get(i)});
            i++;
        }
        selectedStudents.clear();
        selectedStudentList.clear();
        getFragmentLoader().loadFragment(CommonLibs.FragmentId.ID_FRAGMENT_DISTRIBUTE_STUDENTS,null);

    }
}

