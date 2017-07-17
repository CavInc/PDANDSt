package cav.pdst.data.managers;


import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cav.pdst.data.database.DataBaseConnector;
import cav.pdst.data.models.GroupModel;
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.data.models.TrainingModel;
import cav.pdst.utils.ConstantManager;
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
        mDB = new DataBaseConnector(mContext);
    }

    //region ======== databse ==============
    public void addGroup(GroupModel data){
        mDB.addGroup(data);
    }

    public ArrayList<GroupModel> getGroup(){
        ArrayList<GroupModel> rec = new ArrayList<>();
        mDB.open();
        Cursor cursor = mDB.groupAll();
        while (cursor.moveToNext()){
            rec.add(new GroupModel(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("group_name")),
                    cursor.getInt(cursor.getColumnIndex("count_item"))));
        }
        mDB.close();
        return  rec;
    }

    public void delGrop(int id ){
        mDB.delGroup(id);
    }

    public ArrayList<SportsmanModel> getSportsman(){
        ArrayList<SportsmanModel> rec = new ArrayList<>();
        mDB.open();
        Cursor cursor = mDB.getSportsman();
        while (cursor.moveToNext()){
            rec.add(new SportsmanModel(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("sp_name")),
                    cursor.getString(cursor.getColumnIndex("phone")),
                    0,0,
                    cursor.getString(cursor.getColumnIndex("comment"))));
        }
        mDB.close();
        return rec;
    }

    public void addSportsman(SportsmanModel data){
        mDB.addSportsman(data);
    }

    public void delSportsman(int id){
        mDB.delSportsman(id);
    }


    public void addTraining(TrainingModel data){
        mDB.addTraining(data);
    }

    public ArrayList<TrainingModel> getTraining(){
        ArrayList<TrainingModel> rec = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mDB.open();
        Cursor cursor = mDB.getTraining();
        while (cursor.moveToNext()){
            int type_rec = ConstantManager.ONE;
            if (cursor.getInt(cursor.getColumnIndex("count_item"))>1) {
                type_rec = ConstantManager.GROUP;
            } else {
                type_rec = ConstantManager.ONE;
            }
            try {
                rec.add(new TrainingModel(cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("training_name")),type_rec,
                        cursor.getInt(cursor.getColumnIndex("count_item")),
                        format.parse(cursor.getString(cursor.getColumnIndex("date"))),
                        cursor.getString(cursor.getColumnIndex("time"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mDB.close();
        return rec;
    }

}
