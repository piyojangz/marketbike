package com.example.marketbike.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Breeshy on 08/06/2014.
 */
public class Community extends Activity {
    private ArrayList<HashMap<String, String>> DataList;
    HashMap map;
    private ListAdapter listAdpt;
    private ListView lv;
    private CharSequence mTitle;
    private ArrayAdapter<String> listAdapter;
    protected ArrayList<HashMap<String, String>> sList;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_main);
        lv = (ListView) findViewById(R.id.menu_listView);
        this.sList = new ArrayList<HashMap<String, String>>();
        this.DataList = new ArrayList<HashMap<String, String>>();
        this.progress = new ProgressDialog(this);

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

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
                    loadItemList();
                    SystemClock.sleep(2000);
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
        task.execute((Void[]) null);

    }

    private void bindList() {
        this.listAdpt = new ListAdapter(this, this.sList);
        lv.setAdapter(this.listAdpt);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void loadItemList() {
        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_TITLE, "Ducati Desmosedici GP13");
        this.map.put(ListItem.KEY_DESC, "ข้อมูลจากสมาชิกเว็บไซท์ Diavel-Forum.com รายหนึ่งระบุว่า ตัวแทนจำหน่ายของ Ducati ได้บอกกับบรรดาลูกค้าว่าให้เตรียมพร้อมกับ Ducati Diavel รุ่นปรับปรุงใหม่ ซึ่งน่าจะเปิดตัวในวันจันทร์ที่ 3 มีนาคมที่จะถึงนี้");
        this.map.put(ListItem.KEY_IMAGE, "http://www.bigbikesthailand.com/wp-content/uploads/2014/04/Ducati-Desmosedici-GP133.png");

        this.sList.add(map);


        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_TITLE, "Ducati Multistrada 1200 S Granturismo");
        this.map.put(ListItem.KEY_DESC, "ข้อมูลจากสมาชิกเว็บไซท์ Diavel-Forum.com รายหนึ่งระบุว่า ตัวแทนจำหน่ายของ Ducati ได้บอกกับบรรดาลูกค้าว่าให้เตรียมพร้อมกับ Ducati Diavel รุ่นปรับปรุงใหม่ ซึ่งน่าจะเปิดตัวในวันจันทร์ที่ 3 มีนาคมที่จะถึงนี้");
        this.map.put(ListItem.KEY_IMAGE, "http://www.bigbikesthailand.com/wp-content/uploads/2014/03/Ducati-Multistrada-1200-S-Granturismo2.jpg");
        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_ID, "1");
        this.map.put(ListItem.KEY_TITLE, "Motus กำลังทำสอบค่าการปล่อยไอเสีย");
        this.map.put(ListItem.KEY_DESC, "ในเวอร์ชั่นของปี 2014 นั้นมาพร้อมกับขุมกำลังเครื่องยนต์ขนาด 1,000 ซีซี แบบ 90 degree V4 4 จังหวะ 4 วาล์วต่อสูบ เป็นแบบ desmodromic DOHC ระบายความร้อนด้วยของเหลว ให้กำลังสูงสุดถึง 235 แรงม้า และทำความเร็วได้สูงสุดถึง 250 ไมล์ต่อชั่วโมง หรือราวๆ 330 กิโลเมตรต่อชั่วโมงเลยทีเดียว");
        this.map.put(ListItem.KEY_IMAGE, "http://www.bigbikesthailand.com/wp-content/uploads/2014/05/Motus-MST.jpg");
        this.sList.add(map);


        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_ID, "1");
        this.map.put(ListItem.KEY_TITLE, "Ducati Desmosedici GP13");
        this.map.put(ListItem.KEY_DESC, "Ducati Desmosedici GP13 ในเวอร์ชั่นของปี 2014 นั้นมาพร้อมกับขุมกำลังเครื่องยนต์ขนาด 1,000 ซีซี แบบ 90 degree V4 4 จังหวะ 4 วาล์วต่อสูบ เป็นแบบ desmodromic DOHC ระบายความร้อนด้วยของเหลว ให้กำลังสูงสุดถึง 235 แรงม้า และทำความเร็วได้สูงสุดถึง 250 ไมล์ต่อชั่วโมง ");
        this.map.put(ListItem.KEY_IMAGE, "http://www.bigbikesthailand.com/wp-content/uploads/2014/04/Ducati-Desmosedici-GP133.png");
        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_ID, "1");
        this.map.put(ListItem.KEY_TITLE, "Ducati Desmosedici GP13");
        this.map.put(ListItem.KEY_DESC, "ในเวอร์ชั่นของปี 2014 นั้นมาพร้อมกับขุมกำลังเครื่องยนต์ขนาด 1,000 ซีซี แบบ 90 degree V4 4 จังหวะ 4 วาล์วต่อสูบ เป็นแบบ desmodromic DOHC ระบายความร้อนด้วยของเหลว");
        this.map.put(ListItem.KEY_IMAGE, "http://www.bigbikesthailand.com/wp-content/uploads/2014/03/2013-ducati-diavel-dark.jpg");
        this.sList.add(map);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //getActionBar().setTitle(mTitle);
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
}

