package com.project.quiz.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.quiz.R;
import com.project.quiz.customviews.EditTextRegularFont;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSignIn.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSignIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSignIn extends Fragment {
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
    @Bind(R.id.password_field)
    EditTextRegularFont password;
    @Bind(R.id.username_layout)
    TextInputLayout usernameLayout;
    @Bind(R.id.password_layout)
    TextInputLayout passwordLayout;

    @OnClick(R.id.button_login)
    public void onClick() {
        boolean result = validateData();
        if (result) {
            ParseUser.logInInBackground(username.getText().toString().trim(), password.getText().toString().trim(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Toast.makeText(getActivity(), "You are successfully signed in", Toast.LENGTH_LONG).show();
                        mListener.onSignInFragmentInteraction(null);
                    } else {
                        Toast.makeText(getActivity(), "Invalid Login Paramters", Toast.LENGTH_SHORT).show();
                        Log.e("Login Error", e.toString());
                    }
                }
            });
        }
    }

    @OnTextChanged(R.id.username_field)
    public void onUsernameChangedListener(Editable s) {
        if (s.length() > 0) {
            usernameLayout.setErrorEnabled(false);
        }
    }

    @OnTextChanged(R.id.password_field)
    public void onPasswordChangedListener(Editable s) {
        if (s.length() > 0) {
            passwordLayout.setErrorEnabled(false);
        }
    }

    @OnClick(R.id.sign_up)
    public void clickSignUp() {
        mListener.signUp();
    }


    private boolean validateData() {
        boolean result = true;
        if (username.getText().toString().trim().length() <= 0) {
            usernameLayout.setError("Enter username");
            result = false;
        }
        if (password.getText().toString().trim().length() <= 0) {
            passwordLayout.setError("Enter password");
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
     * @return A new instance of fragment FragmentSignIn.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSignIn newInstance(String param1, String param2) {
        FragmentSignIn fragment = new FragmentSignIn();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSignIn() {
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
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, v);
        return v;
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
        public void onSignInFragmentInteraction(Uri uri);

        public void signUp();
    }

    private void setAnimations() {
        setAllowEnterTransitionOverlap(true);
        setEnterTransition(new Slide());
    }

}
