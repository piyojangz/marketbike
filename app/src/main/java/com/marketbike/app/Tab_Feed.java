package com.marketbike.app;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;

import com.facebook.Session;
import com.marketbike.app.RefreshableListView.onListLoadMoreListener;
import com.marketbike.app.RefreshableListView.onListRefreshListener;
import com.marketbike.app.adapter.FeedAdapter;
import com.marketbike.app.custom.ListItem;
import com.marketbike.app.helper.JsonHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;


public class Tab_Feed extends Fragment implements onListRefreshListener, onListLoadMoreListener {

    private ArrayList<HashMap<String, String>> DataList;
    private HashMap map;
    private FeedAdapter listAdpt;
    protected ArrayList<HashMap<String, String>> sList;
    private AsyncTask<Void, Void, Void> task;
    private Menu optionsMenu;
    private static final int LIMIT = 10;
    private int OFFSET = 0;
    AbsoluteLayout wrap_item;
    private RefreshableListView lv;
    private boolean FLAG_END;
    private View rootView;
    private String userid;
    public static final String PREFS_NAME = "MyData_Settings";
    private ImageButton btn_add_item;
    private Session session;
    private boolean isfirst = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.rootView = inflater.inflate(R.layout.tab2, container, false);
        this.wrap_item = (AbsoluteLayout) rootView.findViewById(R.id.wrap_item);
        //RefreshableList lines start
        setHasOptionsMenu(true);
        this.lv = (RefreshableListView) rootView.findViewById(R.id.prod_listView);
        this.lv.setOnListRefreshListener(this);//---------------------------------------------------------------Important
        this.lv.setOnListLoadMoreListener(this);
        this.lv.setDistanceFromBottom(2);
        this.lv.setAddButton(this.wrap_item);
        this.lv.getListView().setFooterDividersEnabled(true);
        this.lv.getListView().setDivider(null);
        this.lv.getListView().setDividerHeight(0);
        this.lv.getListView().setClipToPadding(false);
        //this.lv.getListView().setFooterDividersEnabled(false);
        //this.lv.getListView().setDivider(null);
        //this.lv.getListView().setDividerHeight(0);
        this.lv.getListView().setSelector(new ColorDrawable(Color.TRANSPARENT));
        this.sList = new ArrayList<HashMap<String, String>>();
        this.DataList = new ArrayList<HashMap<String, String>>();
        this.btn_add_item = (ImageButton) rootView.findViewById(R.id.btn_add_item);

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        this.userid = settings.getString("fbid", "");

        this.btn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session = Session.getActiveSession();
                if (session == null || session.isClosed()) {
                    Intent newActivity = new Intent(getActivity(), LoginActivity.class);
                    startActivity(newActivity);
                } else {
                    Intent Product_add = new Intent(getActivity(), Feed_add.class);
                    startActivityForResult(Product_add, 0);
                }
            }
        });

        this.firstload();
        return rootView;
    }


    private void bindList() {
        this.listAdpt = new FeedAdapter(getActivity(), this.sList);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_fade_in);
        this.lv.startAnimation(animation);
        this.lv.setAdapter(this.listAdpt);

    }

    private void firstload() {
        this.task = new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setRefreshActionButtonState(true);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    OFFSET = 0;
                    loadItemList(0);
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                bindList();
                setRefreshActionButtonState(false);
            }

        };
        this.task.execute((Void[]) null);

        /*this.lv.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                String TITLE = sList.get(position).get(ListItem.KEY_TITLE).toString();
                String ID = sList.get(position).get(ListItem.KEY_MENU_ID).toString();
                Intent newActivity = new Intent(getActivity(), Product_detail.class);
                newActivity.putExtra(ListItem.KEY_TITLE, TITLE);
                newActivity.putExtra(ListItem.KEY_ID, ID);
                startActivity(newActivity);

            }
        });*/


    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu.findItem(R.id.action_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    private void loadItemList(float size) {

        try {
            String url = "http://marketbike.zoaish.com/api/get_all_feed/" + this.userid + "/" + OFFSET + "/" + LIMIT;
            JSONArray data = JsonHelper.getJson(url).getJSONArray("result");

            if (data.length() == 0) {
                FLAG_END = true;
            } else {
                FLAG_END = false;
            }


            for (int i = 0; i < data.length(); i++) {

                String id = data.getJSONObject(i).getString("ID");
                String title = data.getJSONObject(i).getString("Short_Description");
                String create_date = data.getJSONObject(i).getString("Create_Date");
                String fname = data.getJSONObject(i).getString("firstname");
                String lname = data.getJSONObject(i).getString("lastname");
                String thumbnail = data.getJSONObject(i).getString("Thumbnail_Image");
                String fbid = data.getJSONObject(i).getString("fbid");

                map = new HashMap<String, String>();
                map.put(ListItem.KEY_MENU_ID, id);
                map.put(ListItem.KEY_TITLE, title);
                map.put(ListItem.KEY_CREATEDATE, create_date);
                map.put(ListItem.KEY_FIRSTNAME, fname);
                map.put(ListItem.KEY_LASTNME, lname);
                map.put(ListItem.KEY_IMAGE, thumbnail);
                map.put(ListItem.KEY_URL, thumbnail);
                map.put(ListItem.KEY_FBID, fbid);

                sList.add(map);
                OFFSET++;
            }


        } catch (Throwable e) {
            e.printStackTrace();
        }


    }


    private void onLoad() {


        this.setRefreshActionButtonState(false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Refresh(this.lv);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
        this.optionsMenu = menu;

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem Item) {
        switch (Item.getItemId()) {
            case R.id.action_refresh:
                this.Refresh(this.lv);
                return true;
        }
        return super.onOptionsItemSelected(Item);
    }

    @Override
    public void LoadMore(RefreshableListView list) {
        if (FLAG_END != true) {
            this.setRefreshActionButtonState(true);
            ////This just asyncly waits 3 seconds then does the finishRefresh()
            new AsyncTask<RefreshableListView, Object, RefreshableListView>() {
                protected RefreshableListView doInBackground(RefreshableListView... params) {
                    try {
                        loadItemList(0);
                    } catch (Exception e) {
                    }
                    return params[0];

                }

                @Override
                protected void onPostExecute(RefreshableListView list) {
                    //I just finish both here to not have to write two example mocks
                    lv.finishLoadingMore();//---------------------------------------------------------------------Important
                    onLoad();
                    listAdpt.notifyDataSetChanged();
                    super.onPostExecute(list);
                }
            }.execute(list);
        }
    }


    @Override
    public void Refresh(RefreshableListView list) {
        this.setRefreshActionButtonState(true);
        OFFSET = 0;
        isfirst = true;
        FLAG_END = false;
        sList.clear();

        lv.getListView().setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        ////This just asyncly waits 3 seconds then does the finishRefresh()
        new AsyncTask<RefreshableListView, Object, RefreshableListView>() {
            protected RefreshableListView doInBackground(RefreshableListView... params) {
                try {
                    loadItemList(0);
                } catch (Exception e) {
                }
                return params[0];

            }

            @Override
            protected void onPostExecute(RefreshableListView list) {
                lv.finishRefresh();//-------------------------------------------------------------------------Important

                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_fade_in);
                lv.startAnimation(animation);
                //I just finish both here to not have to write two example mocks
                onLoad();
                listAdpt.notifyDataSetChanged();
                lv.getListView().setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });

                super.onPostExecute(list);
            }
        }.execute(list);


    }
}