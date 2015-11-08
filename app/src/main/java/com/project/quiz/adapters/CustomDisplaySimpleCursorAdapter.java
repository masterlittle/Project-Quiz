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
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.database.StudentRecords;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shitij on 11/10/15.
 */
public class CustomDisplaySimpleCursorAdapter extends SimpleCursorAdapter {

    public Context context;
    private int layout;

    public CustomDisplaySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.layout = layout;
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
        holder.studentName.setText(cursor.getString(cursor.getColumnIndex(StudentRecords.STUDENT_NAME)));
        holder.studentPosition.setText(String.valueOf(cursor.getPosition()+1));
        holder.studentScore.setText(cursor.getString(cursor.getColumnIndex(StudentRecords.STUDENT_SCORE)));
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
        @Bind(R.id.student_score_field) TextViewRegularFont  studentScore;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
