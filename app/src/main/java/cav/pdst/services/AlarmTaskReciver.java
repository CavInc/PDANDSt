package cav.pdst.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import cav.pdst.utils.ConstantManager;

public class AlarmTaskReciver extends BroadcastReceiver {

    private String mMsg;

    public AlarmTaskReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        mMsg = intent.getStringExtra(ConstantManager.ALARM_MSG);
        showNotification(context);
    }

    private void showNotification(Context context){

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = null;

        Notification.Builder builder = new Notification.Builder(context);

        Intent intent = new Intent();
        PendingIntent pi = PendingIntent.getService(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setContentIntent(pi)
                .setTicker("Задолженность")
                .setWhen(System.currentTimeMillis())
                .setContentText(mMsg)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < 16){
            notification = builder.getNotification(); // до API 16
        }else{
            notification = builder.build();
        }


        notificationManager.notify(ConstantManager.NOTIFY_ID, notification);
    }
}
