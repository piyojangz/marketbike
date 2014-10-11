package com.marketbike.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.marketbike.app.custom.ListItem;
import com.marketbike.app.R;
import com.marketbike.app.custom.setAppFont;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Breeshy on 08/06/2014.
 */


public class SettingAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private CheckBox input_notification;
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "416625437190";
    private ViewGroup context;


    public SettingAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        this.activity = a;
        this.data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public int getCount() {
        return this.data.size();
    }


    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        this.context = parent;
        convertView = inflater.inflate(R.layout.menulist_row, parent, false);
        Typeface typeFace = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        final ViewGroup mContainer = (ViewGroup) convertView.getRootView();
        setAppFont.setAppFont(mContainer, typeFace);

        TextView txt_title = (TextView) convertView.findViewById(R.id.title);
        ImageView imglogo = (ImageView) convertView.findViewById(R.id.list_image);
        input_notification = (CheckBox) convertView.findViewById(R.id.input_notification);


        input_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {


                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        try {
                            if (gcm == null) {
                                gcm = GoogleCloudMessaging.getInstance(context.getContext().getApplicationContext());
                            }
                            regid = gcm.register(PROJECT_NUMBER);
                            //String fbid =  Tab4.settings.getString("fbid", "0");
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://marketbike.zoaish.com/api/set_notification_setting/");
                            Log.v("fb", "gcm: " + regid);
                            // Add your data
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("regid", regid));
                            String value = "0";
                            if (input_notification.isChecked()) {
                                value = "1";
                            }
                            Log.v("fb", "value: " + value);
                            nameValuePairs.add(new BasicNameValuePair("value", value));
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            // execute HTTP post request
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity resEntity = response.getEntity();

                            if (resEntity != null) {

                                String responseStr = EntityUtils.toString(resEntity).trim();
                                Log.v("fb", "Response: " + responseStr);

                                // you can add an if statement here and do other actions based on the response
                            }

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                    }

                };
                task.execute((Void[]) null);


            }
        });

        HashMap<String, String> smart = new HashMap<String, String>();
        smart = this.data.get(position);
        String title = smart.get(ListItem.KEY_TITLE).toString();
        String set = smart.get(ListItem.KEY_SET).toString();


        txt_title.setText(title);
        if (set.equals("1")) {
            input_notification.setChecked(true);
        } else {
            input_notification.setChecked(false);
        }
        int id = this.activity.getResources().getIdentifier("com.marketbike.app:drawable/" + smart.get(ListItem.KEY_IMAGE), null, null);
        imglogo.setImageResource(id);

        return convertView;
    }

}
