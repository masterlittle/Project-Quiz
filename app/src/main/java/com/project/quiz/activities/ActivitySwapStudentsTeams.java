package com.project.quiz.activities;

import android.app.LoaderManager;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleCursorAdapter;

import com.project.quiz.R;
import com.project.quiz.adapters.CustomDragDropCursorAdapter;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.customClasses.DragObject;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.log.Logging;
import com.project.quiz.utils.CommonLibs;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivitySwapStudentsTeams extends AppCompatActivity implements NumberPicker.OnValueChangeListener, LoaderManager.LoaderCallbacks<Cursor> {
    private int team = 1;
    private SimpleCursorAdapter selectedAdapter;
    private SimpleCursorAdapter unSelectedAdapter;
    private Cursor selectedCursor;
    private Cursor unSelectedCursor;

    @Bind(R.id.list_selected)
    ListView selectedList;
    @Bind(R.id.list_unselected)
    ListView unSelectedList;
    @Bind(R.id.numberPicker)
    NumberPicker numberPicker;
    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_students_teams);
        ButterKnife.bind(this);

        toolbar.setTitle("Swap students");
        setSupportActionBar(toolbar);
        String numberOfTeam = getIntent().getStringExtra("team");

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(Integer.parseInt(numberOfTeam));
        numberPicker.setOnValueChangedListener(this);

        String from[] = {StudentRecords.STUDENT_ID, StudentRecords.STUDENT_NAME};
        int to[] = {R.id.student_id_field, R.id.student_name_field};
        selectedAdapter = new CustomDragDropCursorAdapter(this, R.layout.custom_student_swap_students_view, null, from, to, 0);
        unSelectedAdapter = new CustomDragDropCursorAdapter(this, R.layout.custom_student_swap_students_view, null, from, to, 0);
        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().initLoader(1, null, this);
        selectedList.setAdapter(selectedAdapter);
        unSelectedList.setAdapter(unSelectedAdapter);

        selectedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(view);
                TextViewRegularFont nameField = (TextViewRegularFont) view.findViewById(R.id.student_name_field);
                ClipData data = ClipData.newPlainText("Student", nameField.getText().toString().trim());
                DragObject obj = new DragObject(selectedCursor, view, -1, "0");
                view.startDrag(data, dragShadow, obj, 0);
                return true;
            }
        });

        selectedList.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                Logging.logMessage("Drop", String.valueOf(dragEvent.getAction()), CommonLibs.Priority.LOW);
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DROP: {
                        return doOnDrag(view, dragEvent);
                    }
                }
                return true;
            }
        });

        unSelectedList.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                Logging.logMessage("Drop", String.valueOf(dragEvent.getAction()), CommonLibs.Priority.LOW);
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DROP: {
                        return doOnDrag(view, dragEvent);
                    }
                }
                return true;
            }
        });


        unSelectedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(view);
                TextViewRegularFont nameField = (TextViewRegularFont) view.findViewById(R.id.student_name_field);
                ClipData data = ClipData.newPlainText("Student", nameField.getText().toString().trim());
                DragObject obj = new DragObject(unSelectedCursor, view, team, "1");
                view.startDrag(data, dragShadow, obj, 0);
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_swap_students_teams, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        team = numberPicker.getValue();
        getLoaderManager().restartLoader(1, null, this);
    }

    private boolean doOnDrag(View view, DragEvent dragEvent) {
        DragObject obj = (DragObject) dragEvent.getLocalState();
        ListView newParent = (ListView) view;
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
            getContentResolver().update(DataContentProvider.CONTENT_STORE_STUDENTS_URI, values, StudentRecords.STUDENT_ID + "=?", new String[]{id});
            getContentResolver().notifyChange(DataContentProvider.CONTENT_STORE_STUDENTS_URI, null);
            return true;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (i == 0) {
            String projection[] = {StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME, StudentRecords.STUDENT_ID};
            String selection = StudentRecords.STUDENT_SELECTED + "=?";
            String selectionArgs[] = {"0"};
            return new CursorLoader(this, DataContentProvider.CONTENT_STORE_STUDENTS_URI, projection, selection, selectionArgs, StudentRecords.STUDENT_NAME);
        } else if (i == 1) {
            String projection[] = {StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME, StudentRecords.STUDENT_ID};
            String selection = StudentRecords.TEAM_NUMBER + "=? AND " + StudentRecords.STUDENT_SELECTED + "=?";
            String selectionArgs[] = {String.valueOf(team), "1"};
            return new CursorLoader(this, DataContentProvider.CONTENT_STORE_STUDENTS_URI, projection, selection, selectionArgs, StudentRecords.STUDENT_NAME);
        } else return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        if (id == 0) {
            unSelectedCursor = cursor;
            unSelectedAdapter.swapCursor(cursor);
        } else if (id == 1) {
            selectedCursor = cursor;
            selectedAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        if (id == 0) {
            unSelectedAdapter.swapCursor(null);
        } else if (id == 1) {
            selectedAdapter.swapCursor(null);
        }
    }

    private class DragShadow extends View.DragShadowBuilder {

        ColorDrawable greyBox;

        public DragShadow(View view) {
            super(view);
            // TODO Auto-generated constructor stub
            greyBox = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            // TODO Auto-generated method stub
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.colorPrimaryLight));
            paint.setTextSize(20f);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.CENTER);
            // super.onDrawShadow(canvas);
            TextViewRegularFont t = (TextViewRegularFont)getView().findViewById(R.id.student_name_field);
            canvas.drawText(t.getText().toString(), 0, 0, paint);
            greyBox.draw(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize,
                                           Point shadowTouchPoint) {
            // TODO Auto-generated method stub
            // super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
            View v = getView();

            int height = v.getHeight();
            int width = v.getWidth();

            greyBox.setBounds(0, 0, width, height);
            shadowSize.set(width, height);

            shadowTouchPoint.set(width, height);

        }
    }


}
