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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Breeshy on 08/06/2014.
 */


public class MenuAdapter extends BaseAdapter implements Transformation {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private static final String TAG = "MyActivity";
    private Typeface typeFace;

    public MenuAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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

        convertView = inflater.inflate(R.layout.menu_list, parent, false);
        Typeface typeFace = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        final ViewGroup mContainer = (ViewGroup) convertView.getRootView();
        setAppFont.setAppFont(mContainer, typeFace);
        TextView txt_title = (TextView) convertView.findViewById(R.id.title);
        ImageView imglogo = (ImageView) convertView.findViewById(R.id.imglogo);
        HashMap<String, String> smart = new HashMap<String, String>();
        Context context = this.activity;
        smart = this.data.get(position);
        String title = smart.get(ListItem.KEY_MENU_TITLE).toString();
        String url = smart.get(ListItem.KEY_MENU_LOGO).toString();
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
