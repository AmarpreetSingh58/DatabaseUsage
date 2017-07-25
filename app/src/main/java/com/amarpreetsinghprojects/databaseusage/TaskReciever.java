package com.amarpreetsinghprojects.databaseusage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

public class TaskReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent recieverIntent = PendingIntent.getActivity(context,123,new Intent(context,MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT );
        Notification notificationCompat = new NotificationCompat.Builder(context)
                .setContentTitle(intent.getStringExtra("taskTitle"))
                .setContentText("Notification")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build();
        nm.notify(100,notificationCompat);
    }
}
