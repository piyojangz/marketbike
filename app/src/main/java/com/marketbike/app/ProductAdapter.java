package com.marketbike.app;

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

import com.marketbike.app.custom.setAppFont;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Breeshy on 08/06/2014.
 */


public class ProductAdapter extends BaseAdapter implements Transformation {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public ProductAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
        String title = smart.get(ListItem.KEY_TITLE).toString();
        String user = smart.get(ListItem.KEY_USER).toString();
        String price = smart.get(ListItem.KEY_PRICE).toString();
        String url = smart.get(ListItem.KEY_IMAGE).toString();

        convertView = inflater.inflate(R.layout.product_list, parent, false);

        Typeface typeFace = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        final ViewGroup mContainer = (ViewGroup) convertView.getRootView();
        setAppFont.setAppFont(mContainer, typeFace);
        ImageView imglogo = (ImageView) convertView.findViewById(R.id.img_thumnail);
        TextView txt_title = (TextView) convertView.findViewById(R.id.txt_title);
        TextView txt_price = (TextView) convertView.findViewById(R.id.txt_price);
        TextView txt_user = (TextView) convertView.findViewById(R.id.txt_user);

        txt_title.setText(title);
        txt_price.setText(price + " บาท");
        txt_user.setText(user);


        Picasso.with(context).load(url).into(imglogo);
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
