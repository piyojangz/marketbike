package com.marketbike.app;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.*;
import com.marketbike.app.custom.setAppFont;
import com.marketbike.app.helper.JsonHelper;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.marketbike.app.RefreshableListView.onListLoadMoreListener;
import com.marketbike.app.RefreshableListView.onListRefreshListener;


/**
 * Created by Breeshy on 08/06/2014.
 */
public class News extends Activity implements onListRefreshListener, onListLoadMoreListener {
    private ArrayList<HashMap<String, String>> DataList;
    private HashMap map;
    private ListAdapter listAdpt;
    private RefreshableListView lv;
    protected ArrayList<HashMap<String, String>> sList;
    private AsyncTask<Void, Void, Void> task;
    private Menu optionsMenu;
    private String cateid;
    private boolean isfirst = true;
    private static final int LIMIT = 20;
    private int OFFSET = 0;
    private boolean FLAG_END;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);

        Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Regular.ttf");
        final ViewGroup mContainer = (ViewGroup) findViewById(
                android.R.id.content).getRootView();
        setAppFont.setAppFont(mContainer, typeFace);

        setTitle(this.getIntent().getCharSequenceExtra(ListItem.KEY_MENU_TITLE));

        //RefreshableList lines start
        this.lv = (RefreshableListView) findViewById(R.id.menu_listView);
        this.lv.setOnListRefreshListener(this);//---------------------------------------------------------------Important
        this.lv.setOnListLoadMoreListener(this);
        this.lv.setDistanceFromBottom(5);
        //RefreshableList Lines end
        this.sList = new ArrayList<HashMap<String, String>>();
        this.DataList = new ArrayList<HashMap<String, String>>();
        this.cateid = this.getIntent().getCharSequenceExtra(ListItem.KEY_MENU_ID).toString();

    }

    private void bindList() {
        this.listAdpt = new ListAdapter(this, this.sList);
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_in);
        this.lv.startAnimation(animation);
        this.lv.setAdapter(this.listAdpt);

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

        this.lv.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // a.finish();

                String URL = sList.get(position).get(ListItem.KEY_URL).toString();
                String TITLE = sList.get(position).get(ListItem.KEY_TITLE).toString();
                String ID = sList.get(position).get(ListItem.KEY_MENU_ID).toString();
                Intent newActivity = new Intent(getBaseContext(), News_detail.class);
                newActivity.putExtra(ListItem.KEY_URL, URL);
                newActivity.putExtra(ListItem.KEY_TITLE, TITLE);
                newActivity.putExtra(ListItem.KEY_ID, ID);
                startActivity(newActivity);

                //  overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
            }
        });


/*
AdRequest request = new AdRequest.Builder()
    .setGender(AdRequest.GENDER_FEMALE)
    .setBirthday(new GregorianCalendar(1985, 1, 1).getTime())
    .setLocation(location)
    .build();
 */

      /*  AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadItemList(float size) {
        try {
            String url = "http://marketbike.zoaish.com/api/get_content_by_cate/" + cateid + "/" + OFFSET + "/" + LIMIT;
            Log.i("mylog", "url: " + url);
            JSONArray data = JsonHelper.getJson(url).getJSONArray("result");
            if (data.length() == 0) {
                FLAG_END = true;
            } else {
                FLAG_END = false;
            }
            for (int i = 0; i < data.length(); i++) {

                String id = data.getJSONObject(i).getString("ID");
                String title = data.getJSONObject(i).getString("Headline");
                String shortdesc = data.getJSONObject(i).getString("Short_Description");
                String createDate = data.getJSONObject(i).getString("Create_Date");
                String thumbnail = "http://marketbike.zoaish.com/public/uploads/" + data.getJSONObject(i).getString("Thumbnail_Image");
                String thumbnail_logo = "http://marketbike.zoaish.com/public/uploads/" + data.getJSONObject(i).getString("Thumbnail_Logo");

                if (isfirst) {
                    map = new HashMap<String, String>();
                    map.put(ListItem.KEY_MENU_ID, id);
                    map.put(ListItem.KEY_TYPE, "HILIGHT");
                    map.put(ListItem.KEY_TITLE, title);
                    map.put(ListItem.KEY_DESC, shortdesc);
                    map.put(ListItem.KEY_CREATEDATE, createDate);
                    map.put(ListItem.KEY_IMAGE, thumbnail);
                    map.put(ListItem.KEY_URL, thumbnail);
                    map.put(ListItem.KEY_IMAGE_LOGO, thumbnail_logo);


                    sList.add(map);
                } else {

                    map = new HashMap<String, String>();
                    map.put(ListItem.KEY_MENU_ID, id);
                    map.put(ListItem.KEY_TYPE, "CONTENT");
                    map.put(ListItem.KEY_TITLE, title);
                    map.put(ListItem.KEY_DESC, shortdesc);
                    map.put(ListItem.KEY_CREATEDATE, createDate);
                    map.put(ListItem.KEY_IMAGE, thumbnail);
                    map.put(ListItem.KEY_URL, thumbnail);
                    map.put(ListItem.KEY_IMAGE_LOGO, thumbnail_logo);

                    sList.add(map);
                }

                isfirst = false;
                OFFSET++;
            }


        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


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
                this.Refresh(this.lv);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                    onLoad();
                    listAdpt.notifyDataSetChanged();
                    lv.finishLoadingMore();//---------------------------------------------------------------------Important
                    super.onPostExecute(list);
                }
            }.execute(list);
        }
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

                Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_in);
                lv.startAnimation(animation);
                //I just finish both here to not have to write two example mocks
                onLoad();
                listAdpt.notifyDataSetChanged();
                lv.finishRefresh();//-------------------------------------------------------------------------Important

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

