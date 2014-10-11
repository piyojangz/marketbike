package com.marketbike.app;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.marketbike.app.adapter.UserReqAdapter;
import com.marketbike.app.custom.ListItem;
import com.marketbike.app.helper.JsonHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class Friendlist_Request extends Fragment {

    /**
     * Called when the activity is first created.
     */
    private ArrayList<HashMap<String, String>> DataList;
    private HashMap map;
    private UserReqAdapter userAdpt;
    private ListView lv;
    protected ArrayList<HashMap<String, String>> sList;
    AsyncTask<Void, Void, Void> task;
    private JSONArray data;
    public static final String PREFS_NAME = "MyData_Settings";
    private String userid;
    private static final int LIMIT = 100;
    private int OFFSET = 0;
    private TextView notification_label;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.tab3, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        this.userid = settings.getString("fbid", "");
        this.notification_label = (TextView) rootView.findViewById(R.id.notification_label);
        this.lv = (ListView) rootView.findViewById(R.id.tab3_listView);
        this.sList = new ArrayList<HashMap<String, String>>();

        this.DataList = new ArrayList<HashMap<String, String>>();
        this.createList();


        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("mylog", "onItemClick:");
                String ID = sList.get(position).get(ListItem.KEY_MENU_ID).toString();
                String TITLE = sList.get(position).get(ListItem.KEY_MENU_TITLE).toString();
                Intent newActivity = new Intent(container.getContext(), Trip.class);
                newActivity.putExtra(ListItem.KEY_MENU_ID, ID);
                newActivity.putExtra(ListItem.KEY_MENU_TITLE, TITLE);
                startActivity(newActivity);
            }
        });

        return rootView;
    }

    private void bindList() {
        if (this.sList.size() > 0) {
            this.lv.setVisibility(View.VISIBLE);
            this.notification_label.setVisibility(View.GONE);
            this.userAdpt = new UserReqAdapter(this.getActivity(), this.sList);
            this.lv.setAdapter(this.userAdpt);
        } else {
            this.lv.setVisibility(View.GONE);
            this.notification_label.setVisibility(View.VISIBLE);
            this.notification_label.setText("No biker reuqest");
        }
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

                    String url = "http://marketbike.zoaish.com/api/get_user_request/" + userid + "/" + OFFSET + "/" + LIMIT;

                    data = JsonHelper.getJson(url).getJSONArray("result");

                    for (int i = 0; i < data.length(); i++) {

                        String id = data.getJSONObject(i).getString("userid");
                        String name = data.getJSONObject(i).getString("name");
                        String fbid = data.getJSONObject(i).getString("fbid");
                        String email = data.getJSONObject(i).getString("email");
                        String status = data.getJSONObject(i).getString("status");

                        String create_date = data.getJSONObject(i).getString("create_date");
                        String request_date = data.getJSONObject(i).getString("request_date");

                        String thumbnail = "https://graph.facebook.com/" + fbid + "/picture?type=large";

                        map = new HashMap<String, String>();
                        map.put(ListItem.KEY_ID, id);
                        map.put(ListItem.KEY_NAME, name);
                        map.put(ListItem.KEY_FBID, fbid);
                        map.put(ListItem.KEY_EMAIL, email);
                        map.put(ListItem.KEY_CREATEDATE, create_date);
                        map.put(ListItem.KEY_REQUESTDATE, request_date);
                        map.put(ListItem.KEY_MENU_LOGO, thumbnail);
                        map.put(ListItem.KEY_STATUS, status);
                        sList.add(map);
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
}