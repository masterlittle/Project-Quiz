package com.project.quiz.activities;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.project.quiz.R;
import com.project.quiz.adapters.EventsTabPagerAdapter;
import com.project.quiz.customclasses.SyncData;
import com.project.quiz.customviews.SlidingTabLayout;
import com.project.quiz.interfaces.AuthenticateUserInterface;
import com.project.quiz.interfaces.ChangeFragment;
import com.project.quiz.utils.CommonLibs;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityEvents extends AuthActivity implements ChangeFragment, Animation.AnimationListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ButterKnife.bind(this);

        toolbar.setTitle("Events");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] tabs = new String[]{"Your Events\n","Add Events\n", "All Events\n"};

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter mAdapter = new EventsTabPagerAdapter(getSupportFragmentManager(), tabs, tabs.length);

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
        getMenuInflater().inflate(R.menu.menu_activity_events, menu);
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
        if(id == R.id.sync){
            syncAnimation(item);
            new SyncData().sync(getApplicationContext(), this);
        }

        return super.onOptionsItemSelected(item);
    }

    private void syncAnimation(final MenuItem item) {
        this.item = item;
        LayoutInflater inflater = (LayoutInflater) getApplication()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.image_sync_animation,
                null);
        item.setActionView(iv);
        final Animation rotation = AnimationUtils.loadAnimation(getApplication(),
                R.anim.rotate_icon);
        iv.startAnimation(rotation);
        rotation.setAnimationListener(this);
    }
    @Override
    public void loadFragment(int id, Bundle bundle) {

    }

    @Override
    public void onAnimationStart(Animation animation) {
        item.setActionView(null);
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
