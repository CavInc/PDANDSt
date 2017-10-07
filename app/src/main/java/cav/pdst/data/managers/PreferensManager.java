package cav.pdst.data.managers;


import android.content.SharedPreferences;

import cav.pdst.utils.ConstantManager;
import cav.pdst.utils.PdStApplication;

public class PreferensManager {

    private static final String STORE_SPORTSMAN = "STORE_SPORTSMAN";

    private SharedPreferences mSharedPreferences;

    public PreferensManager() {
        mSharedPreferences = PdStApplication.getSharedPreferences();
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    // сохраняем ссылку на выбранного спортсмена
    public void saveUseSportsman(int id){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(STORE_SPORTSMAN,id);
        editor.apply();
    }
    // устанавливаем флаг отсутствия звонилки
    public void setNoPhoneGrand(boolean granted){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(ConstantManager.GRANTED_PHONE,granted);
        editor.apply();
    }

    public boolean getPhoneGrand(){
        return mSharedPreferences.getBoolean(ConstantManager.GRANTED_PHONE,false);
    }

    // проверяем запустили ли мы уже сегодня уведомления
    // при переустановки времени в конфиги флаг сбрасывается
    public void setFirstStart(boolean start){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(ConstantManager.STORE_FIRST_START_ALARM,start);
        editor.apply();
    }

    public boolean isFirstStart(){
        return mSharedPreferences.getBoolean(ConstantManager.STORE_FIRST_START_ALARM,false);
    }

    public void setDateEndingReport(String date, String mode){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(mode,date);
        editor.apply();
    }

    public String getDateEndingReport(String mode){
        return mSharedPreferences.getString(mode,null);
    }




}
