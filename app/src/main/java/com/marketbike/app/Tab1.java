package com.marketbike.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import  com.marketbike.app.helper.JsonHelper;
import com.marketbike.app.custom.setAppFont;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Tab1 extends Activity {

    private ArrayList<HashMap<String, String>> DataList;
    private HashMap map;
    private MenuAdapter menuAdpt;
    private ListView lv;
    protected ArrayList<HashMap<String, String>> sList;
    AsyncTask<Void, Void, Void> task;
    private ProgressDialog progress;
    private JSONArray data;
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
        this.progress = new ProgressDialog(this);
        this.DataList = new ArrayList<HashMap<String, String>>();
        this.createMenu();



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
                    String url = "http://marketbike.zoaish.com/api/get_category";

                     data =  JsonHelper.getJson(url).getJSONArray("result");

                    for(int i = 0; i < data.length(); i++){

                        String id = data.getJSONObject(i).getString("ID");
                        String title = data.getJSONObject(i).getString("Headline");
                        String thumbnail = "http://marketbike.zoaish.com/public/uploads/" + data.getJSONObject(i).getString("Thumbnail_Image");

                        map = new HashMap<String, String>();
                        map.put(ListItem.KEY_MENU_ID, id);
                        map.put(ListItem.KEY_MENU_TITLE,title);
                        map.put(ListItem.KEY_MENU_LOGO, thumbnail);
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
                progress.dismiss();
               // Log.i("mylog", "obj: " + data);
                bindList();
            }

        };
        this.task.execute((Void[]) null);

    }

    private void bindList() {
        this.menuAdpt = new MenuAdapter(this, this.sList);
        this.lv.setAdapter(this.menuAdpt);
    }
}