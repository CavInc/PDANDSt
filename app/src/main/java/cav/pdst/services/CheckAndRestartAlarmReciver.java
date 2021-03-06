package cav.pdst.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.ArrayList;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.AlarmAbonementModel;
import cav.pdst.ui.activity.SportsmanDetailActivity;
import cav.pdst.utils.ConstantManager;
import cav.pdst.utils.Utils;

public class CheckAndRestartAlarmReciver extends BroadcastReceiver {
    private DataManager mDataManager;

    private ArrayList<AlarmAbonementModel> model;
    private Context mContext;

    public CheckAndRestartAlarmReciver() {
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
       // throw new UnsupportedOperationException("Not yet implemented");
        mContext = context;
        model = mDataManager.getFutureCloseAbonement();
        // ставим нотификаторы в соотвествии с тем что вернули в модели
        for (AlarmAbonementModel l :model){
            showNotification(context,l);
        }
        if (model.size()!=0){
            mDataManager.getPreferensManager().setFirstStart(true);
        }
        // запускаем будильник на следующий день
        Utils.restartAlarm(context,1);
    }



    private void showNotification(Context context,AlarmAbonementModel model){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String rington = pref.getString("toast_ringtone","");

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;

        Notification.Builder builder = new Notification.Builder(context);
        //Intent intent = new Intent();

        Intent intent = new Intent(mContext, SportsmanDetailActivity.class);
        intent.putExtra(ConstantManager.MODE_SP_DETAIL,ConstantManager.ALARM_SPORTSMAN);
        intent.putExtra(ConstantManager.ALARM_ID,model.getSpId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pi = PendingIntent.getActivity(context,model.getId(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pi)
                .setSmallIcon(R.drawable.icon_alarm_p)
                .setTicker("Предупреждение !!!")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Предупреждение")
                .setContentText("У спортсмена - "+model.getSportsmanName()+
                        " заканчивается абонемент № "+model.getPostId()+"- "+model.getDate())
                .setOngoing(true)
                //.setDefaults(Notification.DEFAULT_SOUND)
                .setSound(Uri.parse(rington))
                .setAutoCancel(true);


        if (Build.VERSION.SDK_INT < 16){
            notification = builder.getNotification(); // до API 16
        }else{
            //notification = builder.build();
            notification = new Notification.BigTextStyle(builder).bigText("У спортсмена - "+model.getSportsmanName()+
                    " заканчивается абонемент № "+model.getPostId()+"- "+model.getDate()).build();
        }


        notificationManager.notify(ConstantManager.NOTIFY_ID_F+model.getId(), notification);
    }
}
