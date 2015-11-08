package com.project.quiz.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;
import com.project.quiz.R;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.extendedcalendarview.ExtendedCalendarView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentHomeScreen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Bind(R.id.name_field) TextViewRegularFont username;
    @Bind(R.id.calendar) ExtendedCalendarView calendarView;
    private BroadcastReceiver broadcast;

    // TODO: Rename and change types and number of parameters
    public static FragmentHomeScreen newInstance(String param1, String param2) {
        FragmentHomeScreen fragment = new FragmentHomeScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentHomeScreen() {
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
        View v = inflater.inflate(R.layout.fragment_home_screen, container, false);
        ButterKnife.bind(this, v);
        ParseUser user  = ParseUser.getCurrentUser();
        if(user!=null)
            setUsername(user.get("name").toString());
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setUsername(String name) {
        username.setText("Welcome " + name);
    }
}
