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

import com.marketbike.app.XListView.XListView;
import com.marketbike.app.XListView.XListView.IXListViewListener;
import com.marketbike.app.custom.setAppFont;
import com.marketbike.app.helper.JsonHelper;

import org.json.JSONArray;

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
    private JSONArray data;
    private  String cateid;
    private  boolean isfirst = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);

        Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/HelveticaNeueLight.ttf");
        final ViewGroup mContainer = (ViewGroup) findViewById(
                android.R.id.content).getRootView();
        setAppFont.setAppFont(mContainer, typeFace);

        setTitle(this.getIntent().getCharSequenceExtra(ListItem.KEY_MENU_TITLE));
        this.lv = (XListView) findViewById(R.id.menu_listView);

        this.lv.setPullRefreshEnable(true);
        this.lv.setPullLoadEnable(true);

        this.sList = new ArrayList<HashMap<String, String>>();
        this.DataList = new ArrayList<HashMap<String, String>>();
        this.progress = new ProgressDialog(this);
        this.lv.setXListViewListener(this);

       this.cateid =  this.getIntent().getCharSequenceExtra(ListItem.KEY_MENU_ID).toString();


        this.loadItemList(0);




        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // a.finish();

                String URL = sList.get(position - 1).get(ListItem.KEY_URL).toString();
                String TITLE = sList.get(position - 1).get(ListItem.KEY_TITLE).toString();
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
                    String url = "http://marketbike.zoaish.com/api/get_content_by_cate/" + cateid;

                    data =  JsonHelper.getJson(url).getJSONArray("result");

                    for(int i = 0; i < data.length(); i++){

                        String id = data.getJSONObject(i).getString("ID");
                        String title = data.getJSONObject(i).getString("Headline");
                        String shortdesc = data.getJSONObject(i).getString("Short_Description");
                        String thumbnail = "http://marketbike.zoaish.com/public/uploads/" + data.getJSONObject(i).getString("Thumbnail_Image");


                        if (isfirst) {
                            map = new HashMap<String, String>();
                            map.put(ListItem.KEY_MENU_ID,id);
                            map.put(ListItem.KEY_TYPE, "HILIGHT");
                            map.put(ListItem.KEY_TITLE, title);
                            map.put(ListItem.KEY_DESC, shortdesc);
                            map.put(ListItem.KEY_IMAGE, thumbnail);
                            map.put(ListItem.KEY_URL,thumbnail);


                            sList.add(map);
                        }
                        else {

                            map = new HashMap<String, String>();
                            map.put(ListItem.KEY_MENU_ID, "1");
                            map.put(ListItem.KEY_TYPE, "CONTENT");
                            map.put(ListItem.KEY_TITLE, title);
                            map.put(ListItem.KEY_DESC, shortdesc);
                            map.put(ListItem.KEY_IMAGE, thumbnail);
                            map.put(ListItem.KEY_URL,thumbnail);

                            sList.add(map);
                        }

                        isfirst = false;
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

