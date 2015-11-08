package com.project.quiz.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.project.quiz.R;
import com.project.quiz.adapters.ListAllEventsAdapter;
import com.project.quiz.customviews.IconTextView;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.extendedcalendarview.CalendarProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAllEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAllEvents extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String QUIZ_LABEL = "QUIZ_EVENTS";

    private String mParam1;
    private String mParam2;
    private ListAllEventsAdapter adapter;

    @Bind(android.R.id.list)
    ListView allEventsList;
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

    public FragmentAllEvents() {
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
        View v = inflater.inflate(R.layout.fragment_all_events, container, false);
        ButterKnife.bind(this, v);

        adapter = new ListAllEventsAdapter(getContext(), null, getActivity(), R.layout.custom_upcoming_events);
        allEventsList.setAdapter(adapter);

        getLoaderManager().initLoader(1, null, this);
        return v;

    }

    public void showNoEvents() {
        allEventsList.setVisibility(View.GONE);
        noEventIcon.setVisibility(View.VISIBLE);
        noEventText.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = {CalendarProvider.ID, CalendarProvider.EVENT, CalendarProvider.EVENT_ID, CalendarProvider.LOCATION, CalendarProvider.START_DAY, CalendarProvider.END_DAY, CalendarProvider.START, CalendarProvider.END, CalendarProvider.DESCRIPTION, CalendarProvider.LOCATION_ID};
        return new CursorLoader(getActivity(), CalendarProvider.CONTENT_ALL_EVENT_URI, projection, null, null, null);
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