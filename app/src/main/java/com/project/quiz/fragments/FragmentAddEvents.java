package com.project.quiz.fragments;


import android.content.ContentValues;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.parse.ParseObject;
import com.project.quiz.R;
import com.project.quiz.adapters.PlaceAutocompleteAdapter;
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
import butterknife.OnItemClick;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddEvents extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String placeId;
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    @Bind(R.id.timePicker)
    TimePicker timePicker;
    @Bind(R.id.datePicker)
    DatePicker datePicker;
    @Bind(R.id.timePickerEnd)
    TimePicker timePickerEnd;
    @Bind(R.id.datePickerEnd)
    DatePicker datePickerEnd;
    @Bind(R.id.event_title_layout)
    TextInputLayout eventTitleLayout;
    @Bind(R.id.event_desc_layout)
    TextInputLayout eventDescriptionLayout;
    @Bind(R.id.event_title_field)
    EditTextRegularFont eventTitle;
    @Bind(R.id.event_location)
    AutoCompleteTextView eventLocation;
    @Bind(R.id.event_desc_field)
    EditTextRegularFont eventDescription;

    @OnClick(R.id.button_confirm)
    public void OnConfirm() {
        boolean result = validateData();
        if (result) {
            Toast.makeText(getActivity(), "Your event has been added", Toast.LENGTH_LONG).show();
            String randomId = UUID.randomUUID().toString();
            /**
             * Store Event in Server
             */

            ParseObject parseEvent = new ParseObject("QuizEvents");
            parseEvent.put(CalendarProvider.EVENT_ID, randomId);
            parseEvent.put(CalendarProvider.DESCRIPTION, eventDescription.getText().toString().trim());
            parseEvent.put(CalendarProvider.LOCATION, eventLocation.getText().toString().trim());
            parseEvent.put(CalendarProvider.LOCATION_ID, placeId);
            parseEvent.put(CalendarProvider.EVENT, eventTitle.getText().toString().trim());
            /**
             * Store event on local database
             */
            ContentValues values = new ContentValues();
            values.put(CalendarProvider.EVENT_ID, randomId);
            values.put(CalendarProvider.COLOR, Event.COLOR_RED);
            values.put(CalendarProvider.DESCRIPTION, eventDescription.getText().toString().trim());
            values.put(CalendarProvider.LOCATION, eventLocation.getText().toString().trim());
            values.put(CalendarProvider.LOCATION_ID, placeId);
            values.put(CalendarProvider.EVENT, eventTitle.getText().toString().trim());
            Calendar cal = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());

            int julianDay = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
            values.put(CalendarProvider.START, cal.getTimeInMillis());
            values.put(CalendarProvider.START_DAY, julianDay);


            parseEvent.put(CalendarProvider.START, cal.getTimeInMillis());
            parseEvent.put(CalendarProvider.START_DAY, julianDay);

            cal.set(datePickerEnd.getYear(), datePickerEnd.getMonth(), datePickerEnd.getDayOfMonth(), timePickerEnd.getCurrentHour(), timePickerEnd.getCurrentMinute());
            int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));

            values.put(CalendarProvider.END, cal.getTimeInMillis());
            values.put(CalendarProvider.END_DAY, endDayJulian);

            parseEvent.put(CalendarProvider.END, cal.getTimeInMillis());
            parseEvent.put(CalendarProvider.END_DAY, endDayJulian);

            getActivity().getContentResolver().insert(CalendarProvider.CONTENT_URI, values);

            parseEvent.saveEventually();
        }
    }

    private boolean validateData() {
        boolean result = true;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DATE);
        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minutes = Calendar.getInstance().get(Calendar.MINUTE);
        int totalDays= date + (month*30);

        int eventYear = datePicker.getYear();
        int eventMonth = datePicker.getMonth();
        int eventDate = datePicker.getDayOfMonth();
        int timeHour = timePicker.getCurrentHour();
        int timeMinute = timePicker.getCurrentMinute();
        int totalDaysStart = eventDate + (eventMonth * 30) + ((eventYear-year) * 12);

        int eventYearEnd = datePickerEnd.getYear();
        int eventMonthEnd = datePickerEnd.getMonth();
        int eventDateEnd = datePickerEnd.getDayOfMonth();
        int timeHourEnd = timePickerEnd.getCurrentHour();
        int timeMinuteEnd = timePickerEnd.getCurrentMinute();
        int totalDaysEnd = eventDateEnd + (eventMonthEnd * 30) + ((eventYearEnd-year) * 12);

        if (eventTitle.getText().toString().trim().length() <= 0) {
            eventTitleLayout.setError("Enter Event title");
            result = false;
        }
        if (eventDescription.getText().toString().trim().length() <= 0) {
            eventDescriptionLayout.setError("Enter Event description");
            result = false;
        }

        if (eventLocation.getText().toString().trim().length() <= 0) {
            Toast.makeText(getActivity(), "Enter the location", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (totalDays > totalDaysStart) {
            Toast.makeText(getActivity(), "Choose correct date", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (eventYear == year && eventMonth == month && eventDate == date && (timeHour < hours)) {
            Toast.makeText(getActivity(), "Choose correct time", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (totalDaysStart >totalDaysEnd) {
            Toast.makeText(getActivity(), "Choose correct date", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (eventYearEnd == year && eventMonthEnd == month && eventDateEnd == date && (timeHourEnd < hours)) {
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
        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .build();

        mAdapter = new PlaceAutocompleteAdapter(getContext(), mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        eventLocation.setAdapter(mAdapter);
        eventLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
                final AutocompletePrediction item = mAdapter.getItem(position);
                placeId = item.getPlaceId();
                final CharSequence primaryText = item.getPrimaryText(null);
            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

                Log.i("Add events", "Called getPlaceById to get Place details for " + placeId);
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e("Add events", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
            Spanned placeDetails = formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri());

            places.release();
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e("Add events", res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e("Add events", "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(getActivity(),
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
