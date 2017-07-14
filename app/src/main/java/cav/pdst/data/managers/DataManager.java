package cav.pdst.data.managers;


import android.content.Context;

import cav.pdst.utils.PdStApplication;

public class DataManager {
    private static DataManager INSTANCE = null;

    private Context mContext;

    public static DataManager getInstance() {
        if (INSTANCE==null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public DataManager(){
        this.mContext = PdStApplication.getContext();
    }

    //region ======== databse ==============

}
