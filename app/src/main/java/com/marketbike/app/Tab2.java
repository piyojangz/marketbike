package com.marketbike.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class Tab2 extends Activity {

    private ArrayList<HashMap<String, String>> DataList;
    HashMap map;
    private MenuAdapter menuAdpt;
    private ListView lv;
    private ArrayAdapter<String> listAdapter;
    protected ArrayList<HashMap<String, String>> sList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab2);

    }

}