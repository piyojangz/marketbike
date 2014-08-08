package com.marketbike.app;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import  com.marketbike.app.helper.JsonHelper;
import android.support.v4.app.Fragment;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;


public class Tab1 extends Fragment {

    private ArrayList<HashMap<String, String>> DataList;
    private HashMap map;
    private MenuAdapter menuAdpt;
    private ListView lv;
    protected ArrayList<HashMap<String, String>> sList;
    AsyncTask<Void, Void, Void> task;
    private ProgressDialog progress;
    private JSONArray data;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.tab1, container, false);


        this.lv = (ListView) rootView.findViewById(R.id.menu_listView);
        this.sList = new ArrayList<HashMap<String, String>>();
        this.progress = new ProgressDialog(this.getActivity());
        this.DataList = new ArrayList<HashMap<String, String>>();
        this.createMenu();



        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String ID = sList.get(position).get(ListItem.KEY_MENU_ID).toString();
                String TITLE = sList.get(position).get(ListItem.KEY_MENU_TITLE).toString();
                Intent newActivity = new Intent(container.getContext(),News.class);
                newActivity.putExtra(ListItem.KEY_MENU_ID, ID);
                newActivity.putExtra(ListItem.KEY_MENU_TITLE, TITLE);
                startActivity(newActivity);
            }
        });

        return rootView;
    }



    private void createMenu() {

        this.task = new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progress.setMessage("Downloading... :) ");
                //progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //progress.setIndeterminate(true);
                //progress.show();
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
               // progress.dismiss();
               // Log.i("mylog", "obj: " + data);
                bindList();
            }

        };
        this.task.execute((Void[]) null);

    }

    private void bindList() {
        this.menuAdpt = new MenuAdapter(this.getActivity(), this.sList);
        this.lv.setAdapter(this.menuAdpt);
    }
}