package com.marketbike.app.adapter;

import android.app.Activity;
import android.content.Context;
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

import com.marketbike.app.custom.ListItem;
import com.marketbike.app.R;
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


public class ListAdapter extends BaseAdapter implements Transformation {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private static final String TAG = "MyActivity";
    private Typeface typeFace;

    public ListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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

        HashMap<String, String> smart = new HashMap<String, String>();
        Context context = this.activity;
        smart = this.data.get(position);
        String type = smart.get(ListItem.KEY_TYPE).toString();
        String title = smart.get(ListItem.KEY_TITLE).toString();
        String createdate = smart.get(ListItem.KEY_CREATEDATE).toString();
        String desc = smart.get(ListItem.KEY_DESC).toString();
        String url = smart.get(ListItem.KEY_IMAGE).toString();
        String url_brand = smart.get(ListItem.KEY_IMAGE_LOGO).toString();

        if (type == "HILIGHT") {
            //convertView = inflater.inflate(R.layout.news_hilight, parent, false);
            convertView = inflater.inflate(R.layout.news_list, parent, false);
        } else {
            convertView = inflater.inflate(R.layout.news_list, parent, false);
        }

        Typeface typeFace = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        final ViewGroup mContainer = (ViewGroup) convertView.getRootView();
        setAppFont.setAppFont(mContainer, typeFace);
        ImageView imglogo = (ImageView) convertView.findViewById(R.id.img_thumnail);
        ImageView img_brand = (ImageView) convertView.findViewById(R.id.img_brand);
        TextView txt_title = (TextView) convertView.findViewById(R.id.txt_title);
        //TextView txt_desc = (TextView) convertView.findViewById(R.id.txt_desc);
        TextView txt_createdate = (TextView) convertView.findViewById(R.id.txt_createdate);

        txt_title.setText(title);
        //txt_desc.setText(desc);
        long dtMili = System.currentTimeMillis();
        TimeAgo tm = new TimeAgo(context);
        Date date1;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date1 = dateFormat.parse(createdate);
            txt_createdate.setText(tm.timeAgo(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Picasso.with(context).load(url).into(imglogo);
        Picasso.with(context).load(url_brand).into(img_brand);
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
