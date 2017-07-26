package cav.pdst.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cav.pdst.data.models.AbonementModel;
import cav.pdst.data.models.GroupModel;
import cav.pdst.data.models.LinkSpABTrModel;
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.data.models.TrainingModel;


public class DataBaseConnector {
    private SQLiteDatabase database;
    private DBHelper mDBHelper;

    public DataBaseConnector(Context context){
        mDBHelper = new DBHelper(context,DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION);
    }

    public void open(){
        database = mDBHelper.getWritableDatabase();
    }
    public void close(){
        if (database!=null) {
            database.close();
        }
    }

    // Group
    public void addGroup(GroupModel model,Integer[] selectItem){
        open();
        ContentValues value = new ContentValues();
        value.put("group_name",model.getName());
        value.put("count_item",model.getCount());
        int recid = (int) database.insert(DBHelper.GROUP_TABLE,null,value);
        for (int i=0;i<selectItem.length;i++) {
            value.clear();
            value.put("type_ref", 0);
            value.put("id1", selectItem[i]);
            value.put("id2",recid);
            database.insert(DBHelper.REF_TABLE,null,value);
        }
        close();
    }

    public void delGroup(int id){
        open();
        database.delete(DBHelper.GROUP_TABLE,"_id="+id,null);
        database.delete(DBHelper.REF_TABLE,"type_ref=0 and id2="+id,null);
        close();
    }

    public Cursor groupAll(){
        return database.query(DBHelper.GROUP_TABLE,new String[]{"_id","group_name","count_item"},null,null,null,null,"group_name");
    }

    public void updateGroup(GroupModel model,Integer[] selectItem){
        open();
        ContentValues values = new ContentValues();
        values.put("group_name",model.getName());
        values.put("count_item",model.getCount());
        database.update(DBHelper.GROUP_TABLE,values,"_id="+model.getId(),null);
        database.delete(DBHelper.REF_TABLE,"type_ref=0 and id2="+model.getId(),null);
        for (int i=0;i<selectItem.length;i++) {
            values.clear();
            values.put("type_ref", 0);
            values.put("id1", selectItem[i]);
            values.put("id2",model.getId());
            database.insert(DBHelper.REF_TABLE,null,values);
        }
        close();
    }

    public Cursor getSportsmanInGroup(){
        String sql="select sp._id,sp.sp_name,gr.group_name,gr._id as gr_id from SPORTSMAN sp\n" +
                "  left join REF_TABLE rf on rf.type_ref=0 and sp._id=rf.id1\n" +
                "  left join GROUP_TABLE gr on rf.id2=gr._id order by sp_name";
        return database.rawQuery(sql,null);
    }

    // training
    public void addTraining(TrainingModel data, Integer[] selectItem, ArrayList<LinkSpABTrModel> spAB){
        open();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues value = new ContentValues();
        value.put("training_name",data.getName());
        value.put("count_item",data.getCount());
        value.put("date",format.format(data.getDate()));
        value.put("time",data.getTime());
        //value.put("",data.getType());
        int recid = (int) database.insert(DBHelper.TRAINING_TABLE,null,value);
        for (int i=0;i<selectItem.length;i++) {
            value.clear();
            value.put("type_ref", 1);
            value.put("id1", selectItem[i]);
            value.put("id2",recid);
            database.insert(DBHelper.REF_TABLE,null,value);
        }
        for (int i=0;i<spAB.size();i++){
            value.clear();
            value.put("type_ref",2);
            value.put("id1",recid);
            value.put("id2",spAB.get(i).getAbonement());
            database.insertWithOnConflict(DBHelper.REF_TABLE,null,value,SQLiteDatabase.CONFLICT_REPLACE);
            String sql="update "+DBHelper.ABONEMENT_TABLE+" set used_training=used_training+1 "+
                    "where _id="+spAB.get(i).getAbonement();
            database.execSQL(sql);
        }
       close();
    }

    public void updateTraining(TrainingModel data,Integer[] selectItem){
        open();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues value = new ContentValues();
        value.put("training_name",data.getName());
        value.put("count_item",data.getCount());
        value.put("date",format.format(data.getDate()));
        value.put("time",data.getTime());
        database.update(DBHelper.TRAINING_TABLE,value,"_id="+data.getId(),null);
        database.delete(DBHelper.REF_TABLE,"type_ref=1 and id2="+data.getId(),null);
        for (int i=0;i<selectItem.length;i++) {
            value.clear();
            value.put("type_ref", 1);
            value.put("id1", selectItem[i]);
            value.put("id2",data.getId());
            database.insert(DBHelper.REF_TABLE,null,value);
        }
        close();
    }

    //TODO Перенести в тригер
    public void delTraining(int id){
        String sql;
        open();
        database.delete(DBHelper.TRAINING_TABLE,"_id="+id,null);
        Cursor cursor = database.query(DBHelper.REF_TABLE,new String[]{"id2"},"type_ref=2 and id1="+id,null,null,null,null);
        while (cursor.moveToNext()){
            sql="update "+DBHelper.ABONEMENT_TABLE+" set used_training=used_training-1 "+
                    "where _id="+cursor.getInt(0);
            database.execSQL(sql);
        }
        database.delete(DBHelper.REF_TABLE,"type_ref=2 and id1="+id,null);
        database.delete(DBHelper.REF_TABLE,"type_ref=1 and id2="+id,null);
        close();
    }

