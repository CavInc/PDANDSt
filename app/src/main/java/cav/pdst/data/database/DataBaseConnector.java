package cav.pdst.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cav.pdst.data.models.GroupModel;


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
        ContentValues value = new ContentValues();
        value.put("group_name",model.getName());
        value.put("count_item",model.getCount());
        database.insert(DBHelper.GROUP_TABLE,null,value);

    }
    public Cursor groupAll(){
        return database.query(DBHelper.GROUP_TABLE,new String[]{"group_name","count_item"},null,null,null,null,"group_name");
    }

}
