package com.marketbike.app;


import android.app.ActionBar;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.Session;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.marketbike.app.helper.JsonHelper;

import org.json.JSONArray;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

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
        this.checkLogin();
        mCollectionPagerAdapter = new CollectionPagerAdapter(
                getSupportFragmentManager());
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
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
                            .setIcon(R.drawable.ic_news_feed)
                            .setTabListener(this));
                    break;
                case 1:
                    actionBar.addTab(actionBar.newTab()
                            .setIcon(R.drawable.ic_category)
                            .setTabListener(this));
                    break;
                case 2:
                    actionBar.addTab(actionBar.newTab()
                            .setIcon(R.drawable.ic_cart)
                            .setTabListener(this));
                    break;
                case 3:
                    actionBar.addTab(actionBar.newTab()
                            .setIcon(R.drawable.ic_action_person)
                            .setTabListener(this));
                    break;
                case 4:
                    actionBar.addTab(actionBar.newTab()
                            .setIcon(R.drawable.ic_setting_red)
                            .setTabListener(this));
                    break;
                default:
                    actionBar.addTab(actionBar.newTab()
                            .setIcon(R.drawable.ic_setting_red)
                            .setTabListener(this));
            }

        }

        getRegId();
// Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.marketbike.app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("KeyHash:", "error");

        } catch (NoSuchAlgorithmException e) {
            Log.i("KeyHash:", "error");
        }

        if (this.getIntent().hasExtra(ListItem.KEY_ID)) {
            this.title = this.getIntent().getCharSequenceExtra(ListItem.KEY_TITLE).toString();
            this.id = this.getIntent().getCharSequenceExtra(ListItem.KEY_ID).toString();
            this.type = this.getIntent().getCharSequenceExtra(ListItem.KEY_TYPE).toString();
            if (!this.id.equals("")) {
                this.openIntend();
            }
        }

    }

    private void openIntend() {
        Intent resultIntent;

        switch (Integer.parseInt(this.type)) {
            case 0:
                resultIntent = new Intent(this, News_detail.class);
                resultIntent.putExtra(ListItem.KEY_URL, "");
                resultIntent.putExtra(ListItem.KEY_TITLE, this.title);
                resultIntent.putExtra(ListItem.KEY_ID, String.valueOf(id));
                startActivity(resultIntent);
                break;
            case 1:
                resultIntent = new Intent(this, FriendRequest_list.class);
                resultIntent.putExtra(ListItem.KEY_URL, "");
                resultIntent.putExtra(ListItem.KEY_TITLE, this.title);
                resultIntent.putExtra(ListItem.KEY_ID, String.valueOf(id));
                startActivity(resultIntent);
                break;
            case 2: // accept friend
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(Integer.parseInt(id.toString()));
                break;
        }

    }

    private void checkLogin() {
        if (session == null) {
            if (session == null) {
                session = Session.openActiveSessionFromCache(this);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    public void getRegId() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM", msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i("GCM", "GCM = " + regid);
                register_device();
            }
        }.execute(null, null, null);
    }

    private void register_device() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    String url = "http://marketbike.zoaish.com/api/gcm_register/" + regid;
                    JsonHelper.getJson(url);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
            }
        };
        task.execute((Void[]) null);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        this.menu = menu;
        final View menu_hotlist = this.menu.findItem(R.id.action_noti).getActionView();
        this.ui_noti = (TextView) menu_hotlist.findViewById(R.id.noti_count);
        this.ui_noti.setVisibility(View.INVISIBLE);
        checkRequestFriend();
        session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            if (session.isOpened()) {
                this.menu.getItem(1).getSubMenu().getItem(0).setTitle("ออกจากระบบ");
                this.menu.getItem(1).getSubMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        session.close();
                        session.closeAndClearTokenInformation();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        return true;
                    }
                });
            }

        }

        new MyMenuItemStuffListener(menu_hotlist, "Show hot message") {
            @Override
            public void onClick(View v) {
                // String url = "http://marketbike.zoaish.com/api/set_notification_zero/" + fbid;
                Intent resultIntent = new Intent(getApplicationContext(), FriendRequest_list.class);
                resultIntent.putExtra(ListItem.KEY_URL, "");
                resultIntent.putExtra(ListItem.KEY_TITLE, "");
                resultIntent.putExtra(ListItem.KEY_ID, String.valueOf("0"));
                startActivity(resultIntent);
            }
        };


        return super.onCreateOptionsMenu(menu);
    }


    static abstract class MyMenuItemStuffListener implements View.OnClickListener {
        private String hint;
        private View view;

        MyMenuItemStuffListener(View view, String hint) {
            this.view = view;
            this.hint = hint;
            view.setOnClickListener(this);
        }

        @Override
        abstract public void onClick(View v);
    }


    public void checkRequestFriend() {


        task = new AsyncTask<Integer, Void, Integer>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Integer... arg0) {
                Integer notice = 0;
                try {
                    String url = "http://marketbike.zoaish.com/api/get_friend_request_notification/" + fbid;
                    Object data = JsonHelper.getJson(url).getString("result");
                    //Log.v("debug","data = " + data);
                    notice = Integer.parseInt(data.toString());

                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return notice;
            }

            @Override
            protected void onPostExecute(Integer result) {
                hot_number = result;
                updateNotiCount(hot_number);
            }

        };
        task.execute((Integer[]) null);


    }

    public void updateNotiCount(final int new_hot_number) {
        hot_number = new_hot_number;
        if (ui_noti == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_hot_number == 0)
                    ui_noti.setVisibility(View.INVISIBLE);
                else {
                    ui_noti.setVisibility(View.VISIBLE);
                    ui_noti.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        tab.setIcon(R.drawable.appicon);
        switch (tab.getPosition()) {
            case 0:
                tab.setIcon(R.drawable.ic_news_feed_hover);
                getActionBar().setTitle(mCollectionPagerAdapter.getPageTitle(0));

                break;
            case 1:
                tab.setIcon(R.drawable.ic_category_hover);
                getActionBar().setTitle(mCollectionPagerAdapter.getPageTitle(1));

                break;
            case 2:
                tab.setIcon(R.drawable.ic_cart_hover);
                getActionBar().setTitle(mCollectionPagerAdapter.getPageTitle(2));

                break;
            case 3:
                tab.setIcon(R.drawable.ic_action_person_hover);
                getActionBar().setTitle(mCollectionPagerAdapter.getPageTitle(3));
                break;
            case 4:
                tab.setIcon(R.drawable.ic_setting_red_hover);
                getActionBar().setTitle(mCollectionPagerAdapter.getPageTitle(4));

                break;
        }
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        tab.setIcon(R.drawable.appicon);
        switch (tab.getPosition()) {
            case 0:
                tab.setIcon(R.drawable.ic_news_feed);
                break;
            case 1:
                tab.setIcon(R.drawable.ic_category);
                break;
            case 2:
                tab.setIcon(R.drawable.ic_cart);
                break;
            case 3:
                tab.setIcon(R.drawable.ic_action_person);
                break;
            case 4:
                tab.setIcon(R.drawable.ic_setting_red);
                break;
            default:
                tab.setIcon(R.drawable.ic_setting_red);
                break;
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    public void onLoginClick(MenuItem item) {
        Intent newActivity = new Intent(this, LoginActivity.class);
        startActivity(newActivity);
    }


    public class CollectionPagerAdapter extends FragmentPagerAdapter {

        final int NUM_ITEMS = 5; // number of tabs

        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new Tab0();
                case 1:
                    return new Tab1();
                case 2:
                    return new Tab2();
                case 3:
                    return new Tab3();
                case 4:
                    return new Tab4();
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
                    tabLabel = "NEWS";
                    break;
                case 1:
                    tabLabel = "CATEGORY";
                    break;
                case 2:
                    tabLabel = "MARKET";
                    break;
                case 3:
                    tabLabel = "FRIEND";
                    break;
                case 4:
                    tabLabel = "SETTING";
                    break;

            }

            return tabLabel;

        }


    }


}


