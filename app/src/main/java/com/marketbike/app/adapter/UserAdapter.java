package com.marketbike.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marketbike.app.custom.ListItem;
import com.marketbike.app.R;
import com.marketbike.app.helper.TimeAgo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Breeshy on 08/06/2014.
 */


public class UserAdapter extends BaseAdapter implements Transformation {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public static final String PREFS_NAME = "MyData_Settings";
    private boolean[] mHighlightedPositions;

    public UserAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        this.activity = a;
        this.data = d;
        this.mHighlightedPositions = new boolean[this.data.size()];
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
        View view = convertView;
        if (view == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friend_list, parent, false);
        }

        final TextView txt_title = (TextView) convertView.findViewById(R.id.title);
        TextView txt_createdate = (TextView) convertView.findViewById(R.id.txt_createdate);
        ImageView imglogo = (ImageView) convertView.findViewById(R.id.imglogo);
        Button btn_request_friend = (Button) convertView.findViewById(R.id.btn_request_friend);
        final int pos = position;

        HashMap<String, String> smart = new HashMap<String, String>();
        Context context = this.activity;
        smart = this.data.get(position);

        String title = smart.get(ListItem.KEY_NAME).toString();
        String url = smart.get(ListItem.KEY_MENU_LOGO).toString();
        String createdate = smart.get(ListItem.KEY_CREATEDATE).toString();
        String status = smart.get(ListItem.KEY_STATUS).toString();
        final String fbid = smart.get(ListItem.KEY_FBID).toString();
        final String friendid = smart.get(ListItem.KEY_ID).toString();


        if (mHighlightedPositions[position]) {
            btn_request_friend.setBackgroundResource(R.drawable.btn_friend_defult);
            btn_request_friend.setText("Request");
            btn_request_friend.setTextColor(activity.getResources().getColor(R.color.requested));
            btn_request_friend.setPressed(false);
        } else {
            if (status.equals("1")) {
                btn_request_friend.setBackgroundResource(R.drawable.btn_friend_defult);
                btn_request_friend.setText("Request");
                btn_request_friend.setTextColor(activity.getResources().getColor(R.color.requested));
                btn_request_friend.setPressed(false);
            } else {
                btn_request_friend.setBackgroundResource(R.drawable.btn_accept);
                btn_request_friend.setText("Request");
                btn_request_friend.setTextColor(activity.getResources().getColor(R.color.request));

                btn_request_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {


                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                            }


                            @Override
                            protected Void doInBackground(Void... params) {
                                SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, activity.MODE_PRIVATE);
                                String userid = settings.getString("fbid", "");
                                String url = "http://marketbike.zoaish.com/api/request_friend/";
                                HttpClient httpclient = new DefaultHttpClient();
                                HttpPost httppost = new HttpPost(url);
                                try {
                                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                                    nameValuePairs.add(new BasicNameValuePair("userid", userid));
                                    nameValuePairs.add(new BasicNameValuePair("friendid", friendid));


                                    UrlEncodedFormEntity formEntity = null;
                                    try {
                                        formEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                                    } catch (UnsupportedEncodingException e2) {
                                        e2.printStackTrace();
                                    }
                                    if (formEntity != null)
                                        httppost.setEntity(formEntity);
                                    HttpResponse response = httpclient.execute(httppost);
                                    //Log.v("debug","response = " + response);


                                } catch (ClientProtocolException e) {
                                    // TODO Auto-generated catch block
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                // RelativeLayout parent = (RelativeLayout) v.getParent();
                                //  Button button = (Button)parent.getChildAt(3);
                                // button.setBackgroundResource(R.drawable.ic_rq_friend_1);
                                // Log.d("CLICKED POSITION", Integer.valueOf(pos).toString());
                                // ((Button) v).setBackgroundResource(R.drawable.ic_rq_friend_1);
                                int position = Integer.valueOf(pos);
                                //Log.d("debug", "Button row pos click: " + position);

                                // Toggle background resource
                                RelativeLayout layout = (RelativeLayout) v.getParent();
                                Button button = (Button) layout.getChildAt(3);
                                if (mHighlightedPositions[position]) {
                                    button.setBackgroundResource(R.drawable.btn_friend_defult);
                                    button.setText("Request");
                                    button.setTextColor(activity.getResources().getColor(R.color.requested));
                                } else {
                                    button.setBackgroundResource(R.drawable.btn_friend_defult);
                                    button.setText("Request");
                                    button.setTextColor(activity.getResources().getColor(R.color.requested));
                                    mHighlightedPositions[position] = true;
                                }
                            }
                        }.execute();


                    }
                });
            }
        }


        TimeAgo tm = new TimeAgo(context);
        Date date1;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date1 = dateFormat.parse(createdate);
            txt_createdate.setText("joined " + tm.timeAgo(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txt_title.setText(title);

        Picasso.with(context).load(url).transform(this).into(imglogo);
        return convertView;
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

    @Override
    public String key() {
        return "circle()";
    }

}
