package com.project.quiz.activities;

import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.project.quiz.R;
import com.project.quiz.customviews.EditTextRegularFont;
import com.project.quiz.fragments.FragmentSignIn;
import com.project.quiz.fragments.FragmentSignUp;
import com.project.quiz.interfaces.ChangeFragment;
import com.project.quiz.utils.CommonLibs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityLogon extends AppCompatActivity implements ChangeFragment, FragmentSignUp.OnFragmentInteractionListener, FragmentSignIn.OnFragmentInteractionListener {
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        ButterKnife.bind(this);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        loadFragment(CommonLibs.FragmentId.ID_FRAGMENT_SIGN_IN, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_logon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadFragment(int id, Bundle bundle) {
        if(id == CommonLibs.FragmentId.ID_FRAGMENT_SIGN_IN){
            FragmentSignIn fragmentSignIn = new FragmentSignIn();
            getFragmentManager().beginTransaction().replace(R.id.container,fragmentSignIn,"FragmentSignIn").addToBackStack("FragmentSignIn").commit();
        }
        if(id == CommonLibs.FragmentId.ID_FRAGMENT_SIGN_UP){
            FragmentSignUp fragmentSignUp = new FragmentSignUp();
            getFragmentManager().beginTransaction().replace(R.id.container,fragmentSignUp,"FragmentSignUp").addToBackStack("FragmentSignUp").commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSignInFragmentInteraction(Uri uri) {

    }

    @Override
    public void signUp() {
        loadFragment(CommonLibs.FragmentId.ID_FRAGMENT_SIGN_UP, null);
    }
}
