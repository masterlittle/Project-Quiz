package com.project.quiz.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;

import com.project.quiz.R;
import com.project.quiz.customviews.IconTextView;
import com.project.quiz.customviews.TextViewBoldFont;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.extendedcalendarview.CalendarProvider;
import com.project.quiz.fragments.FragmentUpcomingEvents;
import com.project.quiz.interfaces.OnEventChange;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shitij on 13/10/15.
 */
public class CustomEventsAdapter extends CursorAdapter {
    private Context context;
    private int layout;
    OnEventChange listener;

    public CustomEventsAdapter(FragmentUpcomingEvents frag, Context context, int layout, Cursor c, int flags) {
        super(context, c, 0);
        this.context = context;
        this.layout = layout;
        listener = (OnEventChange) frag;
    }

    @Override
    public void bindView(final View v, final Context context, @NonNull final Cursor cursor) {
        ViewHolder holder;
        if (v.getTag() == null) {
            holder = new ViewHolder(v);
            v.setTag(holder);
        }
        holder = (ViewHolder) v.getTag();

        final String eventId = cursor.getString(cursor.getColumnIndex(CalendarProvider.EVENT_ID));
        String startTime  = cursor.getString(cursor.getColumnIndex(CalendarProvider.START));
        String endTime  = cursor.getString(cursor.getColumnIndex(CalendarProvider.END));

        Date dateStart = new Date(Long.parseLong(startTime));
        String startTimeFormatted = new SimpleDateFormat("EEE, MMM dd, HH:mm:ss yyyy").format(dateStart);
        Date dateEnd = new Date(Long.parseLong(endTime));
        String endTimeFormatted = new SimpleDateFormat("EEE, MMM dd, HH:mm:ss yyyy").format(dateEnd);

        holder.eventTitle.setText(cursor.getString(cursor.getColumnIndex(CalendarProvider.EVENT)));
        holder.eventLocation.setText(cursor.getString(cursor.getColumnIndex(CalendarProvider.LOCATION)));
        holder.eventEndTime.setText(endTimeFormatted);
        holder.eventStartTime.setText(startTimeFormatted);
        holder.eventDescription.setText(cursor.getString(cursor.getColumnIndex(CalendarProvider.DESCRIPTION)));

        holder.eventDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                v.animate().alpha(0f).setDuration(500);
                final Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_out_right);
                animation.setDuration(1000);
                v.startAnimation(animation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        context.getContentResolver().delete(CalendarProvider.CONTENT_URI, CalendarProvider.EVENT_ID + "=?", new String[]{eventId});
                        context.getContentResolver().notifyChange(CalendarProvider.CONTENT_URI, null);
                        long c = new CalendarProvider().getCountOfEvents(context);
                        if(c == 0){
                            listener.showNoEvents();
                        }
                    }
                }, 900);

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
        @Bind(R.id.eventTitle) TextViewBoldFont eventTitle;
        @Bind(R.id.eventLocationText) TextViewRegularFont eventLocation;
        @Bind(R.id.eventStartTime) TextViewRegularFont eventStartTime;
        @Bind(R.id.eventEndTime) TextViewRegularFont eventEndTime;
        @Bind(R.id.eventDescription) TextViewRegularFont eventDescription;
        @Bind(R.id.eventDelete) IconTextView eventDelete;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
