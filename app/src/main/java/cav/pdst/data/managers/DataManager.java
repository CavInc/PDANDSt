package cav.pdst.data.managers;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cav.pdst.data.database.DataBaseConnector;
import cav.pdst.data.models.AbonementModel;
import cav.pdst.data.models.AlarmAbonementModel;
import cav.pdst.data.models.GroupModel;
import cav.pdst.data.models.ItemSportsmanModel;
import cav.pdst.data.models.RateTypeSpinerModel;
import cav.pdst.data.models.SpRefAbModeModel;
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.data.models.SportsmanTrainingModel;
import cav.pdst.data.models.TrainingGroupModel;
import cav.pdst.data.models.TrainingModel;
import cav.pdst.data.models.UsedDaysModel;
import cav.pdst.ui.fragments.AddRateDialogFragment;
import cav.pdst.utils.ConstantManager;
import cav.pdst.utils.PdStApplication;

public class DataManager {
    private static final String TAG = "DM";
    private static DataManager INSTANCE = null;

    private Context mContext;
    private PreferensManager mPreferensManager;

    private DataBaseConnector mDB;

    public static DataManager getInstance() {
        if (INSTANCE==null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public DataManager(){
        this.mPreferensManager = new PreferensManager();
        this.mContext = PdStApplication.getContext();
        mDB = new DataBaseConnector(mContext);
    }

    public PreferensManager getPreferensManager() {
        return mPreferensManager;
    }

    public DataBaseConnector getDB() {
        return mDB;
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

    public ArrayList<TrainingGroupModel> getGroupString(){
        ArrayList<TrainingGroupModel> rec =new ArrayList<>();
        mDB.open();
        Cursor cursor = mDB.groupAll();
        while (cursor.moveToNext()){
            rec.add(new TrainingGroupModel(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("group_name"))));
        }
        mDB.close();
        return rec;
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
                        .setGroup(map_rec.get(cursor.getInt(cursor.getColumnIndex("_id"))).getGroup()
                                + "," + cursor.getString(cursor.getColumnIndex("group_name")));

                if (flg) {
                    map_rec.get(cursor.getInt(cursor.getColumnIndex("_id"))).setCheckItem(true);
                }
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
                    cursor.getInt(cursor.getColumnIndex("ci")),
                    cursor.getInt(cursor.getColumnIndex("sm")),
                    cursor.getString(cursor.getColumnIndex("comment")),
                    cursor.getString(cursor.getColumnIndex("last_date")),
                    cursor.getString(cursor.getColumnIndex("last_time"))));
        }
        mDB.close();
        return rec;
    }

    public SportsmanModel getSportsman(int id){
        SportsmanModel model = null;
        mDB.open();
        Cursor cursor = mDB.getSportsman(id);
        while (cursor.moveToNext()){
            model = new SportsmanModel(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("sp_name")),
                    cursor.getString(cursor.getColumnIndex("phone")),
                    cursor.getInt(cursor.getColumnIndex("ci")),
                    cursor.getInt(cursor.getColumnIndex("sm")),
                    cursor.getString(cursor.getColumnIndex("comment")),
                    cursor.getString(cursor.getColumnIndex("last_date")),
                    cursor.getString(cursor.getColumnIndex("last_time")));
        }
        mDB.close();
        return model;
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


    public void addTraining(TrainingModel data, ArrayList<SpRefAbModeModel> selectItem){
        mDB.addTraining(data,selectItem);
    }

    public void updateTraining(TrainingModel data,ArrayList<SpRefAbModeModel> selectItem){
        mDB.updateTraining(data,selectItem);
    }

    public ArrayList<TrainingModel> getTraining(Date selectedDate){
        ArrayList<TrainingModel> rec = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mDB.open();
        Cursor cursor = mDB.getTraining(format.format(selectedDate));
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
    public ArrayList<TrainingModel> getTraining(int sp_id,Date selectedDate){
        ArrayList<TrainingModel> rec = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mDB.open();
        Cursor cursor = mDB.getTraining(sp_id,format.format(selectedDate));
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
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getInt(cursor.getColumnIndex("abid")),
                        cursor.getInt(cursor.getColumnIndex("type_link"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mDB.close();
        return rec;
    }

    public ArrayList<Date> getTrainingDay(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Date> rec= new ArrayList<>();
        mDB.open();
        Cursor cursor = mDB.getDateTraining();
        while (cursor.moveToNext()){
            try {
                rec.add(format.parse(cursor.getString(0)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mDB.close();
        return rec;
    }

    public ArrayList<UsedDaysModel> getTrainingDay(int sp_id){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<UsedDaysModel> rec= new ArrayList<>();
        //mDB.openRead();
        mDB.open();
        Log.d(TAG," STATUS DB "+String.valueOf(mDB.isOpen()));
        Cursor cursor = mDB.getDateTraining(sp_id);
        while (cursor.moveToNext()){
            try {
                rec.add(new UsedDaysModel(format.parse(cursor.getString(0)),cursor.getInt(1)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mDB.close();
        return rec;
    }


    public ArrayList<SportsmanTrainingModel> getSpTraining(int group_id){
        ArrayList<SportsmanTrainingModel> rec = new ArrayList<>();
        mDB.open();
        Cursor cursor = mDB.getSPTraining(group_id);
        while (cursor.moveToNext()){
            rec.add(new SportsmanTrainingModel(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("sp_name")),
                    cursor.getInt(cursor.getColumnIndex("ci"))));
        }
        mDB.close();
        return  rec;
    }
    public ArrayList<SportsmanTrainingModel> getSpTraining(int group_id,int training_id){
        ArrayList<SportsmanTrainingModel> rec = new ArrayList<>();
        mDB.open();
        Cursor cursor = mDB.getSPTraining(group_id, training_id);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("sp_name"));
            int ci = cursor.getInt(cursor.getColumnIndex("ci"));
            int ab = cursor.getInt(cursor.getColumnIndex("ab"));
            int tl = cursor.getInt(cursor.getColumnIndex("type_link"));
            rec.add(new SportsmanTrainingModel(id,name,ci,tl,ab));
        }
        mDB.close();
        return rec;
    }

    public void delTraining(int id){
        mDB.delTraining(id);
    }

    // abonement
    public ArrayList<AbonementModel> getAbonement(int sprotsman_id){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<AbonementModel> rec = new ArrayList<>();
        mDB.open();
        Cursor cursor = mDB.getAbonement(sprotsman_id);
        while (cursor.moveToNext()){
            try {
                Date ddebit = null;
                String db= cursor.getString(cursor.getColumnIndex("alarm_date"));
                if (db != null){
                    ddebit = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(db);
                }
                rec.add(new AbonementModel (cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getInt(cursor.getColumnIndex("pos_id")),
                        cursor.getInt(cursor.getColumnIndex("sp_id")),
                        format.parse(cursor.getString(cursor.getColumnIndex("buy_date"))),
                        format.parse(cursor.getString(cursor.getColumnIndex("start_date"))),
                        format.parse(cursor.getString(cursor.getColumnIndex("end_date"))),
                        cursor.getInt(cursor.getColumnIndex("count_training")),
                        cursor.getFloat(cursor.getColumnIndex("pay")),
                        cursor.getInt(cursor.getColumnIndex("type_abonement")),
                        cursor.getString(cursor.getColumnIndex("comment")),
                        cursor.getInt(cursor.getColumnIndex("used_training")),
                        cursor.getInt(cursor.getColumnIndex("working")),
                        cursor.getInt(cursor.getColumnIndex("warning_count")),
                        cursor.getFloat(cursor.getColumnIndex("debt")),ddebit));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mDB.close();
        return rec;
    }

    public void addAbonement(AbonementModel data){

    }
    public void delAbonememet(int id,int sprotsman_id){
        mDB.delAbonement(id,sprotsman_id);
    }

    public void addUpdateAbonement(AbonementModel model) {
        mDB.addUpdateAbonement(model);
    }

    // возвращаем доход и расход в диапазоне дат
    public ArrayList<Float> getReportAll(Date start, Date end){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Float> rec = new ArrayList<>();
        mDB.open();
        Cursor cursor =mDB.getDohod(format.format(start),format.format(end));
        while (cursor.moveToNext()){
            rec.add(0,cursor.getFloat(0));
        }
        cursor = mDB.getRateAll(format.format(start),format.format(end));
        while (cursor.moveToNext()){
            rec.add(1,cursor.getFloat(0));
        }
        mDB.close();
        return rec;
    }


    public ArrayList<RateTypeSpinerModel> getRateType() {
        ArrayList<RateTypeSpinerModel> rec = new ArrayList<>();
        mDB.open();
        Cursor cursor = mDB.getRateType();
        while (cursor.moveToNext()){
            rec.add(new RateTypeSpinerModel(cursor.getInt(0),cursor.getString(1)));
        }
        mDB.close();
        return rec;
    }

    public ArrayList<AlarmAbonementModel> getFutureCloseAbonement(){
        ArrayList<AlarmAbonementModel> rec = new ArrayList<>();
        //Calendar c = Calendar.getInstance();
       // c.set(2017,7,23);
        mDB.open();
        Cursor cursor = mDB.getCloseNotUsedAbonement(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        while (cursor.moveToNext()){
            rec.add(new AlarmAbonementModel(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getInt(cursor.getColumnIndex("sp_id")),
                    cursor.getInt(cursor.getColumnIndex("pos_id")),
                    cursor.getString(cursor.getColumnIndex("end_date")),
                    cursor.getString(cursor.getColumnIndex("sp_name"))));

        }
        mDB.close();
        return rec;
    }
}
