package com.marketbike.app;


import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.Session;
import com.facebook.SessionState;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.marketbike.app.helper.JsonHelper;
import java.io.IOException;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "416625437190";
    Session session;
    private  Menu menu;
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

        for (int i = 0; i < mCollectionPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab()
                    .setText(mCollectionPagerAdapter.getPageTitle(i))
                    .setTabListener(this));

        }

        getRegId();
// Add code to print out the key hash
       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.marketbike.app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("KeyHash:","error");

        } catch (NoSuchAlgorithmException e) {
            Log.i("KeyHash:","error");
        }*/

    }

    private  void checkLogin(){
        if (session == null) {
            if(session==null){
                session = Session.openActiveSessionFromCache(this);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    public void getRegId(){
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
                Log.i("GCM","GCM = " + regid);
                register_device();
            }
        }.execute(null, null, null);
    }

    private void register_device(){
        AsyncTask<Void, Void, Void> task  = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    String url = "http://marketbike.zoaish.com/api/gcm_register/"+regid;
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
        session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            if (session.isOpened()) {
                this.menu.getItem(0).getSubMenu().getItem(0).setTitle("ออกจากระบบ");
                this.menu.getItem(0).getSubMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
                    public boolean onMenuItemClick(MenuItem item){
                        session.close();
                        session.closeAndClearTokenInformation();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        return true;
                    }});
            }

        }
        return super.onCreateOptionsMenu(menu);
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

    public void onLoginClick(MenuItem item) {
        Intent newActivity = new Intent(this,LoginActivity.class);
        startActivity(newActivity);
    }


    public class CollectionPagerAdapter extends FragmentPagerAdapter {

        final int NUM_ITEMS = 3; // number of tabs

        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new Tab1();
                case 1:
                    return new Tab2();
                case 2:
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
                    tabLabel = "MARKET";
                    break;
                case 2:
                    tabLabel = "SETTING";
                    break;

            }

            return tabLabel;

        }



    }


}


