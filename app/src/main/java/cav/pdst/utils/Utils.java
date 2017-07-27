package cav.pdst.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cav.pdst.data.models.AbonementModel;

public class Utils {
    public static AbonementModel getConvertModel (int sp_id, int id, String createDate,
                                            String startDate, String endDate,
                                            int countTr, float pay, String comment) {
        SimpleDateFormat format = new SimpleDateFormat("E dd.MM.yyyy");
        try {
            return new AbonementModel(-1,id,sp_id,format.parse(createDate),format.parse(startDate),
                    format.parse(endDate),countTr,pay,0,comment,0);
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
}

