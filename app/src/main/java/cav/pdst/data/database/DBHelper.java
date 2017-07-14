package cav.pdst.data.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1 ;

    public static final String DATABASE_NAME = "pdst.db3";

    public static final String GROUP_TABLE = "GROUP_TABLE";
    public static final String TRAINING_TABLE ="TRAINIG_TABLE";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        updatedDB(db,0,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updatedDB(db,oldVersion,newVersion);
    }

    private void updatedDB(SQLiteDatabase db,int oldVersion,int newVersion){
        if (oldVersion<1){
            db.execSQL("create table "+GROUP_TABLE+"(" +
                    "_id integer not null primary key AUTOINCREMENT,"+
                    " group_name text,"+
                    " count_item integer default 0)");

            db.execSQL("create table "+TRAINING_TABLE+"("+"" +
                    "_id integer not null primary key AUTOINCREMENT,"+
                    " training_name text,"+
                    " count_item integer default 0,"+
                    " date text,time text)");


        }else {

        }
    }
}
