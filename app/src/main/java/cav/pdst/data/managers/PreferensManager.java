package cav.pdst.data.managers;


import android.content.SharedPreferences;

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


}
