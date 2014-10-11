package com.marketbike.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.facebook.Session;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.marketbike.app.RefreshableListView.onListLoadMoreListener;
import com.marketbike.app.RefreshableListView.onListRefreshListener;
import com.marketbike.app.adapter.UserPrifileAdapter;
import com.marketbike.app.custom.ListItem;
import com.marketbike.app.helper.JsonHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class User_profile_Activity extends Activity implements onListRefreshListener, onListLoadMoreListener {

    /**
     * Called when the activity is first created.
     */
    private Menu optionsMenu;
    AsyncTask<Void, Void, Void> task;
    private boolean IS_LOGIN;
    private String get_id, get_name, get_gender, get_email, get_birthday;
    private String imageURL;
    private Session session;
    private boolean FLAG_END;
    private UserPrifileAdapter listAdpt;
    protected ArrayList<HashMap<String, String>> sList;
    private View rootView;
    GoogleCloudMessaging gcm;
    private HashMap map;
    private static final int LIMIT = 10;
    private int OFFSET = 0;
    public static final String PREFS_NAME = "MyData_Settings";
    String regid;
    String PROJECT_NUMBER = "416625437190";

    private String userid;
    private RefreshableListView lv;
    private boolean isfirst = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab4);
        this.lv = (RefreshableListView) this.findViewById(R.id.userprofile_listView);
        this.lv.setOnListRefreshListener(this);//---------------------------------------------------------------Important
        this.lv.setOnListLoadMoreListener(this);
        this.lv.setDistanceFromBottom(2);
        this.lv.getListView().setFooterDividersEnabled(true);
        this.lv.getListView().setDivider(null);
        this.lv.getListView().setDividerHeight(0);
        this.lv.getListView().setClipToPadding(false);
        this.lv.getListView().setSelector(new ColorDrawable(Color.TRANSPARENT));
        this.sList = new ArrayList<HashMap<String, String>>();

        this.userid = this.getIntent().getCharSequenceExtra("fbid").toString();

    }

    private void firstload() {
        this.task = new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setRefreshActionButtonState(true);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    OFFSET = 0;
                    loadItemList(0);
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                bindList();
                setRefreshActionButtonState(false);
            }

        };
        this.task.execute((Void[]) null);
    }


    private void loadItemList(float size) {


        try {
            String url = "http://marketbike.zoaish.com/api/get_all_feed_by_user/" + this.userid + "/" + OFFSET + "/" + LIMIT;
            JSONArray data = JsonHelper.getJson(url).getJSONArray("result");

            if (data.length() == 0) {
                FLAG_END = true;
            } else {
                FLAG_END = false;
            }

            if (isfirst) {
                map = new HashMap<String, String>();
                map.put(ListItem.KEY_MENU_ID, "001");
                map.put(ListItem.KEY_TITLE, "header");
                map.put(ListItem.KEY_CREATEDATE, "");
                map.put(ListItem.KEY_FIRSTNAME, "");
                map.put(ListItem.KEY_LASTNME, "");
                map.put(ListItem.KEY_IMAGE, "https://graph.facebook.com/" + userid + "/picture?type=large");
                map.put(ListItem.KEY_URL, "");
                map.put(ListItem.KEY_FBID, "");

                sList.add(map);
                OFFSET++;
                isfirst = false;
            }

            for (int i = 0; i < data.length(); i++) {

                String id = data.getJSONObject(i).getString("ID");
                String title = data.getJSONObject(i).getString("Short_Description");
                String create_date = data.getJSONObject(i).getString("Create_Date");
                String fname = data.getJSONObject(i).getString("firstname");
                String lname = data.getJSONObject(i).getString("lastname");
                String thumbnail = data.getJSONObject(i).getString("Thumbnail_Image");
                String fbid = data.getJSONObject(i).getString("fbid");

                map = new HashMap<String, String>();
                map.put(ListItem.KEY_MENU_ID, id);
                map.put(ListItem.KEY_TITLE, title);
                map.put(ListItem.KEY_CREATEDATE, create_date);
                map.put(ListItem.KEY_FIRSTNAME, fname);
                map.put(ListItem.KEY_LASTNME, lname);
                map.put(ListItem.KEY_IMAGE, thumbnail);
                map.put(ListItem.KEY_URL, thumbnail);
                map.put(ListItem.KEY_FBID, fbid);

                sList.add(map);
                OFFSET++;
            }


        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private void bindList() {
        Log.d("debug", "this.sList= " + this.sList);
        this.listAdpt = new UserPrifileAdapter(this, this.sList);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in);
        this.lv.startAnimation(animation);
        this.lv.setAdapter(this.listAdpt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news, menu);
        this.firstload();
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
            case R.id.action_refresh:
                this.firstload();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void LoadMore(RefreshableListView list) {
        if (FLAG_END != true) {
            this.setRefreshActionButtonState(true);
            ////This just asyncly waits 3 seconds then does the finishRefresh()
            new AsyncTask<RefreshableListView, Object, RefreshableListView>() {
                protected RefreshableListView doInBackground(RefreshableListView... params) {
                    try {
                        loadItemList(0);
                    } catch (Exception e) {
                    }
                    return params[0];

                }

                @Override
                protected void onPostExecute(RefreshableListView list) {
                    //I just finish both here to not have to write two example mocks
                    lv.finishLoadingMore();//---------------------------------------------------------------------Important
                    onLoad();
                    listAdpt.notifyDataSetChanged();
                    super.onPostExecute(list);
                }
            }.execute(list);
        }
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu.findItem(R.id.action_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }


    private void onLoad() {


        this.setRefreshActionButtonState(false);

    }

    @Override
    public void Refresh(RefreshableListView list) {
        this.setRefreshActionButtonState(true);
        OFFSET = 0;
        isfirst = true;
        FLAG_END = false;
        sList.clear();

        lv.getListView().setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        new AsyncTask<RefreshableListView, Object, RefreshableListView>() {
            protected RefreshableListView doInBackground(RefreshableListView... params) {
                try {
                    loadItemList(0);
                } catch (Exception e) {
                }
                return params[0];

            }

            @Override
            protected void onPostExecute(RefreshableListView list) {
                lv.finishRefresh();//-------------------------------------------------------------------------Important
                Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_in);
                lv.startAnimation(animation);
                onLoad();
                listAdpt.notifyDataSetChanged();
                lv.getListView().setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });

                super.onPostExecute(list);
            }
        }.execute(list);


    }
}