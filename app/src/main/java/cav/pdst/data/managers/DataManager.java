package cav.pdst.data.managers;


import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

import cav.pdst.data.database.DataBaseConnector;
import cav.pdst.data.models.AbonementModel;
import cav.pdst.data.models.GroupModel;
import cav.pdst.data.models.ItemSportsmanModel;
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.data.models.SportsmanTrainingModel;
import cav.pdst.data.models.TrainingModel;
import cav.pdst.utils.ConstantManager;
import cav.pdst.utils.PdStApplication;

public class DataManager {
    private static final String TAG = "DM";
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
    public void addGroup(GroupModel data, Integer[] selectItem){
        mDB.addGroup(data,selectItem);
        for (int i=0;i<selectItem.length;i++){
           Log.d(TAG, String.valueOf(i));
        }
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

    public void updateGroup(GroupModel data,Integer[] selectItem){
        mDB.updateGroup(data,selectItem);
    }

    public ArrayList<ItemSportsmanModel> getSportsmanInGroup(int group_id){
        ArrayList<ItemSportsmanModel> rec = new ArrayList<>();
        //TODO переделать на SparseArray
        HashMap<Integer,ItemSportsmanModel> map_rec = new HashMap<>();
        //ArrayMap<Integer,ItemSportsmanModel> map_rec = new ArrayMap<>();
        ItemSportsmanModel md;
        mDB.open();
        Cursor cursor = mDB.getSportsmanInGroup();
        boolean flg;
        while (cursor.moveToNext()){
            if (cursor.getString(cursor.getColumnIndex("group_name"))!=null && cursor.getInt(cursor.getColumnIndex("gr_id"))==group_id) {
                flg=true;
            } else {
                flg=false;
            }
            if (map_rec.containsKey(cursor.getInt(cursor.getColumnIndex("_id")))){
                map_rec.get(cursor.getInt(cursor.getColumnIndex("_id")))
                        .setGroup(map_rec.get(cursor.getInt(cursor.getColumnIndex("_id"))).getGroup() +"," +cursor.getString(cursor.getColumnIndex("group_name")));
            }else{
                md = new ItemSportsmanModel(cursor.getInt(cursor.getColumnIndex("_id")), flg,
                        cursor.getString(cursor.getColumnIndex("sp_name")), cursor.getString(cursor.getColumnIndex("group_name")));
                map_rec.put(cursor.getInt(cursor.getColumnIndex("_id")),md);
            }
            /*
            rec.add(new ItemSportsmanModel(cursor.getInt(cursor.getColumnIndex("_id")),
                    flg,cursor.getString(cursor.getColumnIndex("sp_name")),
                    0,
                    cursor.getString(cursor.getColumnIndex("group_name"))));
            */
        }

        for (ItemSportsmanModel m:map_rec.values()){
            rec.add(m);
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

    public void updateSportsman(SportsmanModel data){
        mDB.updateSportsman(data);
    }

    public void delSportsman(int id){
        mDB.delSportsman(id);
    }


    public void addTraining(TrainingModel data,Integer[] selectItem){
        mDB.addTraining(data,selectItem);
    }

    public void updateTraining(TrainingModel data,Integer[] selectItem){
        mDB.updateTraining(data,selectItem);
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
    public ArrayList<TrainingModel> getTraining(int sp_id){
        ArrayList<TrainingModel> rec = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mDB.open();
        Cursor cursor = mDB.getTraining(sp_id);
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

    public ArrayList<SportsmanTrainingModel> getSpTraining(){
        ArrayList<SportsmanTrainingModel> rec = new ArrayList<>();
        mDB.open();
        Cursor cursor = mDB.getSPTraining();
        while (cursor.moveToNext()){
            rec.add(new SportsmanTrainingModel(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("sp_name")),
                    cursor.getInt(cursor.getColumnIndex("ci"))));
        }
        mDB.close();
        return  rec;
    }

    public void delTraining(int id){
        mDB.delTraining(id);
    }

    // abonement
    public ArrayList<AbonementModel> getAbonement(int sprotsman_id){
        ArrayList<AbonementModel> rec = new ArrayList<>();
        mDB.open();
        Cursor cursor = mDB.getAbonement(sprotsman_id);

        mDB.close();
        return rec;
    }

    public void addAbonement(AbonementModel data){

    }
    public void delAbonememet(int id,int sprotsman_id){
        mDB.delAbonement(id,sprotsman_id);
    }


}
