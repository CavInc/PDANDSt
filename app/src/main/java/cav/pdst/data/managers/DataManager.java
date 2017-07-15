package cav.pdst.data.managers;


import android.content.Context;

import cav.pdst.data.database.DataBaseConnector;
import cav.pdst.data.models.GroupModel;
import cav.pdst.utils.PdStApplication;

public class DataManager {
    private static DataManager INSTANCE = null;

    private Context mContext;
    private DataBaseConnector mDB;

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
    public void addGroup(GroupModel data){
        mDB.addGroup(data);
    }

}
