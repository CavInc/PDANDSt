package cav.pdst.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import cav.pdst.R;
import cav.pdst.ui.activity.SportsmanDetailActivity;
import cav.pdst.utils.ConstantManager;

public class AlarmTaskReciver extends BroadcastReceiver {

    private String mMsg;
    private int mId; // id спортсмена
    private Context mContext;

    public AlarmTaskReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        mContext = context;
        Log.d("ATR","ESH");
        mMsg = intent.getStringExtra(ConstantManager.ALARM_MSG);
        mId = intent.getIntExtra(ConstantManager.ALARM_ID,0);
        showNotification(context);
    }

    private void showNotification(Context context){

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = null;

        Notification.Builder builder = new Notification.Builder(context);

        Intent intent = new Intent(mContext, SportsmanDetailActivity.class);
        intent.putExtra(ConstantManager.MODE_SP_DETAIL,ConstantManager.ALARM_SPORTSMAN);
        intent.putExtra(ConstantManager.ALARM_ID,mId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getService(context,mId,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setContentIntent(pi)
                .setSmallIcon(R.drawable.icon_alarm_p)
                .setTicker("Задолженность")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Задолженность")
                .setContentText(mMsg)
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < 16){
            notification = builder.getNotification(); // до API 16
        }else{
            notification = builder.build();
        }


        notificationManager.notify(ConstantManager.NOTIFY_ID+mId, notification);
    }
}
