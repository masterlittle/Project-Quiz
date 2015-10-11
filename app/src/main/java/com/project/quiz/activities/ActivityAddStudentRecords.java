package com.project.quiz.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.project.quiz.R;
import com.project.quiz.adapters.TabPagerAdapter;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.customClasses.CustomDialogClass;
import com.project.quiz.customClasses.CustomDialogTextClass;
import com.project.quiz.customClasses.SlidingTabLayout;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.interfaces.ChangeFragment;
import com.project.quiz.interfaces.DialogBoxListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityAddStudentRecords extends AppCompatActivity implements ChangeFragment, DialogBoxListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private String info;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_records);
        ButterKnife.bind(this);
        context = this;

        toolbar.setTitle("Student Section");
        setSupportActionBar(toolbar);
        String[] tabs = new String[]{"Add student", "Edit students"};

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter mAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabs, tabs.length);

        viewPager.setAdapter(mAdapter);
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                final InputMethodManager imm = (InputMethodManager)getSystemService(
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

        //setting indicator and divider color
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

    }

    @Override
    public void showEditDialog(int position, String info) {
        this.info= info;
        CustomDialogTextClass dialog = new CustomDialogTextClass(this, "Remove Student?", "Are you sure you want to remove this student?");
        dialog.show();
    }

    @Override
    public void onDialogPositivePressed() {
        getContentResolver().delete(DataContentProvider.CONTENT_STORE_STUDENTS_URI, StudentRecords.STUDENT_ID + "=?", new String[]{info});
    }

    @Override
    public void onDialogCancelPressed() {

    }
}
