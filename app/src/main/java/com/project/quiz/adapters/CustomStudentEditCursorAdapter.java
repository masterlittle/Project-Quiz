package com.project.quiz.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.fragments.FragmentSelectStudents;
import com.project.quiz.interfaces.DialogBoxListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shitij on 27/09/15.
 */
public class CustomStudentEditCursorAdapter extends SimpleCursorAdapter {
    public Context context;
    private int layout;
    private DialogBoxListener listener;

    public CustomStudentEditCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.layout = layout;
        listener = (DialogBoxListener)context;
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
        holder.studentPosition.setText(cursor.getString(cursor.getColumnIndex(StudentRecords.COLUMN_ID)));
        int score = Integer.parseInt(cursor.getString(cursor.getColumnIndex(StudentRecords.STUDENT_SCORE)));

        if(score-100 <0) {
            holder.studentScore.setMinValue(0);
        }
        else{
            holder.studentScore.setMinValue(score - 100);
        }
        holder.studentScore.setMaxValue(score + 500);
        holder.studentScore.setWrapSelectorWheel(false);

        holder.studentScore.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                ContentValues values = new ContentValues();
                values.put(StudentRecords.STUDENT_SCORE, i1);
                context.getContentResolver().update(DataContentProvider.CONTENT_STORE_STUDENTS_URI, values, StudentRecords.STUDENT_ID+ "=?", new String[]{student_id});
            }
        });
        holder.studentDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showEditDialog(0, student_id);

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
        @Bind(R.id.student_score_field)
        NumberPicker studentScore;
        @Bind(R.id.student_delete_button)
        TextViewRegularFont studentDelete;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}

