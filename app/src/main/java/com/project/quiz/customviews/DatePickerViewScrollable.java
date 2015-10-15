package com.project.quiz.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;

/**
 * Created by Shitij on 14/10/15.
 */
public class DatePickerViewScrollable extends DatePicker {

    public DatePickerViewScrollable(Context context) {
        super(context);
    }

    public DatePickerViewScrollable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DatePickerViewScrollable(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }

        return false;
    }

}