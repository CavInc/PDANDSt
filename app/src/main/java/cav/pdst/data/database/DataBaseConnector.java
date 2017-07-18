package cav.pdst.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;

import cav.pdst.data.models.GroupModel;
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
    public void addGroup(GroupModel model){
        open();
        ContentValues value = new ContentValues();
        value.put("group_name",model.getName());
        value.put("count_item",model.getCount());
        database.insert(DBHelper.GROUP_TABLE,null,value);
        close();
    }

    public void delGroup(int id){
        open();
        database.delete(DBHelper.GROUP_TABLE,"_id="+id,null);
        close();
    }

    public Cursor groupAll(){
        return database.query(DBHelper.GROUP_TABLE,new String[]{"_id","group_name","count_item"},null,null,null,null,"group_name");
    }

    public void updateGroup(GroupModel model){
        open();
        ContentValues values = new ContentValues();
        values.put("group_name",model.getName());
        values.put("count_item",model.getCount());
        database.update(DBHelper.GROUP_TABLE,values,"_id="+model.getId(),null);
        close();
    }

    // training
    public void addTraining(TrainingModel data){
        open();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues value = new ContentValues();
        value.put("training_name",data.getName());
        value.put("count_item",data.getCount());
        value.put("date",format.format(data.getDate()));
        value.put("time",data.getTime());
        //value.put("",data.getType());
        database.insert(DBHelper.TRAINING_TABLE,null,value);
        close();
    }

    public void delTraining(int id){
        open();
        database.delete(DBHelper.TRAINING_TABLE,"_id="+id,null);
        close();
    }

    public Cursor getSPTraining(){
        String sql="select sp._id,sp.sp_name,a.ci from SPORTSMAN sp\n" +
                "   left join (select id1, count(1) as ci from REF_TABLE where type_ref=1\n" +
                "   group by id1) as a on sp._id=a.id1;";
        return database.rawQuery(sql,null);
    }

    public Cursor getTraining(){
        return database.query(DBHelper.TRAINING_TABLE,new String[]{"_id","training_name","count_item","date","time"},null,null,null,null,"training_name");
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

    public void delSportsman(int id){
        open();
        database.delete(DBHelper.SPORTSMAN_TABLE,"_id="+id,null);
        close();
    }

    public Cursor getSportsman(){
        return database.query(DBHelper.SPORTSMAN_TABLE,new String[]{"_id","sp_name","phone","comment"},null,null,null,null,"sp_name");
    }


}
