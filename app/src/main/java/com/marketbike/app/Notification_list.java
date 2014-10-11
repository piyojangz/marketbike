package com.marketbike.app;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marketbike.app.adapter.UserReqAdapter;
import com.marketbike.app.custom.ListItem;
import com.marketbike.app.helper.JsonHelper;
import com.marketbike.app.helper.ScrollViewExt;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class Notification_list extends Activity implements Transformation {
    private String id;
    private HashMap map;
    private UserReqAdapter userAdpt;
    private String title, headline, thumbnail, userimg, price, user, description, image1, image2, image3, image4, image5, Images;
    private Menu optionsMenu;
    protected ArrayList<HashMap<String, String>> sList;
    private ScrollViewExt scroll_detail;
    private RelativeLayout frame_thumnail;
    private AbsoluteLayout wrap_item;
    private Boolean is_view_gone = false;
    public static final String PREFS_NAME = "MyData_Settings";
    private String userid;

    private JSONArray data0;
    private JSONArray data;
    private static final int LIMIT = 100;
    private int OFFSET = 0;
    private ListView lv;
    private TextView notification_label;
    AsyncTask<Void, Void, Void> task;
    private ArrayList<HashMap<String, String>> DataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request_list);

        setTitle("Notification");


        SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, this.MODE_PRIVATE);
        this.userid = settings.getString("fbid", "");

        this.notification_label = (TextView) this.findViewById(R.id.notification_label);
        this.lv = (ListView) this.findViewById(R.id.friend_req_listView);
        this.sList = new ArrayList<HashMap<String, String>>();

        this.DataList = new ArrayList<HashMap<String, String>>();


        this.createList();


        this.title = this.getIntent().getCharSequenceExtra(ListItem.KEY_TITLE).toString();
        this.id = this.getIntent().getCharSequenceExtra(ListItem.KEY_ID).toString();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(Integer.parseInt(id.toString()));


    }


    private void set_notification_zero() {


        this.task = new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {

                    String url0 = "http://marketbike.zoaish.com/api/set_notification_zero/" + userid;
                    data0 = JsonHelper.getJson(url0).getJSONArray("result");


                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
            }

        };
        this.task.execute((Void[]) null);

    }

    private void createList() {


        this.task = new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                setRefreshActionButtonState(true);
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {

                    String url = "http://marketbike.zoaish.com/api/get_user_request/" + userid + "/" + OFFSET + "/" + LIMIT;

                    data = JsonHelper.getJson(url).getJSONArray("result");

                    for (int i = 0; i < data.length(); i++) {

                        String id = data.getJSONObject(i).getString("userid");
                        String name = data.getJSONObject(i).getString("name");
                        String fbid = data.getJSONObject(i).getString("fbid");
                        String email = data.getJSONObject(i).getString("email");
                        String status = data.getJSONObject(i).getString("status");

                        String create_date = data.getJSONObject(i).getString("create_date");
                        String request_date = data.getJSONObject(i).getString("request_date");

                        String thumbnail = "https://graph.facebook.com/" + fbid + "/picture?type=large";

                        map = new HashMap<String, String>();
                        map.put(ListItem.KEY_ID, id);
                        map.put(ListItem.KEY_NAME, name);
                        map.put(ListItem.KEY_FBID, fbid);
                        map.put(ListItem.KEY_EMAIL, email);
                        map.put(ListItem.KEY_CREATEDATE, create_date);
                        map.put(ListItem.KEY_REQUESTDATE, request_date);
                        map.put(ListItem.KEY_MENU_LOGO, thumbnail);
                        map.put(ListItem.KEY_STATUS, status);
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
                bindList();
                set_notification_zero();
                setRefreshActionButtonState(false);
            }

        };
        this.task.execute((Void[]) null);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                finish();
                return true;
            case R.id.action_refresh:
                this.sList.clear();
                this.createList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);


        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());


        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));


        if (output != bitmap) {
            bitmap.recycle();
        }


        return output;


    }

    private void bindList() {
        if (this.sList.size() > 0) {
            this.lv.setVisibility(View.VISIBLE);
            this.notification_label.setVisibility(View.GONE);
            this.userAdpt = new UserReqAdapter(this, this.sList);
            this.lv.setAdapter(this.userAdpt);
        } else {
            this.lv.setVisibility(View.GONE);
            this.notification_label.setVisibility(View.VISIBLE);
            this.notification_label.setText("No new notification");
        }
    }

    @Override
    public String key() {
        return "circle()";
    }
}

