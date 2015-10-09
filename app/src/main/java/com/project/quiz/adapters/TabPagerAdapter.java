package com.project.quiz.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.project.quiz.fragments.FragmentAddStudentRecords;
import com.project.quiz.fragments.FragmentDisplayScore;

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
                return new FragmentDisplayScore();
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
