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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.marketbike.app.custom.ListItem;
import com.marketbike.app.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Breeshy on 08/06/2014.
 */


public class Trip_Community_Adapter extends BaseAdapter implements Transformation {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private static final String TAG = "MyActivity";
    private Typeface typeFace;
    protected boolean isjoind;

    public Trip_Community_Adapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
        if (type == "HEADER") {
            convertView = inflater.inflate(R.layout.trip_header, parent, false);
            String title = smart.get(ListItem.KEY_TITLE).toString();
            String urllogo = smart.get(ListItem.KEY_IMAGE_LOGO).toString();
            String url_img_header = smart.get(ListItem.KEY_IMAGE_HEADER).toString();
            ImageView imageBgProfile = (ImageView) convertView.findViewById(R.id.imageBgProfile);
            ImageView imglogo = (ImageView) convertView.findViewById(R.id.imgLogo);
            TextView txt_title = (TextView) convertView.findViewById(R.id.txtTitle);
            final Button btn_addtrip = (Button) convertView.findViewById(R.id.btn_addtrip);
            this.isjoind = false;


            txt_title.setText(title);
            Picasso.with(context).load(url_img_header).into(imageBgProfile);
            Picasso.with(context).load(urllogo).transform(this).into(imglogo);


            btn_addtrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isjoind) {
                        btn_addtrip.setText("  Your trip");
                        btn_addtrip.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_add_trip_success, 0, 0, 0);
                        isjoind = true;
                    } else {
                        btn_addtrip.setText("  Request trip");
                        btn_addtrip.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_add_trip_request, 0, 0, 0);
                        isjoind = false;
                    }
                }
            });

        } else {
            convertView = inflater.inflate(R.layout.trip_user_list, parent, false);
            String title = smart.get(ListItem.KEY_TITLE).toString();
            String desc = smart.get(ListItem.KEY_DESC).toString();
            String url = smart.get(ListItem.KEY_IMAGE).toString();
            ImageView imglogo = (ImageView) convertView.findViewById(R.id.img_thumnail);
            TextView txt_title = (TextView) convertView.findViewById(R.id.txt_title);
            TextView txt_desc = (TextView) convertView.findViewById(R.id.txt_desc);


            txt_title.setText(title);
            txt_desc.setText(desc);
            Picasso.with(context).load(url).into(imglogo);

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
