package com.marketbike.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.marketbike.app.custom.setAppFont;

import java.util.ArrayList;
import java.util.HashMap;

public class Tab1 extends Activity {

    private ArrayList<HashMap<String, String>> DataList;
    private HashMap map;
    private MenuAdapter menuAdpt;
    private ListView lv;
    protected ArrayList<HashMap<String, String>> sList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);

        Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/HelveticaNeueLight.ttf");
        final ViewGroup mContainer = (ViewGroup) findViewById(
                android.R.id.content).getRootView();
        setAppFont.setAppFont(mContainer, typeFace);

        this.lv = (ListView) findViewById(R.id.menu_listView);
        this.sList = new ArrayList<HashMap<String, String>>();

        this.DataList = new ArrayList<HashMap<String, String>>();
        this.createMenu();
        this.menuAdpt = new MenuAdapter(this, this.sList);

        this.lv.setAdapter(this.menuAdpt);


        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // a.finish();

                String ID = sList.get(position).get(ListItem.KEY_MENU_ID).toString();
                String TITLE = sList.get(position).get(ListItem.KEY_MENU_TITLE).toString();
                Intent newActivity = new Intent(getBaseContext(), News.class);
                newActivity.putExtra(ListItem.KEY_MENU_ID, ID);
                newActivity.putExtra(ListItem.KEY_MENU_TITLE, TITLE);
                startActivity(newActivity);

                //  overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
            }
        });


    }


    private void createMenu() {
         String url = "https://marketbike.zoaish.com/api/get_category";


        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Kawasaki");
        this.map.put(ListItem.KEY_MENU_LOGO, "https://www.proride.net/images/logo-kawasaki.gif");
        this.sList.add(map);


        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Honda");
        this.map.put(ListItem.KEY_MENU_LOGO, "http://www.a1grafix.com/image/cache/data/category/honda-logo_large-50x50.png");
        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Ducati");
        this.map.put(ListItem.KEY_MENU_LOGO, "http://live.ducati.com/assets/logo-e98559d0817fe8b9418d0a859458efbd.png");
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
}