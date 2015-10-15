package com.project.quiz.fragments;


import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.project.quiz.R;
import com.project.quiz.customviews.EditTextRegularFont;
import com.project.quiz.extendedcalendarview.CalendarProvider;
import com.project.quiz.extendedcalendarview.Event;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddEvents extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Bind(R.id.timePicker) TimePicker timePicker;
    @Bind(R.id.datePicker) DatePicker datePicker;
    @Bind(R.id.timePickerEnd) TimePicker timePickerEnd;
    @Bind(R.id.datePickerEnd) DatePicker datePickerEnd;
    @Bind(R.id.event_title_layout) TextInputLayout eventTitleLayout;
    @Bind(R.id.event_desc_layout) TextInputLayout eventDescriptionLayout;
    @Bind(R.id.event_title_field) EditTextRegularFont eventTitle;
    @Bind(R.id.event_location_layout) TextInputLayout eventLocationLayout;
    @Bind(R.id.event_location_field) EditTextRegularFont eventLocation;
    @Bind(R.id.event_desc_field) EditTextRegularFont eventDescription;
    @OnClick(R.id.button_confirm)
    public void OnConfirm(){
        boolean result = validateData();
        if(result){
            Toast.makeText(getActivity(), "Your event has been added", Toast.LENGTH_LONG).show();
            ContentValues values = new ContentValues();
            values.put(CalendarProvider.EVENT_ID, UUID.randomUUID().toString());
            values.put(CalendarProvider.COLOR, Event.COLOR_RED);
            values.put(CalendarProvider.DESCRIPTION, eventDescription.getText().toString().trim());
            values.put(CalendarProvider.LOCATION, eventLocation.getText().toString().trim());
            values.put(CalendarProvider.EVENT, eventTitle.getText().toString().trim());
            Calendar cal = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());

            int julianDay = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
            values.put(CalendarProvider.START, cal.getTimeInMillis());
            values.put(CalendarProvider.START_DAY, julianDay);

            cal.set(datePickerEnd.getYear(), datePickerEnd.getMonth(), datePickerEnd.getDayOfMonth(), timePickerEnd.getCurrentHour(), timePickerEnd.getCurrentMinute());
            int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));

            values.put(CalendarProvider.END, cal.getTimeInMillis());
            values.put(CalendarProvider.END_DAY, endDayJulian);

            getActivity().getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
        }
    }

    private boolean validateData() {
        boolean result = true;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DATE);
        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minutes = Calendar.getInstance().get(Calendar.MINUTE);

        int eventYear = datePicker.getYear();
        int eventMonth = datePicker.getMonth();
        int eventDate = datePicker.getDayOfMonth();
        int timeHour = timePicker.getCurrentHour();
        int timeMinute = timePicker.getCurrentMinute();

        int eventYearEnd = datePickerEnd.getYear();
        int eventMonthEnd = datePickerEnd.getMonth();
        int eventDateEnd = datePickerEnd.getDayOfMonth();
        int timeHourEnd = timePickerEnd.getCurrentHour();
        int timeMinuteEnd = timePickerEnd.getCurrentMinute();

        if(eventTitle.getText().toString().trim().length() <= 0){
            eventTitleLayout.setError("Enter Event title");
            result = false;
        }
        if(eventDescription.getText().toString().trim().length() <= 0){
            eventDescriptionLayout.setError("Enter Event description");
            result = false;
        }

        if(eventLocation.getText().toString().trim().length() <= 0){
            eventLocationLayout.setError("Enter Event location");
            result = false;
        }

        if(eventYear < year || eventMonth < month || eventDate < date){
            Toast.makeText(getActivity(), "Choose correct date", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if(eventYear == year && eventMonth == month && eventDate == date && (timeHour < hours)){
            Toast.makeText(getActivity(), "Choose correct time", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if((eventYearEnd < year || eventMonthEnd < month || eventDateEnd < date) && (eventYear>eventYearEnd || eventMonth > eventMonthEnd || eventDate > eventDateEnd)){
            Toast.makeText(getActivity(), "Choose correct date", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if(eventYearEnd == year && eventMonthEnd == month && eventDateEnd == date && (timeHourEnd <hours)){
            Toast.makeText(getActivity(), "Choose correct time", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAddEvents.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddEvents newInstance(String param1, String param2) {
        FragmentAddEvents fragment = new FragmentAddEvents();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentAddEvents() {
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
        View v = inflater.inflate(R.layout.fragment_add_events, container, false);
        ButterKnife.bind(this, v);
        timePicker.setIs24HourView(true);
        timePickerEnd.setIs24HourView(true);
        return v;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
}
