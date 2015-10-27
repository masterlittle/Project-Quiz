package com.project.quiz.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.project.quiz.R;
import com.project.quiz.interfaces.AuthenticateUserInterface;
import com.project.quiz.interfaces.CheckLoginInterface;
import com.project.quiz.utils.CommonLibs;

import java.util.List;

public class AuthActivity extends AppCompatActivity implements CheckLoginInterface {

    protected static final int LOGIN_REQUEST_CODE = 100;
    private Context context;
    protected boolean isAuthorized = false;
    protected boolean isLoggedIn = false;
    private ParseUser currentUser;
    private AuthenticateUserInterface listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        context = this;
        isLoggedIn(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auth, menu);
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
    public void isLoggedIn(String info) {
        currentUser = ParseUser.getCurrentUser();
    }

    protected void checkRole(final String roleType, Context childContext){
        listener = (AuthenticateUserInterface)childContext;
        if (currentUser != null) {
            isLoggedIn = true;
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_Role");
            query.whereEqualTo("users", currentUser.getObjectId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        if(objects.size() != 0) {
                            for (ParseObject obj : objects) {
                                Log.e("Roles", obj.toString());
                                Log.e("Roles name", obj.getString("name"));
                                if (!roleType.equalsIgnoreCase(obj.getString("name"))) {
                                    Toast.makeText(context, "You are not authorized", Toast.LENGTH_SHORT).show();
                                } else {
                                    isAuthorized = true;
                                    Toast.makeText(context, "You are authorized", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            Toast.makeText(context, "You are not authorized", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                        Log.e("Error", e.toString());
                    }
                    listener.authenticateUser(isAuthorized);
                }

            });

        }
        else{
            listener.authenticateUser(false);
        }
    }

    protected void startLogin(Context context){
        Intent intent = new Intent(context, ActivityLogon.class);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }
}
