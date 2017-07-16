package cav.pdst.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cav.pdst.data.models.GroupModel;
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

    // training
    public void addTraining(TrainingModel data){
        open();
        ContentValues value = new ContentValues();
        value.put("training_name",data.getName());
        value.put("count_item",data.getCount());
        //value.put("",data.getType());
        database.insert(DBHelper.TRAINING_TABLE,null,value);
        close();
    }

    public void delTraining(int id){

    }

    public Cursor getTraining(){
        return database.query(DBHelper.TRAINING_TABLE,new String[]{"_id","training_name"},null,null,null,null,"training_name");
    }

    // sportsman
    public void addSportsman(){

    }

    public void delSportsman(int id){

    }

    public Cursor getSportsman(){
        return database.query(DBHelper.SPORTSMAN_TABLE,new String[]{"_id","sp_name","phone","comment"},null,null,null,null,"sp_name");
    }


}
