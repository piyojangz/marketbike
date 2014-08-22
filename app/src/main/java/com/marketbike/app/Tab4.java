package com.marketbike.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.LoginButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.marketbike.app.R;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.facebook.*;
import com.facebook.model.*;
import com.marketbike.app.helper.JsonHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONObject;

public class Tab4 extends Fragment implements Transformation {

    /**
     * Called when the activity is first created.
     */

    private ListView list_menu;
    private static String[] menuItems;
    private SettingAdapter SettingAdapter;
    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> sList;

    private boolean IS_LOGIN;
    private String get_id, get_name, get_gender, get_email, get_birthday;
    private String imageURL;
    private ImageView image_profile;
    private TextView txtFullName;
    private ImageView imageBgProfile;
    private  Session session;
    private RelativeLayout profileConsole;
    private LinearLayout tab4;
    static SharedPreferences settings;
    static SharedPreferences.Editor editor;
    private String _is_notification;
    private String is_notification;
    private View rootView;
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "416625437190";
    private  com.squareup.picasso.Transformation transformation = this;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.rootView = inflater.inflate(R.layout.tab4, container, false);
        image_profile = (ImageView) rootView.findViewById(R.id.image_profile);
        imageBgProfile = (ImageView) rootView.findViewById(R.id.imageBgProfile);
        profileConsole = (RelativeLayout)rootView.findViewById(R.id.profileConsole);
        txtFullName = (TextView) rootView.findViewById(R.id.txtFullName);
        tab4  = (LinearLayout)rootView.findViewById(R.id.tab4);

        settings = getActivity().getPreferences(getActivity().MODE_WORLD_WRITEABLE);
        editor = settings.edit();

        session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        } else if (session == null || session.isClosed()) {
            profileConsole.setVisibility(View.GONE);
            tab4.setPadding(0, 20, 0, 0);
        }


        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    loadSetting();
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                list_menu = (ListView)rootView.findViewById(R.id.list_menu);
                if(sList != null) {
                    SettingAdapter = new SettingAdapter(getActivity(), sList);
                    list_menu.setAdapter(SettingAdapter);
                }
            }

        };
        task.execute((Void[]) null);




        return rootView;
    }

    private void loadSetting() {

        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(getActivity().getApplicationContext());
            }
            regid = gcm.register(PROJECT_NUMBER);
            String url = "http://marketbike.zoaish.com/api/get_notification_setting/" + regid;
            JSONObject jsondata = JsonHelper.getJson(url).getJSONObject("result");
            is_notification = jsondata.getString("is_notification").trim();

            //list_menu
            String[] menu = {"Notification"};
            String[] img = {"ic_action_alarms"};
            String[] psetting = {is_notification};

            sList = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < menu.length; i++) {
                Log.v("fb", "is_notification: " + psetting[i]);

                map = new HashMap<String, String>();
                map.put(ListItem.KEY_TITLE, menu[i]);
                map.put(ListItem.KEY_IMAGE, img[i]);
                map.put(ListItem.KEY_SET,psetting[i]);
                sList.add(map);
            }

        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("fb", "Logged in...");
            Request.executeMeRequestAsync(session,
                    new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user,
                                                Response response) {
                            if (response != null) {
                                try {
                                    get_id = user.getId();
                                    get_name = user.getName();
                                    get_gender = (String) user.getProperty("gender");
                                    get_email = (String) user.getProperty("email");
                                    get_birthday = user.getBirthday();
                                    imageURL = "https://graph.facebook.com/" + get_id + "/picture?type=large";
                                    Picasso.with(getActivity()).load(imageURL).into(image_profile);
                                    String imgBg = "http://marketbike.zoaish.com/public/images/default_bg.jpg";
                                    Picasso.with(getActivity()).load(imgBg).into(imageBgProfile);
                                    txtFullName.setText(get_name);
                                    editor.putString("fbid",get_id);
                                    editor.commit();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else if (state.isClosed()) {
            Log.i("fb", "Logged out...");
            profileConsole.setVisibility(View.GONE);
            tab4.setPadding(0,20,0,0);
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




        return output;


    }



    @Override
    public String key() {
        return "circle()";
    }
}