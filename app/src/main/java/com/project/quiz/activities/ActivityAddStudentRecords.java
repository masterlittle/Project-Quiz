package com.project.quiz.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.project.quiz.R;
import com.project.quiz.adapters.TabPagerAdapter;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.customClasses.CustomDialogTextClass;
import com.project.quiz.customClasses.SlidingTabLayout;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.fragments.FragmentLoading;
import com.project.quiz.interfaces.AuthenticateUserInterface;
import com.project.quiz.interfaces.ChangeFragment;
import com.project.quiz.interfaces.DialogBoxListener;
import com.project.quiz.utils.CommonLibs;


import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityAddStudentRecords extends AuthActivity implements ChangeFragment, DialogBoxListener, AuthenticateUserInterface {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private String info;
    private Context context;
    PagerAdapter mAdapter;
    ViewPager viewPager;
    boolean isAuthorized;
    SlidingTabLayout slidingTabLayout;
    private Activity parentactivity;
    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.authenticateText)
    TextViewRegularFont authenticateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_records);
        ButterKnife.bind(this);
        context = this;
        checkRole(CommonLibs.Roles.ROLE_NORMAL, this);
        toolbar.setTitle("Student Section");
        setSupportActionBar(toolbar);
//        loadFragment(CommonLibs.FragmentId.ID_FRAGMENT_LOADING, null);

        //setting indicator and divider color

    }

    @Override
    public void authenticateUser(boolean isAuthorized) {
        if (isLoggedIn) {
            if (isAuthorized) {
                loadViewpager();
            } else {
                finish();
            }
        } else {
            startLogin(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                checkRole(CommonLibs.Roles.ROLE_NORMAL, this);
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_add_student_records, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadFragment(int id, Bundle bundle) {
        if (id == CommonLibs.FragmentId.ID_FRAGMENT_LOADING) {
            FragmentLoading fragmentLoading = new FragmentLoading();
            getFragmentManager().beginTransaction().replace(R.id.container, fragmentLoading, "FragmentLoading").commit();
        }
    }

    @Override
    public void showEditDialog(int position, String info) {
        this.info = info;
        CustomDialogTextClass dialog = new CustomDialogTextClass(this, "Remove Student?", "Are you sure you want to remove this student?");
        dialog.show();
    }

    @Override
    public void onDialogPositivePressed() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Students");
        query.whereEqualTo(StudentRecords.STUDENT_ID, info);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    if (object != null) {
                        deleteEvent(object);
                    }
                }

            }

        });
    }

    @Override
    public void onDialogCancelPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void loadViewpager() {
        progress.setVisibility(View.GONE);
        authenticateText.setVisibility(View.GONE);

        String[] tabs = new String[]{"Add student", "Edit students"};
        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabs, tabs.length);

        viewPager.setAdapter(mAdapter);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.orange_e96125);    //define any color in xml resources and set it here, I have used white
            }

            @Override
            public int getDividerColor(int position) {
                return getResources().getColor(R.color.orange_e96125);
            }
        });

        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                final InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void deleteEvent(ParseObject object) {
        object.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    getContentResolver().delete(DataContentProvider.CONTENT_STORE_STUDENTS_URI, StudentRecords.STUDENT_ID + "=?", new String[]{info});
                }
            }
        });

    }
}