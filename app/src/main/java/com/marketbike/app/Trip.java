package com.marketbike.app;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.marketbike.app.ListAdapter;
import com.marketbike.app.ListItem;
import com.marketbike.app.News_detail;
import com.marketbike.app.R;
import com.marketbike.app.XListView.XListView;
import com.marketbike.app.XListView.XListView.IXListViewListener;
import com.marketbike.app.custom.setAppFont;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Breeshy on 08/06/2014.
 */
public class Trip extends Activity implements IXListViewListener {
    private ArrayList<HashMap<String, String>> DataList;
    HashMap map;
    private Trip_Community_Adapter listAdpt;
    private XListView lv;
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

        Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/HelveticaNeueLight.ttf");
        final ViewGroup mContainer = (ViewGroup) findViewById(
                android.R.id.content).getRootView();
        setAppFont.setAppFont(mContainer, typeFace);

        setTitle(this.getIntent().getCharSequenceExtra(ListItem.KEY_MENU_TITLE));
        this.lv = (XListView) findViewById(R.id.trip_listView);

        this.lv.setPullRefreshEnable(true);
        this.lv.setPullLoadEnable(true);

        this.sList = new ArrayList<HashMap<String, String>>();
        this.DataList = new ArrayList<HashMap<String, String>>();
        this.progress = new ProgressDialog(this);
        this.lv.setXListViewListener(this);
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

      /*  this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_TYPE, "CONTENT");
        this.map.put(ListItem.KEY_TITLE, "Ducati Multistrada 1200 S Granturismo");
        this.map.put(ListItem.KEY_DESC, "ข้อมูลจากสมาชิกเว็บไซท์ Diavel-Forum.com รายหนึ่งระบุว่า ตัวแทนจำหน่ายของ Ducati ได้บอกกับบรรดาลูกค้าว่าให้เตรียมพร้อมกับ Ducati Diavel ");
        this.map.put(ListItem.KEY_IMAGE, "http://www.bigbikesthailand.com/wp-content/uploads/2014/03/Ducati-Multistrada-1200-S-Granturismo2.jpg");
        this.map.put(ListItem.KEY_URL, "http://www.bigbikesthailand.com/wp-content/uploads/2014/05/Ducati-Multistrada-1200-S-Touring-D-Air.jpg");

        this.sList.add(map);*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conversation, menu);
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
        lv.stopRefresh();
        lv.stopLoadMore();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        lv.setRefreshTime(strDate);
    }

    @Override
    public void onRefresh() {
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

    @Override
    public void onLoadMore() {
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
}

