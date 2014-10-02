package com.marketbike.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.marketbike.app.helper.TimeAgo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Breeshy on 08/06/2014.
 */


public class FriendsAdapter extends BaseAdapter implements Transformation {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public static final String PREFS_NAME = "MyData_Settings";
    private boolean[] mHighlightedPositions;

    public FriendsAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
        String requestdate = smart.get(ListItem.KEY_APPROVEDATE).toString();
        String status = smart.get(ListItem.KEY_STATUS).toString();
        final String fbid = smart.get(ListItem.KEY_FBID).toString();
        final String friendid = smart.get(ListItem.KEY_ID).toString();

        if (mHighlightedPositions[position]) {
            btn_request_friend.setBackgroundResource(R.drawable.ic_rq_friend_2_0);
        } else {
            if (status.equals("1")) {
                btn_request_friend.setBackgroundResource(R.drawable.ic_rq_friend_2);
            } else {
                btn_request_friend.setBackgroundResource(R.drawable.ic_rq_friend_2_0);
            }
        }

        btn_request_friend.setVisibility(View.INVISIBLE);

        TimeAgo tm = new TimeAgo(context);
        Date date1;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date1 = dateFormat.parse(requestdate);
            txt_createdate.setText("approved " + tm.timeAgo(date1));
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
