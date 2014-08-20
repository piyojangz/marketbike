package com.marketbike.app.helper;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.marketbike.app.ListItem;
import com.marketbike.app.News_detail;
import com.marketbike.app.R;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;
/**
 * Created by Breeshy on 8/19/14 AD.
 */
public class GcmMessagerHandler  extends IntentService {

    String mes;
    private Handler handler;
    public GcmMessagerHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("title");
       // showToast();
        String[]  recievedata = extras.getString("message").toString().split("\\|");
        Integer id =  Integer.parseInt(recievedata[0].trim().toString()) ;
        String title = recievedata[1];
        String msg = recievedata[2];
        Log.i("GCM", "Received : (" +messageType+")  "+ id);
        notice("New massage from " + "Marketbike", title, id, this);
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
            }
        });

    }
    private static void notice(String msgfrom, String msg,int id, Context Context) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(Context)
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle(msgfrom)
                        .setSound(alarmSound)
                        .setContentText(msg);


        Intent resultIntent = new Intent(Context, News_detail.class);
        resultIntent.putExtra(ListItem.KEY_URL, "");
        resultIntent.putExtra(ListItem.KEY_TITLE, msg);
        resultIntent.putExtra(ListItem.KEY_ID, String.valueOf(id));


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(Context);
        stackBuilder.addParentStack(News_detail.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        id,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =  (NotificationManager) Context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(id, mBuilder.build());

    }
}