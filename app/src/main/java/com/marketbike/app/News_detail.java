package com.marketbike.app;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.marketbike.app.MenuAdapter;
import com.marketbike.app.R;
import com.marketbike.app.custom.setAppFont;

import java.util.ArrayList;
import java.util.HashMap;

public class News_detail extends Activity {

    private ArrayList<HashMap<String, String>> DataList;
    private HashMap map;
    private MenuAdapter menuAdpt;
    private ListView lv;
    private ArrayAdapter<String> listAdapter;
    protected ArrayList<HashMap<String, String>> sList;
    private CharSequence mTitle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        /*Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/ThaiSansNeue-Light.ttf");
        final ViewGroup mContainer = (ViewGroup) findViewById(
                android.R.id.content).getRootView();
        setAppFont.setAppFont(mContainer, typeFace);*/
        setTitle(this.getIntent().getCharSequenceExtra(ListItem.KEY_TITLE));
        WebView webview = (WebView) findViewById(R.id.news_detail);
        webview.loadUrl("http://marketbike.zoaish.com/api/get_content/" + this.getIntent().getCharSequenceExtra(ListItem.KEY_ID));




        AdView adView = (AdView) this.findViewById(R.id.adView_detail);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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