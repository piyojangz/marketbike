package com.marketbike.app;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class News_detail extends Activity {
    private String title;
    private WebView webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        this.title = this.getIntent().getCharSequenceExtra(ListItem.KEY_TITLE).toString();
        CharSequence id = this.getIntent().getCharSequenceExtra(ListItem.KEY_ID);
        setTitle(this.title);
        webview = (WebView) findViewById(R.id.news_detail);
        webview.loadUrl("http://marketbike.zoaish.com/api/get_content/" + id);


        AdView adView = (AdView) this.findViewById(R.id.adView_detail);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(Integer.parseInt(id.toString()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.content, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                finish();
                return true;
            case R.id.action_share:
                String shareBody = this.title;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, this.title);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.abc_action_bar_home_description)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}