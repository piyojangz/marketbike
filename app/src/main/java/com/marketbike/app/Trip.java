package com.marketbike.app;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.marketbike.app.RefreshableListView.onListLoadMoreListener;
import com.marketbike.app.RefreshableListView.onListRefreshListener;


/**
 * Created by Breeshy on 08/06/2014.
 */
public class Trip extends Activity implements onListRefreshListener, onListLoadMoreListener {
    private ArrayList<HashMap<String, String>> DataList;
    HashMap map;
    private Trip_Community_Adapter listAdpt;
    private RefreshableListView lv;
    private CharSequence mTitle;
    private ArrayAdapter<String> listAdapter;
    protected ArrayList<HashMap<String, String>> sList;
    private ProgressDialog progress;
    private Handler mHandler;
    AsyncTask<Void, Void, Void> task;
    private int start = 0;
    private static int refreshCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip);

        // Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/HelveticaNeueLight.ttf");
        final ViewGroup mContainer = (ViewGroup) findViewById(
                android.R.id.content).getRootView();
        // setAppFont.setAppFont(mContainer, typeFace);

        setTitle(this.getIntent().getCharSequenceExtra(ListItem.KEY_MENU_TITLE));
        this.lv = (RefreshableListView) findViewById(R.id.trip_listView);

        this.lv.setOnListRefreshListener(this);//---------------------------------------------------------------Important
        this.lv.setOnListLoadMoreListener(this);
        this.lv.setDistanceFromBottom(2);

        this.sList = new ArrayList<HashMap<String, String>>();
        this.DataList = new ArrayList<HashMap<String, String>>();
        this.progress = new ProgressDialog(this);
        this.mHandler = new Handler();
        this.task = new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setMessage("Downloading... :) ");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    //Do something...
                    loadItemList(0);
                    //SystemClock.sleep(2000);
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                progress.dismiss();
                bindList();
            }

        };
        this.task.execute((Void[]) null);

    }

    private void bindList() {
        this.listAdpt = new Trip_Community_Adapter(this, this.sList);
        this.lv.setAdapter(this.listAdpt);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadItemList(float size) {
        start += size;
        if (size == 0) {
            this.map = new HashMap<String, String>();
            this.map.put(ListItem.KEY_MENU_ID, "1");
            this.map.put(ListItem.KEY_TYPE, "HEADER");
            this.map.put(ListItem.KEY_TITLE, "Z800 Club Thailand");
            this.map.put(ListItem.KEY_IMAGE_LOGO, "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/t1.0-1/c95.3.395.395/s160x160/1234747_194747977371010_1425671170_n.jpg");
            this.map.put(ListItem.KEY_IMAGE_HEADER, "http://www.gt-rider.com/wp-content/uploads/2006/08/motorcycle-touring-thailand-900a1.jpg");
            this.sList.add(map);
        }

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "2");
        this.map.put(ListItem.KEY_TYPE, "CONTENT");
        this.map.put(ListItem.KEY_TITLE, "CHUMPON SAMA");
        this.map.put(ListItem.KEY_DESC, "DUCATI PANIGALE 1990");
        this.map.put(ListItem.KEY_IMAGE, "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xfp1/t1.0-1/p320x320/10255282_746118662075641_5186835560918721027_n.jpg");
        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "3");
        this.map.put(ListItem.KEY_TYPE, "CONTENT");
        this.map.put(ListItem.KEY_TITLE, "BREESHY SAMA");
        this.map.put(ListItem.KEY_DESC, "DUCATI MONSTER 795");
        this.map.put(ListItem.KEY_IMAGE, "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xfp1/t1.0-1/p320x320/10306388_10202092533301118_8683534609256773072_n.jpg");

        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "4");
        this.map.put(ListItem.KEY_TYPE, "CONTENT");
        this.map.put(ListItem.KEY_TITLE, "Soophuksorn Somphanit");
        this.map.put(ListItem.KEY_DESC, "HONDA CBR 1000R");
        this.map.put(ListItem.KEY_IMAGE, "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpa1/v/t1.0-1/p320x320/10325795_10201375990835562_8132249767906859215_n.jpg?oh=ccdb0f93df03377129139bc23e27d266&oe=543A6559&__gda__=1412884424_bd3b92bd11af0c2780d30659626af582");


        this.sList.add(map);

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

    private void onLoad() {
        Log.i("mylog", "onLoad: ");
        lv.finishRefresh();//-------------------------------------------------------------------------Important
        lv.finishLoadingMore();//---------------------------------------------------------------------Important
    }


    @Override
    public void LoadMore(RefreshableListView list) {
        Log.i("mylog", "onLoadMore: " + start);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                loadItemList(sList.size());
                listAdpt.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void Refresh(RefreshableListView list) {
        Log.i("mylog", "onRefresh: ");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sList.clear();
                loadItemList(0);
                onLoad();
            }
        }, 2000);
    }
}

