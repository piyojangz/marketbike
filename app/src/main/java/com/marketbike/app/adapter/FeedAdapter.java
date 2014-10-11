package com.marketbike.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marketbike.app.Feed_image_show;
import com.marketbike.app.R;
import com.marketbike.app.User_profile_Activity;
import com.marketbike.app.custom.ListItem;
import com.marketbike.app.custom.setAppFont;
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


public class FeedAdapter extends BaseAdapter implements Transformation {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public FeedAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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


    public View getView(final int position, View convertView, ViewGroup parent) {

        HashMap<String, String> smart = new HashMap<String, String>();
        Context context = this.activity;
        smart = this.data.get(position);
        String title = smart.get(ListItem.KEY_TITLE).toString();
        String fname = smart.get(ListItem.KEY_FIRSTNAME).toString();
        String lname = smart.get(ListItem.KEY_LASTNME).toString();
        final String url = smart.get(ListItem.KEY_IMAGE).toString();
        String createdate = smart.get(ListItem.KEY_CREATEDATE).toString();
        final String fbid = smart.get(ListItem.KEY_FBID).toString();
        final String strimg = "https://graph.facebook.com/" + fbid + "/picture?type=large";

        convertView = inflater.inflate(R.layout.product_list, parent, false);

        Typeface typeFace = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        final ViewGroup mContainer = (ViewGroup) convertView.getRootView();
        setAppFont.setAppFont(mContainer, typeFace);

        final TextView txt_user = (TextView) convertView.findViewById(R.id.txt_user);
        ImageView imglogo = (ImageView) convertView.findViewById(R.id.img_thumnail);
        ImageView user_img = (ImageView) convertView.findViewById(R.id.user_img);
        TextView txt_detail = (TextView) convertView.findViewById(R.id.txt_detail);
        TextView txt_createdate = (TextView) convertView.findViewById(R.id.txt_createdate);

        txt_user.setText(fname + " " + lname);
        txt_detail.setText(title);

        TimeAgo tm = new TimeAgo(context);
        Date date1;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date1 = dateFormat.parse(createdate);
            txt_createdate.setText(tm.timeAgo(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Picasso.with(context).load(strimg).transform(this).into(user_img);

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(activity, User_profile_Activity.class);
                newActivity.putExtra("fbid", fbid);
                activity.startActivity(newActivity);
            }
        });

        if (!url.equals("")) {
            Picasso.with(context).load(url).into(imglogo);
            imglogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newActivity = new Intent(activity, Feed_image_show.class);
                    newActivity.putExtra("strimg", url);
                    activity.startActivity(newActivity);
                }
            });
        } else {
            imglogo.setVisibility(View.GONE);
        }
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
