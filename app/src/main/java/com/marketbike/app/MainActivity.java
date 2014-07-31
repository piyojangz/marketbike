package com.marketbike.app;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.app.TabActivity;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.marketbike.app.custom.setAppFont;

public class MainActivity extends TabActivity implements OnTabChangeListener {
    /**
     * Called when the activity is first created.
     */
    TabHost tabHost;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/HelveticaNeueLight.ttf");
        final ViewGroup mContainer = (ViewGroup) findViewById(
                android.R.id.content).getRootView();
        setAppFont.setAppFont(mContainer, typeFace);

        // Get TabHost Refference
        tabHost = getTabHost();

        // Set TabChangeListener called when tab changed
        tabHost.setOnTabChangedListener(this);

        TabHost.TabSpec spec;
        Intent intent;

        /************* TAB1 ************/
        // Create  Intents to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, Tab1.class);
        spec = tabHost.newTabSpec("First").setIndicator("Brand")
                .setContent(intent);

        //Add intent to tab
        tabHost.addTab(spec);

        /************* TAB2 ************/
       /* intent = new Intent().setClass(this, Tab2.class);
        spec = tabHost.newTabSpec("Second").setIndicator("Live")
                .setContent(intent);
        tabHost.addTab(spec);*/

        /************* TAB3 ************/
        intent = new Intent().setClass(this, Tab3.class);
        spec = tabHost.newTabSpec("Third").setIndicator("Trip")
                .setContent(intent);
        tabHost.addTab(spec);

        /************* TAB4 ************/
        intent = new Intent().setClass(this, Tab4.class);
        spec = tabHost.newTabSpec("Fourth").setIndicator("Market")
                .setContent(intent);
        tabHost.addTab(spec);


        // Set drawable images to tab
        //tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_bg_selector);
        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_bg_selector);
        tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tab_bg_selector);

        // Set Tab1 as Default tab and change image
        tabHost.getTabWidget().setCurrentTab(0);
        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_bg_selector);


    }


    @Override
    public void onTabChanged(String tabId) {
        /************ Called when tab changed *************/
        //********* Check current selected tab and change according images *******/
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            if (i == 0) {
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_bg_selector);
                // TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            //} else if (i == 1) {
                //tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_bg_selector);
                //  TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            } else if (i == 1) {
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_bg_selector);
                // TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            } else if (i == 2) {
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_bg_selector);
                // TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            }
        }

        // TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        // tv.setTextColor(Color.parseColor("#D35400"));

        //Log.i("mylog", "CurrentTab: " + tabHost.getCurrentTab());

        if (tabHost.getCurrentTab() == 0)
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.tab_bg_selector);
        //else if (tabHost.getCurrentTab() == 1)
            //tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.tab_bg_selector);
        else if (tabHost.getCurrentTab() == 1)
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.tab_bg_selector);
        else if (tabHost.getCurrentTab() == 2)
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.tab_bg_selector);
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