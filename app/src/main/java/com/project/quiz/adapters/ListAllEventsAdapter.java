package com.project.quiz.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.project.quiz.R;
import com.project.quiz.customviews.IconTextView;
import com.project.quiz.customviews.TextViewBoldFont;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.extendedcalendarview.CalendarProvider;
import com.project.quiz.extendedcalendarview.Event;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shitij on 22/10/15.
 */
public class ListAllEventsAdapter extends CursorAdapter {

    private Context context;
    private Activity activity;
    private int layout;

    public ListAllEventsAdapter(Context context, Cursor cursor, Activity activity, int layout) {
        super(context, cursor);
        this.layout = layout;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        View view = LayoutInflater.from(context).inflate(layout, viewGroup, false);
        viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder viewHolder;
        if (view.getTag() == null) {
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        viewHolder.eventDelete.setVisibility(View.GONE);
        viewHolder.eventAdd.setVisibility(View.VISIBLE);

        final String eventId = cursor.getString(cursor.getColumnIndex(CalendarProvider.EVENT_ID));
        String startTime = cursor.getString(cursor.getColumnIndex(CalendarProvider.START));
        String endTime = cursor.getString(cursor.getColumnIndex(CalendarProvider.END));

        Date dateStart = new Date(Long.parseLong(startTime));
        String startTimeFormatted = new SimpleDateFormat("EEE, MMM dd, HH:mm:ss yyyy").format(dateStart);
        Date dateEnd = new Date(Long.parseLong(endTime));
        String endTimeFormatted = new SimpleDateFormat("EEE, MMM dd, HH:mm:ss yyyy").format(dateEnd);

        viewHolder.eventTitle.setText(cursor.getString(cursor.getColumnIndex(CalendarProvider.EVENT)));
        viewHolder.eventLocation.setText(cursor.getString(cursor.getColumnIndex(CalendarProvider.LOCATION)));
        viewHolder.eventEndTime.setText(endTimeFormatted);
        viewHolder.eventStartTime.setText(startTimeFormatted);
        viewHolder.eventDescription.setText(cursor.getString(cursor.getColumnIndex(CalendarProvider.DESCRIPTION)));

        viewHolder.eventAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cursor!=null) {
                    ContentValues values = new ContentValues();
                    values.put(CalendarProvider.EVENT_ID, eventId);
                    values.put(CalendarProvider.COLOR, Event.COLOR_RED);
                    values.put(CalendarProvider.DESCRIPTION, cursor.getString(cursor.getColumnIndex(CalendarProvider.DESCRIPTION)));
                    values.put(CalendarProvider.LOCATION, cursor.getString(cursor.getColumnIndex(CalendarProvider.LOCATION)));
                    values.put(CalendarProvider.LOCATION_ID, cursor.getString(cursor.getColumnIndex(CalendarProvider.LOCATION_ID)));
                    values.put(CalendarProvider.EVENT, cursor.getString(cursor.getColumnIndex(CalendarProvider.EVENT)));
                    values.put(CalendarProvider.START, cursor.getString(cursor.getColumnIndex(CalendarProvider.START)));
                    values.put(CalendarProvider.START_DAY, cursor.getString(cursor.getColumnIndex(CalendarProvider.START_DAY)));
                    values.put(CalendarProvider.END, cursor.getString(cursor.getColumnIndex(CalendarProvider.END)));
                    values.put(CalendarProvider.END_DAY, cursor.getString(cursor.getColumnIndex(CalendarProvider.END_DAY)));
                    context.getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
                }
            }
        });
    }

    public static class ViewHolder {
        @Bind(R.id.eventTitle)
        TextViewBoldFont eventTitle;
        @Bind(R.id.eventLocationText)
        TextViewRegularFont eventLocation;
        @Bind(R.id.eventStartTime)
        TextViewRegularFont eventStartTime;
        @Bind(R.id.eventEndTime)
        TextViewRegularFont eventEndTime;
        @Bind(R.id.eventDescription)
        TextViewRegularFont eventDescription;
        @Bind(R.id.eventAdd)
        TextViewRegularFont eventAdd;
        @Bind(R.id.eventDelete)
        IconTextView eventDelete;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
