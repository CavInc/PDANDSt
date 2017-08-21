package cav.pdst.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class CheckAndRestartAlarmReciver extends BroadcastReceiver {
    public CheckAndRestartAlarmReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // перезапускает будильник на следующий день
    private void restartAlarm(Context context){
        Calendar c = Calendar.getInstance();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,CheckAndRestartAlarmReciver.class);
        PendingIntent pi= PendingIntent.getBroadcast(context,0, intent,0);
        am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi);
    }
}
