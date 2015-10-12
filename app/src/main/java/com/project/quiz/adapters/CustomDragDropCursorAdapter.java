package com.project.quiz.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.project.quiz.R;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.customClasses.DragObject;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.log.Logging;
import com.project.quiz.utils.CommonLibs;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shitij on 12/10/15.
 */
public class CustomDragDropCursorAdapter extends SimpleCursorAdapter {
    private int layout;
    private Context context;
    public CustomDragDropCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to);
        this.layout = layout;
        this.context = context;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolder holder;
        if(view.getTag() == null){
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder = (ViewHolder)view.getTag();
        String student_id = cursor.getString(cursor.getColumnIndex(StudentRecords.STUDENT_ID));
        holder.studentId.setText(student_id);
        holder.studentName.setText(cursor.getString(cursor.getColumnIndex(StudentRecords.STUDENT_NAME)));
        holder.container.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                Logging.logMessage("Drop", String.valueOf(dragEvent.getAction()), CommonLibs.Priority.LOW);
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DROP: {
                        DragObject obj = (DragObject) dragEvent.getLocalState();
                        ListView newParent = (ListView) view.getParent();
                        CustomDragDropCursorAdapter newAdapter = (CustomDragDropCursorAdapter) newParent.getAdapter();
                        Cursor newCursor = newAdapter.getCursor();
                        int team = obj.getTeam();
                        String selected = obj.getSelected();
                        if (obj.getCursor().equals(newCursor)) {
                            return false;
                        } else {
                            TextViewRegularFont idView = (TextViewRegularFont) obj.getView().findViewById(R.id.student_id_field);
                            String id = idView.getText().toString().trim();
                            ContentValues values = new ContentValues();
                            values.put(StudentRecords.TEAM_NUMBER, team);
                            values.put(StudentRecords.STUDENT_SELECTED, selected);
                            context.getContentResolver().update(DataContentProvider.CONTENT_STORE_STUDENTS_URI, values, StudentRecords.STUDENT_ID + "=?", new String[]{id});
                            context.getContentResolver().notifyChange(DataContentProvider.CONTENT_STORE_STUDENTS_URI, null);
                            return true;
                        }
                    }
                }
                return true;
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
        @Bind(R.id.student_name_field) TextViewRegularFont studentName;
        @Bind(R.id.student_id_field) TextViewRegularFont  studentId;
        @Bind(R.id.dragContainer) LinearLayout container;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
