package com.marketbike.app;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.facebook.Session;
import com.marketbike.app.adapter.UserAdapter;
import com.marketbike.app.custom.ListItem;
import com.marketbike.app.helper.JsonHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class Tab3 extends FragmentActivity {

    /**
     * Called when the activity is first created.
     */
    private ArrayList<HashMap<String, String>> DataList;
    private HashMap map;
    private UserAdapter userAdpt;
    private ListView lv;
    AsyncTask<Void, Void, Void> task;
    private JSONArray data;
    private static final int LIMIT = 100;
    private int OFFSET = 0;
    protected ArrayList<HashMap<String, String>> sList;
    public static final String PREFS_NAME = "MyData_Settings";
    private String userid;
    Session session;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3);
        setTitle("Suggestions");

        SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, this.MODE_PRIVATE);
        this.userid = settings.getString("fbid", "");


        this.lv = (ListView) this.findViewById(R.id.tab3_listView);
        this.sList = new ArrayList<HashMap<String, String>>();

        this.DataList = new ArrayList<HashMap<String, String>>();


        this.createList();
    }

    private void bindList() {
        this.userAdpt = new UserAdapter(this, this.sList);
        this.lv.setAdapter(this.userAdpt);
    }

    private boolean checkLogin() {
        if (session == null) {
            if (session == null) {
                session = Session.openActiveSessionFromCache(this);
                if (session == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private void createList() {


        this.task = new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    String url = "http://marketbike.zoaish.com/api/get_all_user/" + userid + "/" + OFFSET + "/" + LIMIT;

                    data = JsonHelper.getJson(url).getJSONArray("result");

                    for (int i = 0; i < data.length(); i++) {

                        String id = data.getJSONObject(i).getString("userid");
                        String name = data.getJSONObject(i).getString("name");
                        String fbid = data.getJSONObject(i).getString("fbid");
                        String email = data.getJSONObject(i).getString("email");
                        String status = data.getJSONObject(i).getString("friendstatus");

                        String create_date = data.getJSONObject(i).getString("create_date");
                        String thumbnail = "https://graph.facebook.com/" + fbid + "/picture?type=large";

                        map = new HashMap<String, String>();
                        map.put(ListItem.KEY_ID, id);
                        map.put(ListItem.KEY_NAME, name);
                        map.put(ListItem.KEY_FBID, fbid);
                        map.put(ListItem.KEY_EMAIL, email);
                        map.put(ListItem.KEY_CREATEDATE, create_date);
                        map.put(ListItem.KEY_MENU_LOGO, thumbnail);
                        map.put(ListItem.KEY_STATUS, status);
                        if (!status.equals("2")) {
                            sList.add(map);
                        }
                    }

                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                bindList();
            }

        };
        this.task.execute((Void[]) null);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.close, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}