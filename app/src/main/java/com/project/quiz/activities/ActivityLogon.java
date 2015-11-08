package com.project.quiz.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

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
        setAnimations();
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
            getFragmentManager().beginTransaction().replace(R.id.container,fragmentSignIn,"FragmentSignIn").commit();
        }
        if(id == CommonLibs.FragmentId.ID_FRAGMENT_SIGN_UP){
            FragmentSignUp fragmentSignUp = new FragmentSignUp();
            getFragmentManager().beginTransaction().replace(R.id.container,fragmentSignUp,"FragmentSignUp").addToBackStack("FragmentSignUp").commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.project.quiz.LOGIN_SUCCESS");
        sendBroadcast(broadcastIntent);
        finishAfterTransition();
    }

    private void setAnimations() {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);
        getWindow().setTransitionBackgroundFadeDuration(700);
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());
    }

    @Override
    public void onSignInFragmentInteraction(Uri uri) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.project.quiz.LOGIN_SUCCESS");
        sendBroadcast(broadcastIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if( getFragmentManager().getBackStackEntryCount() >0){
            getFragmentManager().popBackStackImmediate();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void signUp() {
        loadFragment(CommonLibs.FragmentId.ID_FRAGMENT_SIGN_UP, null);
    }
}
