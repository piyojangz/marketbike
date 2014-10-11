package com.marketbike.app;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Session;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.marketbike.app.adapter.SettingAdapter;
import com.marketbike.app.custom.ListItem;
import com.marketbike.app.helper.JsonHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SettingActivity extends Activity {
    private static String[] menuItems;
    private com.marketbike.app.adapter.SettingAdapter SettingAdapter;
    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> sList;
    ViewPager mViewPager;
    private JSONArray data;
    GoogleCloudMessaging gcm;
    private boolean IS_LOGIN;
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
    private ListView list_menu;
    private Activity _activity;
    private String _is_notification;
    private String is_notification;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Back");
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this._activity = this;
        this.btn_logout = (Button) this.findViewById(R.id.btn_logout);
        list_menu = (ListView) this.findViewById(R.id.list_menu);
        session = Session.getActiveSession();
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    loadSetting();
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                if (sList != null) {
                    SettingAdapter = new SettingAdapter(_activity, sList);
                    list_menu.setAdapter(SettingAdapter);
                }
            }

        };
        task.execute((Void[]) null);


        this.btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session != null) {
                    session.close();
                    session.closeAndClearTokenInformation();
                    Intent resultIntent = new Intent(_activity, LoginActivity.class);
                    startActivity(resultIntent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e74c3c")));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    private void loadSetting() {

        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(this.getApplicationContext());
            }
            regid = gcm.register(PROJECT_NUMBER);
            String url = "http://marketbike.zoaish.com/api/get_notification_setting/" + regid;
            JSONObject jsondata = JsonHelper.getJson(url).getJSONObject("result");
            is_notification = jsondata.getString("is_notification").trim();

            //list_menu
            String[] menu = {"Notification"};
            String[] img = {"ic_action_alarms"};
            String[] psetting = {is_notification};

            sList = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < menu.length; i++) {
                //Log.v("fb", "is_notification: " + psetting[i]);

                map = new HashMap<String, String>();
                map.put(ListItem.KEY_TITLE, menu[i]);
                map.put(ListItem.KEY_IMAGE, img[i]);
                map.put(ListItem.KEY_SET, psetting[i]);
                sList.add(map);
            }

        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}


