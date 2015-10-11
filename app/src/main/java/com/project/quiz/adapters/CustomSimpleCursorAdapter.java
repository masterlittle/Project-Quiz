package com.project.quiz.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.project.quiz.R;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.fragments.FragmentSelectStudents;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shitij on 27/09/15.
 */
public class CustomSimpleCursorAdapter extends SimpleCursorAdapter {
    public Context context;
    private int layout;
    public HashMap<String, Integer> selectedStudents = new HashMap<>();
    public ArrayList<String> selectedStudentList = new ArrayList<>();

    public CustomSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, HashMap<String, Integer> selectedStudents, ArrayList<String> selectedStudentList) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.layout = layout;
        this.selectedStudents = selectedStudents;
        this.selectedStudentList = selectedStudentList;
    }

    @Override
    public void bindView(View view, Context context, @NonNull final Cursor cursor) {
        boolean selected;
        ViewHolder holder;
        if(view.getTag() == null){
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder = (ViewHolder)view.getTag();
        String student_id = cursor.getString(cursor.getColumnIndex(StudentRecords.STUDENT_ID));
        selectedStudents.put(student_id, 1);
        selectedStudentList.add(student_id);
        holder.studentId.setText(student_id);
        holder.studentName.setText(cursor.getString(cursor.getColumnIndex(StudentRecords.STUDENT_NAME)));
        holder.studentPosition.setText(cursor.getString(cursor.getColumnIndex(StudentRecords.COLUMN_ID)));
        selected = cursor.getInt(cursor.getColumnIndex(StudentRecords.STUDENT_SELECTED)) == 1;
        holder.studentCheckbox.setSelected(selected);
        final ViewHolder finalHolder = holder;
        holder.studentCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int checked = 0;
                if(finalHolder.studentCheckbox.isChecked()){
                    checked =1;
                }
                selectedStudents.put(finalHolder.studentId.getText().toString(), checked);
            }
        });
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(holder);
        return v;
    }

    public static class ViewHolder {
        @Bind(R.id.student_name_position)
        TextView studentPosition;
        @Bind(R.id.student_name_field)
        TextView studentName;
        @Bind(R.id.student_id_field) TextView  studentId;
        @Bind(R.id.student_select_checkbox)
        CheckBox studentCheckbox;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}

