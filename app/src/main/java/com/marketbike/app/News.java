package com.marketbike.app;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.marketbike.app.XListView.XListView;
import com.marketbike.app.XListView.XListView.IXListViewListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Breeshy on 08/06/2014.
 */
public class News extends Activity implements IXListViewListener {
    private ArrayList<HashMap<String, String>> DataList;
    HashMap map;
    private ListAdapter listAdpt;
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
        setContentView(R.layout.news);
        setTitle(this.getIntent().getCharSequenceExtra(ListItem.KEY_MENU_TITLE));
        this.lv = (XListView) findViewById(R.id.menu_listView);

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



        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // a.finish();

                String URL = sList.get(position -1).get(ListItem.KEY_URL).toString();
                String TITLE = sList.get(position -1).get(ListItem.KEY_TITLE).toString();
                Intent newActivity = new Intent(getBaseContext(), News_detail.class);
                newActivity.putExtra(ListItem.KEY_URL, URL);
                newActivity.putExtra(ListItem.KEY_TITLE, TITLE);
                startActivity(newActivity);

                //  overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
            }
        });


    }

    private void bindList() {
        this.listAdpt = new ListAdapter(this, this.sList);
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
            this.map.put(ListItem.KEY_TYPE, "HILIGHT");
            this.map.put(ListItem.KEY_TITLE, "Ducati Desmosedici GP13");
            this.map.put(ListItem.KEY_DESC, "ข้อมูลจากสมาชิกเว็บไซท์ Diavel-Forum.com รายหนึ่งระบุว่า ตัวแทนจำหน่ายของ Ducati ได้บอกกับบรรดาลูกค้าว่าให้เตรียมพร้อมกับ Ducati Diavelี้");
            this.map.put(ListItem.KEY_IMAGE, "http://www.bigbikesthailand.com/wp-content/uploads/2014/05/Ducati-Multistrada-1200-S-Touring-D-Air.jpg");
            this.map.put(ListItem.KEY_URL, "http://www.bigbikesthailand.com/wp-content/uploads/2014/05/Ducati-Multistrada-1200-S-Touring-D-Air.jpg");


            this.sList.add(map);
        }

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_TYPE, "CONTENT");
        this.map.put(ListItem.KEY_TITLE, "Ducati Multistrada 1200 S Granturismo");
        this.map.put(ListItem.KEY_DESC, "ข้อมูลจากสมาชิกเว็บไซท์ Diavel-Forum.com รายหนึ่งระบุว่า ตัวแทนจำหน่ายของ Ducati ได้บอกกับบรรดาลูกค้าว่าให้เตรียมพร้อมกับ Ducati Diavel ");
        this.map.put(ListItem.KEY_IMAGE, "http://www.bigbikesthailand.com/wp-content/uploads/2014/03/Ducati-Multistrada-1200-S-Granturismo2.jpg");
        this.map.put(ListItem.KEY_URL, "http://www.bigbikesthailand.com/wp-content/uploads/2014/05/Ducati-Multistrada-1200-S-Touring-D-Air.jpg");

        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_ID, "1");
        this.map.put(ListItem.KEY_TYPE, "CONTENT");
        this.map.put(ListItem.KEY_TITLE, "Motus กำลังทำสอบค่าการปล่อยไอเสีย");
        this.map.put(ListItem.KEY_DESC, "ในเวอร์ชั่นของปี 2014 นั้นมาพร้อมกับขุมกำลังเครื่องยนต์ขนาด 1,000 ซีซี แบบ 90 degree V4 4 จังหวะ 4 วาล์วต่อสูบ เป็นแบบ desmodromic DOHC");
        this.map.put(ListItem.KEY_IMAGE, "http://www.bigbikesthailand.com/wp-content/uploads/2014/05/Motus-MST.jpg");
        this.map.put(ListItem.KEY_URL, "http://www.bigbikesthailand.com/wp-content/uploads/2014/05/Ducati-Multistrada-1200-S-Touring-D-Air.jpg");

        this.sList.add(map);


        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_ID, "1");
        this.map.put(ListItem.KEY_TYPE, "CONTENT");
        this.map.put(ListItem.KEY_TITLE, "Ducati Desmosedici GP13");
        this.map.put(ListItem.KEY_DESC, "Ducati Desmosedici GP13 ในเวอร์ชั่นของปี 2014 นั้นมาพร้อมกับขุมกำลังเครื่องยนต์ขนาด 1,000 ซีซี แบบ 90 degree V4 4 จังหวะ 4 วาล์วต่อสูบ เป็นแบบ desmodromic DOHC  ");
        this.map.put(ListItem.KEY_IMAGE, "http://www.bigbikesthailand.com/wp-content/uploads/2014/04/Ducati-Desmosedici-GP133.png");
        this.map.put(ListItem.KEY_URL, "http://www.bigbikesthailand.com/wp-content/uploads/2014/05/Ducati-Multistrada-1200-S-Touring-D-Air.jpg");

        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_ID, "1");
        this.map.put(ListItem.KEY_TYPE, "CONTENT");
        this.map.put(ListItem.KEY_TITLE, "Ducati Desmosedici GP13");
        this.map.put(ListItem.KEY_DESC, "ในเวอร์ชั่นของปี 2014 นั้นมาพร้อมกับขุมกำลังเครื่องยนต์ขนาด 1,000 ซีซี แบบ 90 degree V4 4 จังหวะ 4 วาล์วต่อสูบ เป็นแบบ desmodromic DOHC ระบายความร้อนด้วยของเหลว");
        this.map.put(ListItem.KEY_IMAGE, "http://www.bigbikesthailand.com/wp-content/uploads/2014/03/2013-ducati-diavel-dark.jpg");
        this.map.put(ListItem.KEY_URL, "http://www.bigbikesthailand.com/wp-content/uploads/2014/05/Ducati-Multistrada-1200-S-Touring-D-Air.jpg");

        this.sList.add(map);

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

