package com.marketbike.app.helper;

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
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.marketbike.app.custom.ListItem;
import com.marketbike.app.MainActivity;
import com.marketbike.app.R;

/**
 * Created by Breeshy on 8/19/14 AD.
 */
public class GcmMessagerHandler extends IntentService {

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
        String[] recievedata = extras.getString("message").toString().split("\\|");
        Integer type = Integer.parseInt(recievedata[0].trim().toString()); // 0 == content , 1 == reqfriend
        Integer id = Integer.parseInt(recievedata[1].trim().toString());
        String title = recievedata[2];
        String msg = recievedata[3];

        String obj1 = recievedata[4];
        String obj2 = recievedata[5];

        //Log.i("GCM", "Received : (" +messageType+")  "+ id);
        switch (type) {
            case 0:
                notice(type, "New massage from " + "Marketbike", title, id, obj1, obj2, this);
                break;
            default:
                notice(type, title, msg, id, obj1, obj2, this);
                break;
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    public void showToast() {
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_LONG).show();
            }
        });

    }

    private static void notice(Integer type, String msgfrom, String msg, int id, String obj1, String obj2, Context Context) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotificationManager;
        Intent resultIntent;
        TaskStackBuilder stackBuilder;
        PendingIntent resultPendingIntent;

        mBuilder = new NotificationCompat.Builder(Context)
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(msgfrom)
                .setSound(alarmSound)
                .setContentText(msg);


        resultIntent = new Intent(Context, MainActivity.class);
        resultIntent.putExtra(ListItem.KEY_URL, "");
        resultIntent.putExtra(ListItem.KEY_TITLE, msg);
        resultIntent.putExtra(ListItem.KEY_TYPE, String.valueOf(type));
        resultIntent.putExtra(ListItem.KEY_ID, String.valueOf(id));


        stackBuilder = TaskStackBuilder.create(Context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        resultPendingIntent =
                stackBuilder.getPendingIntent(
                        id,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) Context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(id, mBuilder.build());


    }
}