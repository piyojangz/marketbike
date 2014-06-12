package com.example.marketbike.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
    private listAdapter listAdpt;
    private ListView lv;
    private ArrayAdapter<String> listAdapter;
    protected ArrayList<HashMap<String, String>> sList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.community_main);
        lv = (ListView) findViewById(R.id.menu_listView);
        this.sList = new ArrayList<HashMap<String, String>>();
        this.DataList = new ArrayList<HashMap<String, String>>();
        this.loadItemList();
        this.listAdpt = new listAdapter(this, this.sList);
        lv.setAdapter(this.listAdpt);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void loadItemList() {
        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Ducati Multistrada 1200 S Granturismo");
        this.map.put(ListItem.KEY_MENU_LOGO, "http://www.bigbikesthailand.com/wp-content/uploads/2014/04/Ducati-Desmosedici-GP133.png");

        this.sList.add(map);


        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Ducati Multistrada 1200 S Granturismo");
        this.map.put(ListItem.KEY_MENU_LOGO, "http://www.bigbikesthailand.com/wp-content/uploads/2014/03/Ducati-Multistrada-1200-S-Granturismo2.jpg");
        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Motus กำลังทำสอบค่าการปล่อยไอเสีย");
        this.map.put(ListItem.KEY_MENU_LOGO, "http://www.bigbikesthailand.com/wp-content/uploads/2014/05/Motus-MST.jpg");
        this.sList.add(map);


        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Suzuki");
        this.map.put(ListItem.KEY_MENU_LOGO, "http://www.mytyres.co.uk/simg/Logos/Suzuki.png");
        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "BMW");
        this.map.put(ListItem.KEY_MENU_LOGO, "http://freetexturesblueprints.com/albums/userpics/10001/thumb_bmw-logo.jpg");
        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Yamaha");
        this.map.put(ListItem.KEY_MENU_LOGO, "http://fsa.zedge.net/content/7/7/7/0/1-289821-7770-t.jpg");
        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Harley Davidson");
        this.map.put(ListItem.KEY_MENU_LOGO, "http://vectorlogo.biz/wp-content/uploads/2012/11/HARLEY-DAVIDSON-CYCLES-VECTORLOGO-DOT-BIZ-128x128.png");
        this.sList.add(map);
        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Triumph");
        this.map.put(ListItem.KEY_MENU_LOGO, "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xap1/t1.0-1/p48x48/1454785_760379783978094_98624236_n.jpg");
        this.sList.add(map);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

    }
}

class ProgressTask extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }
}

