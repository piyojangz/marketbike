package com.marketbike.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marketbike.app.custom.setAppFont;
import com.marketbike.app.helper.JsonHelper;
import com.marketbike.app.helper.ScrollViewExt;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Product_detail extends Activity implements Transformation, ScrollViewExt.ScrollViewListener {
    private String id;
    private HashMap map;
    private AsyncTask<Void, Void, Void> task;
    private String title, headline, thumbnail, userimg, price, user, description, image1, image2, image3, image4, image5,Images;
    private Menu optionsMenu;
    protected ArrayList<HashMap<String, String>> sList;
    private ScrollViewExt scroll_detail;
    private RelativeLayout frame_thumnail;
    private AbsoluteLayout wrap_item;
    private Boolean is_view_gone = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        final ViewGroup mContainer = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setAppFont.setAppFont(mContainer, typeFace);
        this.title = this.getIntent().getCharSequenceExtra(ListItem.KEY_TITLE).toString();
        id = this.getIntent().getCharSequenceExtra(ListItem.KEY_ID).toString();
        setTitle(this.title);
        this.frame_thumnail = (RelativeLayout) this.findViewById(R.id.frame_thumnail);
        this.scroll_detail = (ScrollViewExt) this.findViewById(R.id.scroll_detail);
        this.scroll_detail.setSmoothScrollingEnabled(true);
        this.wrap_item = (AbsoluteLayout) this.findViewById(R.id.wrap_item);
        this.scroll_detail.setScrollViewListener(this);

    }

    private void firstload() {
        this.task = new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setRefreshActionButtonState(true);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    getDetail(id);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                setDetail();
                setRefreshActionButtonState(false);
            }

        };
        this.task.execute((Void[]) null);
    }


    private void setDetail() {
        ImageView img_thumnail = (ImageView) this.findViewById(R.id.img_thumnail);
        ImageView image_profile = (ImageView) this.findViewById(R.id.image_profile);
        TextView txt_headeline = (TextView) this.findViewById(R.id.txt_headeline);
        TextView txt_price = (TextView) this.findViewById(R.id.txt_price);
        TextView txt_userfullname = (TextView) this.findViewById(R.id.txt_userfullname);
        TextView txt_description = (TextView) this.findViewById(R.id.txt_description);


        txt_description.setText(this.description);
        txt_price.setText(this.price + "฿");
        txt_headeline.setText(this.headline);
        txt_userfullname.setText(this.user);


        ImageView img1 = (ImageView) this.findViewById(R.id.img_1);
        ImageView img2 = (ImageView) this.findViewById(R.id.img_2);
        ImageView img3 = (ImageView) this.findViewById(R.id.img_3);
        ImageView img4 = (ImageView) this.findViewById(R.id.img_4);
        ImageView img5 = (ImageView) this.findViewById(R.id.img_5);


        Picasso.with(this).load(this.thumbnail).into(img_thumnail);
        Picasso.with(this).load(this.userimg).transform(this).into(image_profile);

        String[] imgs = this.Images.split(";");
        int i = 1;
        for (String each : imgs) {
            if(i==1) {
                this.image1 = each;
            }
            else if(i==2){
                this.image2 = each;
            }
            else if(i==3){
                this.image3 = each;
            }
            else if(i==4){
                this.image4 = each;
            }
            else if(i==5){
                this.image5 = each;
            }
            i++;
        }


        if (this.image1.isEmpty()) {
            img1.setVisibility(View.GONE);
        } else {
            Picasso.with(this).load(this.image1).into(img1);
        }
        if (this.image2.isEmpty()) {
            img2.setVisibility(View.GONE);
        } else {
            Picasso.with(this).load(this.image2).into(img2);
        }
        if (this.image3.isEmpty()) {
            img3.setVisibility(View.GONE);
        } else {
            Picasso.with(this).load(this.image3).into(img3);
        }
        if (this.image4.isEmpty()) {
            img4.setVisibility(View.GONE);
        } else {
            Picasso.with(this).load(this.image4).into(img4);
        }
        if (this.image5.isEmpty()) {
            img5.setVisibility(View.GONE);
        } else {
            Picasso.with(this).load(this.image5).into(img5);
        }
    }

    private void getDetail(String id) {
        try {
            String url = "http://marketbike.zoaish.com/api/get_product/" + id;
            JSONObject data = JsonHelper.getJson(url).getJSONObject("result");

            this.headline = data.getString("Headline");
            this.price = data.getString("aPrice");
            this.user = data.getString("name");
            this.description = data.getString("Full_Descripption");
            this.thumbnail = data.getString("Thumbnail_Image");
            this.userimg = "https://graph.facebook.com/" + data.getString("fbid") + "/picture?type=large";
            this.image1 = data.getString("Image_1");
            this.image2 = data.getString("Image_2");
            this.image3 = data.getString("Image_3");
            this.image4 = data.getString("Image_4");
            this.image5 = data.getString("Image_5");
            this.Images =  data.getString("Images");

        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news, menu);
        this.firstload();
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
                this.firstload();
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

    @Override
    public String key() {
        return "circle()";
    }


    @Override
    public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
        // We take the last son in the scrollview
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

        RelativeLayout.LayoutParams head_params = (RelativeLayout.LayoutParams) this.frame_thumnail.getLayoutParams();
        head_params.setMargins(0, (y * -1) / 2, 0, 0); //substitute parameters for left, top, right, bottom
        this.frame_thumnail.setLayoutParams(head_params);
        // if diff is zero, then the bottom has been reached
        //Log.d("fb", "y : " + y);
        //Log.d("fb", "oldy : " + oldy);
        //Log.d("fb", "diff : " + diff);
        if (diff > 0) {
            if (y > 500 && y >= oldy && !is_view_gone) {

                is_view_gone = true;
                if (this.wrap_item.INVISIBLE != View.GONE) {


                    Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_from_top);
                    this.wrap_item.startAnimation(anim);
                    this.wrap_item.setAnimation(anim);

                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation arg0) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            wrap_item.setVisibility(View.GONE);
                        }
                    });


                }

            } else if (y < oldy && is_view_gone) {
                Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_from_bottom);
                this.wrap_item.startAnimation(anim);
                this.wrap_item.setAnimation(anim);
                this.wrap_item.setVisibility(View.VISIBLE);
                is_view_gone = false;
            } else {

            }
        }
    }
}
