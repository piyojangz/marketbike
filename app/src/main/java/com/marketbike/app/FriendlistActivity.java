package com.marketbike.app;


import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.Session;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;


public class FriendlistActivity extends FragmentActivity implements ActionBar.TabListener {

    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;
    private JSONArray data;
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "416625437190";
    Session session;
    private Menu menu;
    private String title, id, type;
    private int hot_number = 0;
    private TextView ui_noti = null;
    AsyncTask<Integer, Void, Integer> task;
    public static final String PREFS_NAME = "MyData_Settings";
    private String fbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Back");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mCollectionPagerAdapter = new CollectionPagerAdapter(
                getSupportFragmentManager());
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        this.getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

        });

        SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, this.MODE_PRIVATE);
        this.fbid = settings.getString("fbid", "");


        for (int i = 0; i < mCollectionPagerAdapter.getCount(); i++) {

            switch (i) {
                case 0:
                    actionBar.addTab(actionBar.newTab()
                            .setText(mCollectionPagerAdapter.getPageTitle(i).toString())
                            .setTabListener(this));
                    break;
                case 1:
                    actionBar.addTab(actionBar.newTab()
                            .setText(mCollectionPagerAdapter.getPageTitle(i).toString())
                            .setTabListener(this));
                    break;

                default:
                    actionBar.addTab(actionBar.newTab()
                            .setText(mCollectionPagerAdapter.getPageTitle(i).toString())
                            .setTabListener(this));
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.findfriends, menu);
        this.menu = menu;
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e74c3c")));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();


        switch (itemId) {
            case R.id.action_findfriend:
                startActivity(new Intent(FriendlistActivity.this, Tab3.class));
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }


    public class CollectionPagerAdapter extends FragmentPagerAdapter {

        final int NUM_ITEMS = 2; // number of tabs

        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new Friendlist();
                case 1:
                    return new Friendlist_Request();
            }

            return null;
        }


        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tabLabel = null;
            switch (position) {
                case 0:
                    tabLabel = "Contacts";
                    break;
                case 1:
                    tabLabel = "Biker request";
                    break;
            }

            return tabLabel;

        }


    }


}


