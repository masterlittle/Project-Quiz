package com.project.quiz.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.parse.ParseObject;
import com.project.quiz.R;
import com.project.quiz.customviews.IconTextView;
import com.project.quiz.customviews.TextViewBoldFont;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.extendedcalendarview.CalendarProvider;
import com.project.quiz.extendedcalendarview.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shitij on 22/10/15.
 */
public class ListAllEventsAdapter extends BaseAdapter {

    private List<ParseObject> listEvents;
    private Context context;
    private Activity activity;
    private int layout;
    public ListAllEventsAdapter(Context context, Activity activity, int layout, List<ParseObject> listEvents){
        this.layout = layout;
        this.context = context;
        this.activity = activity;
        this.listEvents = listEvents;
    }

    @Override
    public int getCount() {
        return listEvents.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View v, ViewGroup viewGroup) {
        View view = v;
        ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(layout, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder)view.getTag();
        viewHolder.eventDelete.setVisibility(View.GONE);
        viewHolder.eventAdd.setVisibility(View.VISIBLE);
        viewHolder.eventTitle.setText(listEvents.get(position).get(CalendarProvider.EVENT).toString());
        final String eventId = listEvents.get(position).get(CalendarProvider.EVENT_ID).toString();
        String startTime  = listEvents.get(position).get(CalendarProvider.START).toString();
        String endTime  = listEvents.get(position).get(CalendarProvider.END).toString();

        Date dateStart = new Date(Long.parseLong(startTime));
        String startTimeFormatted = new SimpleDateFormat("EEE, MMM dd, HH:mm:ss yyyy").format(dateStart);
        Date dateEnd = new Date(Long.parseLong(endTime));
        String endTimeFormatted = new SimpleDateFormat("EEE, MMM dd, HH:mm:ss yyyy").format(dateEnd);

        viewHolder.eventLocation.setText(listEvents.get(position).get(CalendarProvider.LOCATION).toString());
        viewHolder.eventEndTime.setText(endTimeFormatted);
        viewHolder.eventStartTime.setText(startTimeFormatted);
        viewHolder.eventDescription.setText(listEvents.get(position).get(CalendarProvider.DESCRIPTION).toString());

        viewHolder.eventAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(CalendarProvider.EVENT_ID, eventId);
                values.put(CalendarProvider.COLOR, Event.COLOR_RED);
                values.put(CalendarProvider.DESCRIPTION, listEvents.get(position).get(CalendarProvider.DESCRIPTION).toString());
                values.put(CalendarProvider.LOCATION, listEvents.get(position).get(CalendarProvider.LOCATION).toString());
                values.put(CalendarProvider.LOCATION_ID, listEvents.get(position).get(CalendarProvider.LOCATION_ID).toString());
                values.put(CalendarProvider.EVENT, listEvents.get(position).get(CalendarProvider.EVENT).toString());
                values.put(CalendarProvider.START, listEvents.get(position).get(CalendarProvider.START).toString());
                values.put(CalendarProvider.START_DAY, listEvents.get(position).get(CalendarProvider.START_DAY).toString());
                values.put(CalendarProvider.END, listEvents.get(position).get(CalendarProvider.END).toString());
                values.put(CalendarProvider.END_DAY, listEvents.get(position).get(CalendarProvider.END_DAY).toString());
                context.getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
            }
        });
        return view;
    }

    public static class ViewHolder {
        @Bind(R.id.eventTitle) TextViewBoldFont eventTitle;
        @Bind(R.id.eventLocationText) TextViewRegularFont eventLocation;
        @Bind(R.id.eventStartTime) TextViewRegularFont eventStartTime;
        @Bind(R.id.eventEndTime) TextViewRegularFont eventEndTime;
        @Bind(R.id.eventDescription) TextViewRegularFont eventDescription;
        @Bind(R.id.eventAdd) TextViewRegularFont eventAdd;
        @Bind(R.id.eventDelete) IconTextView eventDelete;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
