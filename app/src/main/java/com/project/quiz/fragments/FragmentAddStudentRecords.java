package com.project.quiz.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.quiz.R;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.log.Logging;
import com.project.quiz.utils.CommonLibs;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentAddStudentRecords extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String LOG_TAG = FragmentAddStudentRecords.class.getSimpleName();

    @Bind(R.id.nameEditLayout)
    TextInputLayout nameEditLayout;
    @Bind(R.id.yearEditLayout) TextInputLayout yearEditLayout;
    @OnClick(R.id.button_next)
    public void onClick(){
        if(nameEditLayout.getEditText()!=null && nameEditLayout.getEditText().getText().toString().length() <= 0){
            nameEditLayout.setError("Please enter the name");
        }
        if(yearEditLayout.getEditText()!=null && yearEditLayout.getEditText().getText().toString().length() <= 0){
            yearEditLayout.setError("Please enter the year");
        }
        if(nameEditLayout.getEditText().getText().toString().length() >0 && yearEditLayout.getEditText().getText().toString().length() > 0){
            try {
                ContentValues values = new ContentValues();
                values.put(StudentRecords.STUDENT_NAME, nameEditLayout.getEditText().getText().toString());
                values.put(StudentRecords.STUDENT_YEAR, yearEditLayout.getEditText().getText().toString());
                values.put(StudentRecords.STUDENT_SCORE, 0);
                values.put(StudentRecords.STUDENT_ID, UUID.randomUUID().toString());
                values.put(StudentRecords.STUDENT_SELECTED, 0);
                getActivity().getContentResolver().insert(DataContentProvider.CONTENT_STORE_STUDENTS_URI, values);
                Toast.makeText(getActivity(), "Student has been added", Toast.LENGTH_SHORT).show();
            }catch(Exception exception){
                Logging.logException(LOG_TAG, exception, CommonLibs.Priority.MEDIUM);
            }
        }
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAddStudentRecords.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddStudentRecords newInstance(String param1, String param2) {
        FragmentAddStudentRecords fragment = new FragmentAddStudentRecords();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentAddStudentRecords() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_student_records, container, false);
        ButterKnife.bind(this, v);
        return v;
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
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
