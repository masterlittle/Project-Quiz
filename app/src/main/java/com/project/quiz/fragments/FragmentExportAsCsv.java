package com.project.quiz.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.quiz.R;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.customviews.EditTextRegularFont;
import com.project.quiz.customviews.TextViewRegularFont;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentExportAsCsv#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentExportAsCsv extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private File file;
    private String mParam2;

    @Bind(R.id.filename_layout)
    TextInputLayout filenameLayout;
    @Bind(R.id.filename)
    EditTextRegularFont filename;
    @Bind(R.id.success_text)
    TextViewRegularFont successText;
    @OnTextChanged(R.id.filename)
    public void onFilenameChangedListener(Editable s) {
        if (s.length() > 0) {
            successText.setText("");
            filenameLayout.setErrorEnabled(false);
        }
    }
    @OnClick(R.id.button_delete)
    public void onDelete(){
        if(file != null && !file.getAbsolutePath().equalsIgnoreCase("") && !file.getAbsolutePath().equalsIgnoreCase("")){
            if(file.delete()){
                Toast.makeText(getActivity(), "File deleted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(), "File not deleted", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            successText.setText("Export a file first");
        }
    }
    @OnClick(R.id.button_export)
    public void onClick() {
        if (validateData()) {
            successText.setText("");
            if (!DataContentProvider.exportData(filename.getText().toString().trim(), getContext())) {
                filenameLayout.setError("Enter a different filename and try again");
            } else {
                File exportDir = new File(Environment.getExternalStorageDirectory(), "");
                file = new File(exportDir, filename.getText().toString().trim() + ".csv");
                if (file.exists()) {
                    String filelocation = file.getAbsolutePath();
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    // set the type to 'email'
                    emailIntent.setType("vnd.android.cursor.dir/email");
//                    String to[] = {"asd@gmail.com"};
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                    // the attachment
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+ filelocation));
                    // the mail subject
//                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    successText.setText("Successfully converted");
                }
            }
        }
    }


    private boolean validateData() {
        if (filename.getText().toString().trim().length() <= 0) {
            filenameLayout.setError("Enter a valid filename");
            return false;
        }
        return true;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentExportAsCsv.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentExportAsCsv newInstance(String param1, String param2) {
        FragmentExportAsCsv fragment = new FragmentExportAsCsv();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentExportAsCsv() {
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
        View v = inflater.inflate(R.layout.fragment_export_csv, container, false);
        ButterKnife.bind(this, v);
        return v;
    }


}
