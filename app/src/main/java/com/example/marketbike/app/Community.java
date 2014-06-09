package com.example.marketbike.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Created by Breeshy on 08/06/2014.
 */
public class Community extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_main);


    }
}

class ProgressTask extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }
}

