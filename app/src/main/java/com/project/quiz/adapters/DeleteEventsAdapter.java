package com.project.quiz.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
public class DeleteEventsAdapter extends CursorAdapter {

    private Context context;
    private Activity activity;
    private int layout;

    public DeleteEventsAdapter(Context context, Cursor cursor, Activity activity, int layout) {
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
    public void bindView(final View v, final Context context, final Cursor cursor) {
        ViewHolder viewHolder;
        if (v.getTag() == null) {
            viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) v.getTag();
        viewHolder.eventDelete.setVisibility(View.VISIBLE);
        viewHolder.eventAdd.setVisibility(View.GONE);

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

        viewHolder.eventDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cursor!=null) {
                    deleteEvent(eventId, v);
                }
            }
        });
    }

    private void deleteEvent(final String eventId, final View v) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuizEvents");
        query.whereEqualTo(CalendarProvider.EVENT_ID, eventId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() > 0){
                        for(ParseObject event : objects){
                            event.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    final Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_out_right);
                                    animation.setDuration(1000);
                                    v.startAnimation(animation);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            context.getContentResolver().delete(CalendarProvider.CONTENT_URI, CalendarProvider.EVENT_ID + "=?", new String[]{eventId});
                                            context.getContentResolver().notifyChange(CalendarProvider.CONTENT_ALL_EVENT_URI, null);
                                        }
                                    }, 900);
                                }
                            });
                        }
                    }
                }
                else{
                    Log.e("Fail", "Event not deleted");
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
