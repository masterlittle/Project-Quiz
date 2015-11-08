package com.project.quiz.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.project.quiz.R;
import com.project.quiz.customviews.EditTextRegularFont;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.utils.CommonLibs;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSignUp.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSignUp extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.username_field)
    EditTextRegularFont username;
    @Bind(R.id.phone_field)
    EditTextRegularFont phone;
    @Bind(R.id.year_field)
    EditTextRegularFont year;
    @Bind(R.id.password_field)
    EditTextRegularFont password;
    @Bind(R.id.password_verify_field)
    EditTextRegularFont verifyPassword;
    @Bind(R.id.name_field)
    EditTextRegularFont name;
    @Bind(R.id.name_layout)
    TextInputLayout nameLayout;
    @Bind(R.id.username_layout)
    TextInputLayout usernameLayout;
    @Bind(R.id.phone_layout)
    TextInputLayout phoneLayout;
    @Bind(R.id.year_layout)
    TextInputLayout yearLayout;
    @Bind(R.id.password_layout)
    TextInputLayout passwordLayout;
    @Bind(R.id.password_verify_layout)
    TextInputLayout verifyPasswordLayout;

    @OnClick(R.id.button_signup)
    public void onClick() {
        boolean result = validateData();
        if (result) {
            final ParseUser user = new ParseUser();
            user.setUsername(username.getText().toString().trim());
            user.setPassword(password.getText().toString().trim());
            user.put("phone", phone.getText().toString().trim());
            user.put("year", year.getText().toString().trim());
            user.put("name", name.getText().toString().trim());

            /**
             * Sign up User
             */
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "Sign Up successful", Toast.LENGTH_LONG).show();
                        ParseQuery<ParseRole> parseRole = ParseRole.getQuery();
                        parseRole.findInBackground(new FindCallback<ParseRole>() {
                            @Override
                            public void done(List<ParseRole> objects, ParseException e) {
                                if (e == null) {

                                    for (ParseRole role : objects) {
                                        if (role.getName().equalsIgnoreCase(CommonLibs.Roles.ROLE_ADMINISTRATOR)) {
                                            role.getUsers().add(user);
                                            role.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        if (mListener != null && !getActivity().isDestroyed())
                                                            mListener.onFragmentInteraction(null);
                                                        Log.e("Success", "Role success");
                                                    } else {
                                                        Log.e("Fail", e.getMessage());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        Log.e("Sign Up Error", e.toString());
                    }
                }
            });
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void setAnimations() {
        setAllowEnterTransitionOverlap(true);
        setEnterTransition(new Slide());
        setExitTransition(new Fade());
    }

    private boolean validateData() {
        boolean result = true;
        if (username.getText().toString().trim().length() <= 0 || !(isValidEmail(username.getText().toString().trim()))) {
            usernameLayout.setError("Enter valid email");
            result = false;
        }
        if (name.getText().toString().trim().length() <= 0) {
            nameLayout.setError("Enter your name");
            result = false;
        }
        if (phone.getText().toString().trim().length() <= 0) {
            phone.setError("Enter Phone Number");
            result = false;
        }
        if (year.getText().toString().trim().length() <= 0) {
            year.setError("Enter Year");
            result = false;
        }
        if (password.getText().toString().trim().length() <= 8) {
            password.setError("Enter password of atleast 8 characters");
            result = false;
        }
        if (!verifyPassword.getText().toString().trim().equalsIgnoreCase(password.getText().toString().trim())) {
            verifyPassword.setError("The two passwords do not match!");
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
     * @return A new instance of fragment FragmentSignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSignUp newInstance(String param1, String param2) {
        FragmentSignUp fragment = new FragmentSignUp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSignUp() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setAnimations();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
