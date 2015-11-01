package com.project.quiz.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.project.quiz.R;
import com.project.quiz.adapters.CustomEventsAdapter;
import com.project.quiz.customviews.IconTextView;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.extendedcalendarview.CalendarProvider;
import com.project.quiz.interfaces.OnEventChange;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUpcomingEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUpcomingEvents extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, OnEventChange {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private CustomEventsAdapter adapter;

    @Bind(android.R.id.list) ListView upcomingEventsList;
    @Bind(R.id.noeventIcon)
    IconTextView noEventIcon;
    @Bind(R.id.noeventText)
    TextViewRegularFont noEventText;

    public static FragmentUpcomingEvents newInstance(String param1, String param2) {
        FragmentUpcomingEvents fragment = new FragmentUpcomingEvents();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentUpcomingEvents() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        long c = new CalendarProvider().getCountOfEvents(getActivity());
            View v = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
            ButterKnife.bind(this, v);
        if(c!= 0) {
            adapter = new CustomEventsAdapter(this,getActivity(), R.layout.custom_upcoming_events, null, 0);
            upcomingEventsList.setAdapter(adapter);
            getLoaderManager().initLoader(0, null, this);
        }
        else{
            showNoEvents();
        }
            return v;

    }

    @Override
    public void showNoEvents() {
        upcomingEventsList.setVisibility(View.GONE);
        noEventIcon.setVisibility(View.VISIBLE);
        noEventText.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = {CalendarProvider.ID, CalendarProvider.EVENT, CalendarProvider.EVENT_ID, CalendarProvider.LOCATION, CalendarProvider.START_DAY, CalendarProvider.END_DAY, CalendarProvider.START, CalendarProvider.END, CalendarProvider.DESCRIPTION};
        return new CursorLoader(getActivity(), CalendarProvider.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
