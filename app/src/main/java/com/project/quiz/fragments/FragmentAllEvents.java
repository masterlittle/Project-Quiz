package com.project.quiz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.project.quiz.R;
import com.project.quiz.adapters.ListAllEventsAdapter;
import com.project.quiz.customviews.IconTextView;
import com.project.quiz.customviews.TextViewRegularFont;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAllEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAllEvents extends Fragment {
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

        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuizEvents");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> eventList, ParseException e) {
                if (e == null) {
                    if (eventList.size() > 0) {
                        adapter = new ListAllEventsAdapter(getContext(), getActivity(), R.layout.custom_upcoming_events, eventList);
                        allEventsList.setAdapter(adapter);
                    } else {
                        showNoEvents();
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        return v;

    }

    public void showNoEvents() {
        allEventsList.setVisibility(View.GONE);
        noEventIcon.setVisibility(View.VISIBLE);
        noEventText.setVisibility(View.VISIBLE);
    }

}