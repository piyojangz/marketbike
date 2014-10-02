package com.marketbike.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class TabGroup extends Fragment {

    /**
     * Called when the activity is first created.
     */
    private ArrayList<HashMap<String, String>> DataList;
    private HashMap map;
    private TripAdapter tripAdpt;
    private ListView lv;
    protected ArrayList<HashMap<String, String>> sList;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.tab3, container, false);

        this.lv = (ListView) rootView.findViewById(R.id.tab3_listView);
        this.sList = new ArrayList<HashMap<String, String>>();

        this.DataList = new ArrayList<HashMap<String, String>>();
        this.createTrips();
        this.tripAdpt = new TripAdapter(this.getActivity(), this.sList);
        this.lv.setAdapter(this.tripAdpt);


        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("mylog", "onItemClick:");
                String ID = sList.get(position).get(ListItem.KEY_MENU_ID).toString();
                String TITLE = sList.get(position).get(ListItem.KEY_MENU_TITLE).toString();
                Intent newActivity = new Intent(container.getContext(), Trip.class);
                newActivity.putExtra(ListItem.KEY_MENU_ID, ID);
                newActivity.putExtra(ListItem.KEY_MENU_TITLE, TITLE);
                startActivity(newActivity);
            }
        });

        return rootView;
    }

    private void createTrips() {
        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "ER6-Club Thailand");
        this.map.put(ListItem.KEY_MENU_LOGO, "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xap1/t1.0-1/c33.33.418.418/s160x160/561413_356182494488330_715098968_n.jpg");
        this.sList.add(map);


        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Ninja300 Club Thailand");
        this.map.put(ListItem.KEY_MENU_LOGO, "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/t1.0-1/c1.0.160.160/p160x160/10401932_1424591527806439_6608546196938191283_n.jpg");
        this.sList.add(map);


        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Z800 Club Thailand");
        this.map.put(ListItem.KEY_MENU_LOGO, "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/t1.0-1/c95.3.395.395/s160x160/1234747_194747977371010_1425671170_n.jpg");
        this.sList.add(map);


        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Ducati Thailand");
        this.map.put(ListItem.KEY_MENU_LOGO, "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpa1/t1.0-1/c66.66.828.828/s160x160/65184_429211237146806_1255761957_n.jpg");
        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Honda CBR250R");
        this.map.put(ListItem.KEY_MENU_LOGO, "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-prn2/t1.0-1/p160x160/1426668_617784511593570_593621757_n.jpg");
        this.sList.add(map);

        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Honda CBR 150R");
        this.map.put(ListItem.KEY_MENU_LOGO, "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-frc1/t1.0-1/c22.22.272.272/s160x160/250599_221837081179111_3748108_n.jpg");
        this.sList.add(map);


        this.map = new HashMap<String, String>();
        this.map.put(ListItem.KEY_MENU_ID, "1");
        this.map.put(ListItem.KEY_MENU_TITLE, "Honda CBR 150R");
        this.map.put(ListItem.KEY_MENU_LOGO, "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-frc1/t1.0-1/c22.22.272.272/s160x160/250599_221837081179111_3748108_n.jpg");
        this.sList.add(map);
    }
}