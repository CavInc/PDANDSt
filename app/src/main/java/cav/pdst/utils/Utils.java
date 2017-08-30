package cav.pdst.utils;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cav.pdst.data.models.AbonementModel;
import cav.pdst.services.AlarmTaskReciver;
import cav.pdst.services.CheckAndRestartAlarmReciver;

public class Utils {
    private static final String TAG = "UTILS";

    public static AbonementModel getConvertModel (int sp_id, int id, String createDate,
                                                  String startDate, String endDate,
                                                  int countTr, float pay, String comment) {
        SimpleDateFormat format = new SimpleDateFormat("E dd.MM.yyyy");
        try {
            return new AbonementModel(-1,id,sp_id,format.parse(createDate),format.parse(startDate),
                    format.parse(endDate),countTr,pay,0,comment,0,0,0,0,null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Date getSteToDate(String str_date,String mask){
        SimpleDateFormat format = new SimpleDateFormat(mask);

        try {
            return format.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // проверить что дата находится в прошлом месяце
    public static boolean isAfterDate(Date date){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int moth = c.get(Calendar.MONTH);
        c.setTime(date);
        int old_year = c.get(Calendar.YEAR);
        int old_moth = c.get(Calendar.MONTH);
        if (year>old_year) return true;
        if (moth>old_moth) return true;
        return false;
    }
    //TODO что то рещить с времененем
    public static boolean isAfterDate2(Date date){
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),0,0,0);
        c.set(Calendar.MILLISECOND, 0);
        Date dt = c.getTime();
        /*
        if (dt.getTime()>date.getTime()) return true;
        else return false;*/
        return dt.after(date);
    }

    public static void startAlarm(Context context,Date date,String msg,int sp_id){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmTaskReciver.class);
        intent.putExtra(ConstantManager.ALARM_MSG,msg);
        intent.putExtra(ConstantManager.ALARM_ID,sp_id);
        PendingIntent pi= PendingIntent.getBroadcast(context,sp_id, intent,0);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi);

    }

    // перезапускает будильник на следующий день
    public static void restartAlarm(Context context,int day_offset){
        Log.d(TAG,"SETALARM");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,day_offset);
        //c.set(2017,8,23);
        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,8);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,CheckAndRestartAlarmReciver.class);
        PendingIntent pi= PendingIntent.getBroadcast(context,0, intent,0);
        am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi);
    }
}

