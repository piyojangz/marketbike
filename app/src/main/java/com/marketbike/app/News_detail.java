package com.marketbike.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.marketbike.app.MenuAdapter;
import com.marketbike.app.R;

import java.util.ArrayList;
import java.util.HashMap;

public class News_detail extends Activity {

    private ArrayList<HashMap<String, String>> DataList;
    HashMap map;
    private MenuAdapter menuAdpt;
    private ListView lv;
    private ArrayAdapter<String> listAdapter;
    protected ArrayList<HashMap<String, String>> sList;
    private CharSequence mTitle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);

        setTitle(this.getIntent().getCharSequenceExtra(ListItem.KEY_TITLE));
        WebView webview = (WebView) findViewById(R.id.news_detail);
       // String summary = "<html><body>You scored <b>192</b> points.</body></html>";
        //webview.loadData(summary, "text/html", null);
        webview.loadUrl("http://m.trueyou.net/privilege/3038082/3103990");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conversation, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}