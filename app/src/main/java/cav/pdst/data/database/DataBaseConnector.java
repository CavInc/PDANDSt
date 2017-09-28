package cav.pdst.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cav.pdst.data.models.AbonementModel;
import cav.pdst.data.models.GroupModel;
import cav.pdst.data.models.SpRefAbModeModel;
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.data.models.TrainingModel;
import cav.pdst.utils.ConstantManager;


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

    public void openRead(){
        database =mDBHelper.getReadableDatabase();
    }

    public boolean isOpen(){
        return database.isOpen();
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
    public void addTraining(TrainingModel data, ArrayList<SpRefAbModeModel> selectItem){
        open();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues value = new ContentValues();
        value.put("training_name",data.getName());
        value.put("count_item",data.getCount());
        value.put("date",format.format(data.getDate()));
        value.put("time",data.getTime());
        value.put("repeat_training",data.getRepeatType());
        //value.put("",data.getType());
        int recid = (int) database.insert(DBHelper.TRAINING_TABLE,null,value);
        for (int i=0;i<selectItem.size();i++) {
            SpRefAbModeModel mx = selectItem.get(i);
            value.clear();
            value.put("type_ref", 1);
            value.put("id1", mx.getSpId());
            value.put("id2",recid);
            value.put("type_link",mx.getMode());
            database.insert(DBHelper.REF_TABLE,null,value);

            value.clear();
            value.put("type_ref", 2);
            value.put("id1", recid);
            value.put("id2", mx.getAbonement());
            value.put("type_link", mx.getMode());
            database.insertWithOnConflict(DBHelper.REF_TABLE, null, value, SQLiteDatabase.CONFLICT_REPLACE);

            value.clear();
            value.put("type_ref", 2);
            value.put("id1", recid);
            value.put("id2", mx.getAbonement());
            value.put("type_link", mx.getMode());
            database.insertWithOnConflict(DBHelper.REF_TABLE, null, value, SQLiteDatabase.CONFLICT_REPLACE);

            // тренировка и пропуск
            if ((mx.getMode() == 0) || (mx.getMode() == 1) ){
                String sql = "update " + DBHelper.ABONEMENT_TABLE + " set used_training=used_training+1 " +
                        "where _id=" + mx.getAbonement();
                database.execSQL(sql);
            }
            // отработка
            if (mx.getMode() == 3){
                String sql = "update " + DBHelper.ABONEMENT_TABLE + " set used_working=used_working+1 " +
                        "where _id=" + mx.getAbonement();
                database.execSQL(sql);
            }

            // предупреждение
            if (mx.getMode() == 2 ){
                String sql = "update " + DBHelper.ABONEMENT_TABLE + " set warning_count=warning_count+1 " +
                        "where _id=" + mx.getAbonement();
                database.execSQL(sql);
            }
        }
        Cursor cursor = getLastDateTraining(recid);
        while (cursor.moveToNext()){
            value.clear();
            value.put("last_date",cursor.getString(cursor.getColumnIndex("date")));
            value.put("last_time",cursor.getString(cursor.getColumnIndex("time")));
            database.update(DBHelper.SPORTSMAN_TABLE,value,"_id="+cursor.getInt(0),null);
        }

       close();
    }

    public void updateTraining(TrainingModel data,ArrayList<SpRefAbModeModel> selectItem){
        open();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues value = new ContentValues();
        value.put("training_name",data.getName());
        value.put("count_item",data.getCount());
        value.put("date",format.format(data.getDate()));
        value.put("time",data.getTime());
        value.put("repeat_training",data.getRepeatType());
        database.update(DBHelper.TRAINING_TABLE,value,"_id="+data.getId(),null); // обновили данные тренировки
        database.delete(DBHelper.REF_TABLE,"type_ref=1 and id2="+data.getId(),null); // удалили связь спортсмен - тренировка

        //todo сдесь сделать отдачу абонемента обратно
        // откатываем использованные тренировки обратно откатываются тренировки и пропуски.
        String sql ="select id2,type_link from ref_table rf where rf.type_ref=2 and id1="+data.getId();
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()){
            if (cursor.getInt(1) == ConstantManager.SPORTSMAN_MODE_WARNING) {
                sql = "update " + DBHelper.ABONEMENT_TABLE + " set warning_count=warning_count-1 " +
                        "where _id=" + cursor.getString(0);
            } else if (cursor.getInt(1) == ConstantManager.SPORTSMAN_MODE_WORKINGOFF){
                sql = "update " + DBHelper.ABONEMENT_TABLE + " set used_working=used_working-1 " +
                        "where _id=" + cursor.getString(0);
            } else {
                sql = "update " + DBHelper.ABONEMENT_TABLE + " set used_training=used_training-1 " +
                        "where _id=" + cursor.getString(0);
            }
            database.execSQL(sql);
        }

        database.delete(DBHelper.REF_TABLE,"type_ref=2 and id1="+data.getId(),null);// удаляем связь тренировка - абонемент

        for (int i=0;i<selectItem.size();i++) {
            SpRefAbModeModel mx = selectItem.get(i);
            value.clear();
            value.put("type_ref", 1);
            value.put("id1", mx.getSpId());
            value.put("id2",data.getId());
            value.put("type_link",mx.getMode());
            database.insert(DBHelper.REF_TABLE,null,value);

            value.clear();
            value.put("type_ref",2);
            value.put("id1",data.getId());
            value.put("id2",mx.getAbonement());
            value.put("type_link",mx.getMode());
            database.insertWithOnConflict(DBHelper.REF_TABLE,null,value,SQLiteDatabase.CONFLICT_REPLACE);
            if (mx.getMode() == ConstantManager.SPORTSMAN_MODE_WARNING) {
                sql = "update " + DBHelper.ABONEMENT_TABLE + " set warning_count=warning_count+1 " +
                        "where _id=" + mx.getAbonement();
            }else if (mx.getMode() == ConstantManager.SPORTSMAN_MODE_WORKINGOFF){
                sql = "update " + DBHelper.ABONEMENT_TABLE + " set used_working=used_working+1 " +
                        "where _id=" + mx.getAbonement();
            }else {
                sql = "update " + DBHelper.ABONEMENT_TABLE + " set used_training=used_training+1 " +
                        "where _id=" + mx.getAbonement();
            }
            database.execSQL(sql);
        }
        close();
    }

    //TODO Перенести в тригер
    public void delTraining(int id){
        String sql;
        open();
        database.delete(DBHelper.TRAINING_TABLE,"_id="+id,null);

        Cursor cursor = database.query(DBHelper.REF_TABLE,new String[]{"id1"},"type_ref=1 and id2="+id,null,null,null,null);

        while (cursor.moveToNext()){
            Cursor rx = database.rawQuery("select rf.id1,tt.date,tt.time from ref_table rf\n" +
                    " join trainig_table tt on rf.type_ref=1 and rf.id2=tt._id\n" +
                    "where rf.id2<>"+id+" and rf.id1="+cursor.getInt(0)+"\n" +
                    "order by rf.id1,tt.date desc ,tt.time  desc\n" +
                    "limit 1",null);
            rx.moveToFirst();
            if (rx.getCount()!=0) {
                sql = "update " + DBHelper.SPORTSMAN_TABLE + " set last_date='" + rx.getString(1) + "', last_time='" + rx.getString(2) + "' " +
                        "where  _id=" + rx.getInt(0);
                database.execSQL(sql);
            }
        }

        cursor = database.query(DBHelper.REF_TABLE,new String[]{"id2","type_link"},"type_ref=2 and id1="+id,null,null,null,null);
        while (cursor.moveToNext()){
            if (cursor.getInt(1) == ConstantManager.SPORTSMAN_MODE_WARNING){
                sql = "update " + DBHelper.ABONEMENT_TABLE + " set warning_count=warning_count-1 " +
                        "where _id=" + cursor.getInt(0);
            }else if (cursor.getInt(1) == ConstantManager.SPORTSMAN_MODE_WORKINGOFF){
                sql = "update " + DBHelper.ABONEMENT_TABLE + " set used_working=used_working-1 " +
                        "where _id=" + cursor.getInt(0);
            }else {
                sql = "update " + DBHelper.ABONEMENT_TABLE + " set used_training=used_training-1 " +
                        "where _id=" + cursor.getInt(0);
            }
            database.execSQL(sql);
        }
        //TODO сдесь пеустановка у спортсмена даты последней тренировки.
        /*
        select rf.id1,tt.date,tt.time from ref_table rf
 join trainig_table tt on rf.type_ref=1 and rf.id2=tt._id
where rf.id2<>5
order by rf.id1,tt.date desc ,tt.time  desc
         */

        database.delete(DBHelper.REF_TABLE,"type_ref=2 and id1="+id,null);
        database.delete(DBHelper.REF_TABLE,"type_ref=1 and id2="+id,null);
        close();
    }

    public Cursor getSPTraining(int group_id){
        String sql = null;
        if (group_id == -1) {
            sql = "select sp._id,sp.sp_name,a.ci from SPORTSMAN sp " +
                    " left join (select sp_id,sum((count_training+(working-used_working))-(used_training+warning_count)) as ci from ABONEMENT group by sp_id) as a on sp._id= a.sp_id "+
                    " where sp.used=1 "+
                    " order by sp.sp_name";
        }else{
            sql="select sp._id,sp.sp_name,a.ci from SPORTSMAN sp \n" +
                    " left join (select sp_id,sum((count_training+(working-used_working))-(used_training+warning_count)) as ci from ABONEMENT group by sp_id) as a on sp._id= a.sp_id \n" +
                    "  join REF_TABLE rf on rf.type_ref=0 and sp._id=rf.id1 \n" +
                    " where rf.id2=" +group_id+" and sp.used=1"+
                    " order by sp.sp_name";

        }
        return database.rawQuery(sql,null);
    }
    public Cursor getSPTraining(int group_id,int training_id){
        String sql = null;
        if (group_id == -1){
            sql="select spt._id as _id,spt.sp_name as sp_name,a.ci as ci,tb.type_link as type_link,tb.id2 as ab from\n" +
                    "                (select sp._id,sp.sp_name,rf.id2,sp.used from  SPORTSMAN sp\n" +
                    "                        left join REF_TABLE rf on rf.type_ref=1 and sp._id = rf.id1 and rf.id2="+training_id+") as spt\n" +
                    "    left join (select rf.id1,rf.id2,rf.type_link,ab.sp_id from REF_TABLE rf \n" +
                    "                    left join ABONEMENT ab on rf.id2= ab._id\n" +
                    "                    where rf.type_ref=2 and rf.id1="+training_id+") as tb on spt.id2=tb.id1 and spt._id=tb.sp_id\n" +
                    "    left join (select sp_id,sum((count_training+(working-used_working))-(used_training+warning_count)) as ci from ABONEMENT group by sp_id) as a on spt._id= a.sp_id\n" +
                    " where spt.used=1 "+
                    " order by spt.sp_name";
        }

        return database.rawQuery(sql,null);
    }

    public Cursor getTraining(String selectDate){
        return database.query(DBHelper.TRAINING_TABLE,new String[]{"_id","training_name","count_item","date","time","repeat_training"},"date='"+selectDate+"'",null,null,null,"time");
    }

    public Cursor getTraining(int sp_id,String selectDate){
        String sql="select tt._id,tt.training_name,tt.count_item,tt.date,tt.time,ab.pos_id as abid,rf.type_link,tt.repeat_training from REF_TABLE  rf\n" +
                "  join TRAINIG_TABLE tt on rf.id2=tt._id\n" +
                "  join REF_TABLE rf2 on rf2.type_ref=2 and tt._id=rf2.id1\n" +
                "  join ABONEMENT ab on rf2.id2=ab._id and ab.sp_id=" +sp_id+
                " where  rf.type_ref=1 and rf.id1="+sp_id+" and tt.date='"+selectDate+"' order by tt.date desc ,tt.time desc";
        return database.rawQuery(sql,null);
    }

    public Cursor getTraining(int ab_id){
        String sql="select tt._id,tt.training_name,tt.count_item,tt.date,tt.time,ab.pos_id as abid,rf.type_link,tt.repeat_training from REF_TABLE rf\n" +
                " left join TRAINIG_TABLE tt on rf.id1=tt.\"_id\"\n" +
                " join ABONEMENT ab on rf.id2=ab._id\n" +
                "where rf.type_ref=2 and rf.id2="+ab_id+"\n" +
                "order by tt.date desc ,tt.time desc";
        return database.rawQuery(sql,null);
    }

    //TODO возможно следует передавать дату
    public Cursor getDateTraining(){
        String sql="select distinct date from TRAINIG_TABLE order by date;";
        return database.rawQuery(sql,null);
    }

    public Cursor getDateTraining(int sp_id){
        String sql="select distinct tt.date,rf.type_link from TRAINIG_TABLE  tt\n" +
                " join REF_TABLE rf on rf.type_ref=1 and tt._id=rf.id2\n" +
                " where rf.id1=" +sp_id+" "+
                "order by tt.date";
        return database.rawQuery(sql,null);
    }

    // sportsman
    public void addSportsman(SportsmanModel data){
        open();
        ContentValues value = new ContentValues();
        value.put("sp_name",data.getName());
        value.put("phone",data.getTel());
        value.put("comment",data.getComment());
        value.put("used",(data.isActive() ? 1:0));
        database.insert(DBHelper.SPORTSMAN_TABLE,null,value);
        close();
    }

    public void updateSportsman(SportsmanModel data){
        open();
        ContentValues value = new ContentValues();
        value.put("sp_name",data.getName());
        value.put("phone",data.getTel());
        value.put("comment",data.getComment());
        value.put("used",(data.isActive() ? 1:0));
        database.update(DBHelper.SPORTSMAN_TABLE,value,"_id="+data.getId(),null);
        close();
    }

    public void delSportsman(int id){
        open();
        database.delete(DBHelper.SPORTSMAN_TABLE,"_id="+id,null);
        close();
    }

    public Cursor getSportsman(boolean mode){
        int m = (mode ? 1:0);
        String sql="select sp._id,sp.sp_name,sp.phone,sp.comment,a.ci,ab.sm,sp.last_date,sp.last_time,sp.used from SPORTSMAN sp\n" +
                "  left join (select id1, count(1) as ci from REF_TABLE where type_ref=1   group by id1) as a on sp._id=a.id1 \n" +
                "  left join (select sp_id,sum((count_training+(working-used_working))-(used_training+warning_count)) as sm from ABONEMENT "+
                "where count_training+(working-used_working)-(used_training+warning_count)<>0 group by sp_id) as ab "+
                "on sp._id=ab.sp_id\n" +
                "where used="+m+"\n"+
                "order by sp.sp_name";
        return database.rawQuery(sql,null);
    }

    public Cursor getSportsman(int id){
        String sql="select sp._id,sp.sp_name,sp.phone,sp.comment,a.ci,ab.sm,sp.last_date,sp.last_time from SPORTSMAN sp\n" +
                "  left join (select id1, count(1) as ci from REF_TABLE where type_ref=1   group by id1) as a on sp._id=a.id1 \n" +
                "  left join (select sp_id,sum((count_training+(working-used_working))-(used_training+warning_count)) as sm from ABONEMENT "+
                "where count_training-used_training<>0 group by sp_id) as ab "+
                "on sp._id=ab.sp_id\n" +
                " where _id="+id+" "+
                "order by sp.sp_name";
        return database.rawQuery(sql,null);
    }

    public boolean isUseSportsman(int id){
        String sql = "select count(*) as ci FROM REF_TABLE rf\n" +
                "where rf.type_ref=1 and rf.id1="+id;
        open();
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        boolean ret = (cursor.getInt(0) != 0 ? true : false);
        close();
        return ret;
    }

    // абонемент

    public Cursor getAbonement(int sportsman_id){
        return database.query(DBHelper.ABONEMENT_TABLE,
                new String[]{"_id","sp_id","pos_id","buy_date","start_date","end_date",
                        "type_abonement","pay","count_training","comment","used_training","working"
                        ,"warning_count","debt","alarm_date","used_working"},
                "sp_id="+sportsman_id,null,null,null,"pos_id");
    }

    public void delAbonement(int id,int sprotsman_id){
        open();
        database.delete(DBHelper.ABONEMENT_TABLE,"pos_id="+id+" and sp_id="+sprotsman_id,null);
        close();
    }
    // удаляем по идентификатору
    public void delAbonement(int id){
        open();
        database.delete(DBHelper.ABONEMENT_TABLE,"_id="+id,null);
        close();
    }
    // удаляем не приязанные абонементы
    public void delAbonement(){
        open();
        database.delete(DBHelper.ABONEMENT_TABLE,"sp_id=-1",null);
        close();
    }
    // проверяем есть ли тренировки на абонементе
    public boolean isUseAbomenet(int id,int sportsman_id) {
        String sql="select count(*) as ci from ABONEMENT\n" +
                "where pos_id="+id+" and sp_id="+sportsman_id+" and used_training+used_working+warning_count<>0";
        open();
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        boolean ret = (cursor.getInt(0) !=0 ? true : false );
        close();
        return ret;
    }

    public void addUpdateAbonement(AbonementModel model){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        open();
        ContentValues values = new ContentValues();
        if (model.getAbonementId()!= -1){
            values.put("_id",model.getAbonementId());
        }
        int ref_id = -1;
        //
        if (model.getAbonementId()==-1) {
            String sql = "select ab._id,ab.warning_count from ABONEMENT ab\n" +
                    " left join (select id1,count(1) as ci from REF_TABLE rf\n" +
                    "      where rf.type_ref=3\n" +
                    "      group by id1) as a on ab.\"_id\"=a.id1 \n" +
                    "where ab.sp_id=" + model.getSpId() + " and ab.end_date<'" + model.getEndDate() + "' and (ab.warning_count-COALESCE(a.ci,0,a.ci))<>0\n" +
                    "order by ab.end_date DESC\n" +
                    "limit 1";
            Cursor cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                model.setWorking(cursor.getInt(1));
                ref_id = cursor.getInt(0);
            }
        }
        //

        values.put("sp_id",model.getSpId());
        values.put("pos_id",model.getId());
        values.put("buy_date",format.format(model.getCreateDate()));
        values.put("start_date",format.format(model.getStartDate()));
        values.put("end_date",format.format(model.getEndDate()));
        values.put("pay",model.getPay());
        values.put("count_training",model.getCountTraining());
        values.put("comment",model.getComment());
        values.put("type_abonement",model.getType());
        values.put("used_training",model.getUsedTraining());
        values.put("working",model.getWorking());
        values.put("warning_count",model.getWarning());
        values.put("debt",model.getDebit());
        if (model.getDebitDate() != null){
            values.put("alarm_date",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(model.getDebitDate()));
        }
        int rec_id = (int) database.insertWithOnConflict(DBHelper.ABONEMENT_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);

        if (model.getAbonementId()==-1) {
            if (model.getWorking() != 0) {
                values.clear();
                values.put("type_ref", 3);
                values.put("id1", ref_id); // откуда
                values.put("id2", rec_id); // куда
                database.insert(DBHelper.REF_TABLE, null, values);
            }
        }
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

    // возвращает чтото если у спортсмена дата последней тренировки меньше.
    private Cursor getLastDateTraining(int trainig) {
        String sql="select sp._id,tt.date,tt.time from TRAINIG_TABLE tt\n" +
                " left join REF_TABLE rf on rf.type_ref=1 and tt._id=rf.id2\n" +
                " left join SPORTSMAN sp on rf.id1=sp._id\n" +
                " where tt._id="+trainig+" and (tt.date>sp.last_date) and (tt.time>sp.last_time)";
        return database.rawQuery(sql,null);
    }

    public Cursor getAbonementInDate(int sp_id,String date){
        String sql="select _id,working,used_working,(count_training)-(used_training+warning_count) as trainint from ABONEMENT \n" +
                   " where sp_id="+sp_id+" and start_date<='"+date+"' and end_date>='"+date+"' and ((count_training+(working-used_working))-(used_training+warning_count))<>0"+
                " order by pos_id limit 1";

        //System.out.println(sql);
        return database.rawQuery(sql,null);
    }

    public Cursor getDohod(String sdate, String edate){
        /*
        String sql="select sp.sp_name,ab.pay,(ab.start_date||' - '||ab.end_date) as period from ABONEMENT ab\n" +
                "  join SPORTSMAN sp on ab.sp_id=sp._id\n" +
                "where ab.start_date>='"+sdate+"' and ab.end_date<='"+edate+"'\n" +
                "order by sp.sp_name,ab.start_date";
        */
        String sql="select sum(ab.pay) as sm from ABONEMENT ab\n" +
                "  join SPORTSMAN sp on ab.sp_id=sp._id\n" +
                "where ab.buy_date>='"+sdate+"' and ab.buy_date<='"+edate+"'";
        return database.rawQuery(sql,null);
    }
    public Cursor getRateAll(String sdate,String edate){
        String sql="select sum(summ) as sm from RATE "+
                "where create_date>='"+sdate+"' and create_date<='"+edate+"'";
        return database.rawQuery(sql,null);
    }

    public Cursor getDohodDetail(String sdate,String edate){
        String sql="select sp.sp_name,ab.comment,ab.pay,ab.start_date,ab.end_date from ABONEMENT ab\n" +
                " left join SPORTSMAN sp on ab.sp_id=sp._id\n" +
                "where ab.buy_date>='"+sdate+"' and ab.buy_date<='"+edate+"'\n" +
                "order by sp.sp_name";
        return database.rawQuery(sql,null);
    }

    public Cursor getRateDetail(String sdate,String edate){
        String sql="select rt._id,rf.name,rt.create_date,rt.summ,rt.rate_type,rt.comment from RATE rt\n" +
                " left join rate_type rf on rt.rate_type=rf._id"+
                " where rt.create_date>='"+sdate+"' and rt.create_date<='"+edate+"'";
        return database.rawQuery(sql,null);
    }

    public Cursor getRateDetailOne(int id){
        return  database.query(DBHelper.RATE_TABLE,
                new String[]{"_id","rate_type","create_date","summ","comment"},
                "_id="+id,null,null,null,null);
    }

    // rate
    public void addUpdateRateType(int id,String name){
        open();
        ContentValues values = new ContentValues();
        if (id!=-1){
            values.put("_id",id);
        }
        values.put("name",name);
        database.insertWithOnConflict(DBHelper.RATE_TYPE_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        close();
    }

    public void delRateType(int id){
        open();
        database.delete(DBHelper.RATE_TYPE_TABLE,"_id="+id,null);
        close();
    }

    public Cursor getRateType(){
        return database.query(DBHelper.RATE_TYPE_TABLE,new String[]{"_id","name"},null,null,null,null,null);
    }

    public void addUpdateRate(int rate_type,String date,Float summ,int rec_id,String coment){
        ContentValues values = new ContentValues();
        if (rec_id != -1){
            values.put("_id",rec_id);
        }
        values.put("rate_type",rate_type);
        values.put("create_date",date);
        values.put("summ",summ);
        values.put("comment",coment);
        open();
        database.insertWithOnConflict(DBHelper.RATE_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        close();
    }

    public void delRate(int id){
        open();
        database.delete(DBHelper.RATE_TABLE,"_id="+id,null);
        close();
    }

    // получаем список абонементов у которых на следующий день заканчивает действие абонемента и еще
    // есть тренировки
    public Cursor getCloseNotUsedAbonement(String date){
        String sql="select ab._id,ab.sp_id,ab.pos_id,ab.end_date,sp.sp_name from abonement ab\n" +
                " left join sportsman sp on ab.sp_id=sp._id "+
                "where  '"+date+"'in (date(ab.end_date,'-1 day'),ab.end_date) and (ab.count_training+(ab.working-ab.used_working))-(ab.used_training+ab.warning_count)<>0";
        return  database.rawQuery(sql,null);
    }

}