    public Cursor getSPTraining(){
        /*
        String sql="select sp._id,sp.sp_name,a.ci from SPORTSMAN sp\n" +
                "   left join (select id1, count(1) as ci from REF_TABLE where type_ref=1\n" +
                "   group by id1) as a on sp._id=a.id1 order by sp.sp_name";
        */
        String sql="select sp._id,sp.sp_name,a.ci from SPORTSMAN sp "+
                   " left join (select sp_id,sum(count_training-used_training) as ci from ABONEMENT group by sp_id) as a on sp._id= a.sp_id order by sp.sp_name";
        return database.rawQuery(sql,null);
    }

    public Cursor getTraining(String selectDate){
        return database.query(DBHelper.TRAINING_TABLE,new String[]{"_id","training_name","count_item","date","time"},"date='"+selectDate+"'",null,null,null,"training_name");
    }

    public Cursor getTraining(int sp_id){
        /*
        String sql="select _id,training_name,count_item,date,time from TRAINIG_TABLE tt\n" +
                "   join REF_TABLE rf on rf.type_ref=1 and tt._id=rf.id2\n" +
                "   where rf.id1="+sp_id;
        */
        String sql="select tt._id,tt.training_name,tt.count_item,tt.date,tt.time,ab.pos_id as abid from REF_TABLE  rf\n" +
                "  join TRAINIG_TABLE tt on rf.id2=tt._id\n" +
                "  join REF_TABLE rf2 on rf2.type_ref=2 and tt._id=rf2.id1\n" +
                "  join ABONEMENT ab on rf2.id2=ab._id and ab.sp_id=" +sp_id+
                " where  rf.type_ref=1 and rf.id1="+sp_id;
        return database.rawQuery(sql,null);
    }
    //TODO возможно следует передавать дату
    public Cursor getDateTraining(){
        String sql="select distinct date from TRAINIG_TABLE order by date;";
        return database.rawQuery(sql,null);
    }

    // sportsman
    public void addSportsman(SportsmanModel data){
        open();
        ContentValues value = new ContentValues();
        value.put("sp_name",data.getName());
        value.put("phone",data.getTel());
        value.put("comment",data.getComment());
        database.insert(DBHelper.SPORTSMAN_TABLE,null,value);
        close();
    }

    public void updateSportsman(SportsmanModel data){
        open();
        ContentValues value = new ContentValues();
        value.put("sp_name",data.getName());
        value.put("phone",data.getTel());
        value.put("comment",data.getComment());
        database.update(DBHelper.SPORTSMAN_TABLE,value,"_id="+data.getId(),null);

        close();
    }

    public void delSportsman(int id){
        open();
        database.delete(DBHelper.SPORTSMAN_TABLE,"_id="+id,null);
        close();
    }

    public Cursor getSportsman(){
        String sql="select sp._id,sp.sp_name,sp.phone,sp.comment,a.ci from SPORTSMAN sp\n" +
                "   left join (select id1, count(1) as ci from REF_TABLE where type_ref=1\n" +
                "   group by id1) as a on sp._id=a.id1 order by sp.sp_name";
        return database.rawQuery(sql,null);
        //return database.query(DBHelper.SPORTSMAN_TABLE,new String[]{"_id","sp_name","phone","comment"},null,null,null,null,"sp_name");
    }

    // абонемент

    public Cursor getAbonement(int sportsman_id){
        return database.query(DBHelper.ABONEMENT_TABLE,
                new String[]{"sp_id","pos_id","buy_date","start_date","end_date",
                        "type_abonement","pay","count_training","comment","used_training"},
                "sp_id="+sportsman_id,null,null,null,"_id");
    }

    public void delAbonement(int id,int sprotsman_id){
        open();
        database.delete(DBHelper.ABONEMENT_TABLE,"pos_id="+id+" and sp_id"+sprotsman_id,null);
        close();
    }

    public void addUpdateAbonement(AbonementModel model){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        open();
        ContentValues values = new ContentValues();
        values.put("sp_id",model.getSpId());
        values.put("pos_id",model.getId());
        values.put("buy_date",format.format(model.getCreateDate()));
        values.put("start_date",format.format(model.getStartDate()));
        values.put("end_date",format.format(model.getEndDate()));
        values.put("pay",model.getPay());
        values.put("count_training",model.getCountTraining());
        values.put("comment",model.getComment());
        database.insertWithOnConflict(DBHelper.ABONEMENT_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        close();
    }

    public int getLastIndex(int sp_id) {
        int res = 0;
        String sql="select coalesce (max(pos_id) + 1, 0, max(pos_id) + 1)from ABONEMENT where sp_id = "+sp_id;
        open();
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()){
            res = cursor.getInt(0);
        }
        close();
        return res;
    }

    // misc
    public void linkTrainingAbomenet(int sp_id,int ab_id){

    }

    public Cursor getAbonementInDate(int sp_id,String date){
        String sql="select _id from ABONEMENT \n" +
                   " where sp_id="+sp_id+" and start_date<='"+date+"' and end_date>='"+date+"' and (count_training-used_training)<>0"+
                " order by pos_id";

        //System.out.println(sql);
        return database.rawQuery(sql,null);
    }

}
