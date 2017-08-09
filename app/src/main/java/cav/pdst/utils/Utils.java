package cav.pdst.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cav.pdst.data.models.AbonementModel;

public class Utils {
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

    public static boolean isAfterDate2(Date date){
        Date dt = new Date();
        return dt.after(date);
    }
}

