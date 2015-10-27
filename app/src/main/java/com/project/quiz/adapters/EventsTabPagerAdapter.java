package com.project.quiz.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.quiz.fragments.FragmentAddEvents;
import com.project.quiz.fragments.FragmentAddStudentRecords;
import com.project.quiz.fragments.FragmentAllEvents;
import com.project.quiz.fragments.FragmentEditStudentDetails;
import com.project.quiz.fragments.FragmentUpcomingEvents;

/**
 * Created by Shitij on 27/09/15.
 */
public class EventsTabPagerAdapter extends FragmentPagerAdapter {
    private String[] tabs;
    private int length;

    public EventsTabPagerAdapter(FragmentManager fm, String[] tabs, int length) {
        super(fm);
        this.tabs = tabs;
        this.length = length;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new FragmentUpcomingEvents();
            case 1:
                return new FragmentAddEvents();
            case 2:
                return new FragmentAllEvents();

        }

        return null;
    }

    @Override
    public int getCount() {
        return length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
