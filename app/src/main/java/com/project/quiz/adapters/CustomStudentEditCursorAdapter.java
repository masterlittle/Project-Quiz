package com.project.quiz.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.project.quiz.R;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.customClasses.CustomNumberPicker;
import com.project.quiz.customviews.IconTextView;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.fragments.FragmentSelectStudents;
import com.project.quiz.interfaces.DialogBoxListener;
import com.project.quiz.interfaces.NumberPickerListener;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shitij on 27/09/15.
 */
public class CustomStudentEditCursorAdapter extends SimpleCursorAdapter{
    public Context context;
    private int layout;
    private DialogBoxListener listener;
    private HashMap<String, Integer> studentScores;
    private CustomStudentEditCursorAdapter adapter;

    public CustomStudentEditCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.layout = layout;
        studentScores = new HashMap<>();
        listener = (DialogBoxListener)context;
    }

    public void getInstance(CustomStudentEditCursorAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public void bindView(View view, final Context context, @NonNull final Cursor cursor) {
        ViewHolder holder;
        if (view.getTag() == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();
        final String student_id = cursor.getString(cursor.getColumnIndex(StudentRecords.STUDENT_ID));
        holder.studentName.setText(cursor.getString(cursor.getColumnIndex(StudentRecords.STUDENT_NAME)));
        holder.studentPosition.setText(String.valueOf(cursor.getPosition()+1));
        final int score = Integer.parseInt(cursor.getString(cursor.getColumnIndex(StudentRecords.STUDENT_SCORE)));
        holder.studentScore.setText(String.valueOf(score));
        studentScores.put(student_id, score);

        holder.studentScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomNumberPicker dialog = new CustomNumberPicker(context, score, student_id, adapter);
                dialog.show();
            }
        });
        holder.studentDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showEditDialog(0, student_id);

            }
        });
    }

    public HashMap<String, Integer> getStudentScores() {
        return studentScores;
    }

    public void setStudentScores(int score, String id) {
        studentScores.put(id, score);
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
        TextViewRegularFont studentPosition;
        @Bind(R.id.student_name_field)
        TextViewRegularFont studentName;
        @Bind(R.id.student_score_field)
        TextViewRegularFont studentScore;
        @Bind(R.id.student_delete_button)
        IconTextView studentDelete;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}

