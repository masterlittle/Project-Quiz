package com.project.quiz.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.quiz.fragments.FragmentAddStudentRecords;
import com.project.quiz.fragments.FragmentAllEvents;
import com.project.quiz.fragments.FragmentDeleteEvents;
import com.project.quiz.fragments.FragmentEditStudentDetails;
import com.project.quiz.fragments.FragmentExportAsCsv;

/**
 * Created by Shitij on 27/09/15.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {
    private String[] tabs;
    private int length;

    public TabPagerAdapter(FragmentManager fm, String[] tabs, int length) {
        super(fm);
        this.tabs = tabs;
        this.length = length;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new FragmentAddStudentRecords();
            case 1:
                return new FragmentEditStudentDetails();
            case 2:
                return new FragmentDeleteEvents();
            case 3:
                return new FragmentExportAsCsv();
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
