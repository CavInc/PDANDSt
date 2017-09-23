package cav.pdst.data.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1 ;

    public static final String DATABASE_NAME = "pdst.db3";

    public static final String GROUP_TABLE = "GROUP_TABLE";
    public static final String TRAINING_TABLE ="TRAINIG_TABLE";
    public static final String REF_TABLE = "REF_TABLE";
    public static final String SPORTSMAN_TABLE = "SPORTSMAN";
    public static final String ABONEMENT_TABLE = "ABONEMENT";
    public static final String RATE_TYPE_TABLE = "RATE_TYPE";
    public static final String RATE_TABLE = "RATE";

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
                    " repeat_training integer default 0,"+ //тип повторов.
                    " date text,time text)");
            db.execSQL("CREATE INDEX \"trainint_date_desc\" on trainig_table (date DESC)");
            db.execSQL("CREATE INDEX \"trainint_date_asc\" on trainig_table (date ASC)");

            db.execSQL("create table "+REF_TABLE+"("+
                    "type_ref integer not null,"+ // 0 -spman - group 1- traning-spman 2- тренировка - абонемент 3- абонемент - абонемент
                    "id1 integer not null,"+ // spanam
                    "id2 integer not null,"+ // otner
                    "type_link integer default -1,"+ // для связи тренировка абонемент указан тип связи
                    "primary key(type_ref,id1,id2))");

            db.execSQL("create table "+SPORTSMAN_TABLE+"("+
                    "_id integer not null primary key AUTOINCREMENT,"+
                    "sp_name text,"+
                    "phone text,"+"" +
                    "last_date text default '2010-01-01',"+
                    "last_time text default '00:00',"+
                    "used integer default 1,"+ // активный клиент или нет.
                    "comment text)");

            db.execSQL("CREATE INDEX \"SP_NAME_SPORTSMAN\" on sportsman (sp_name ASC)");

            db.execSQL("create table "+ABONEMENT_TABLE+"("+
                    " _id integer not null primary key AUTOINCREMENT,"+
                    " sp_id integer not null,"+
                    " pos_id integer not null,"+
                    " buy_date text,"+
                    " start_date text,"+
                    " end_date text,"+
                    " type_abonement integer default 0,"+ // 0 -абонемент 1- разовое
                    " pay numeric,"+"" +
                    " count_training integer,"+
                    " used_training integer default 0,"+
                    " working integer default 0,"+ // отработки
                    " used_working integer default 0,"+ // использованные отработки
                    " warning_count integer default 0,"+
                    " debt numeric default 0,"+
                    " alarm_date text,"+
                    " comment text)");

            db.execSQL("CREATE INDEX \"ab_date\" on abonement (start_date ASC, end_date ASC)");

            db.execSQL("create table "+RATE_TYPE_TABLE+"(" +
                    "_id integer not null primary key AUTOINCREMENT,"+
                    "name text)");

            db.execSQL("create table "+RATE_TABLE+"(" +
                    "_id integer not null primary key AUTOINCREMENT," +
                    "rate_type integer not null," +
                    "create_date text," +
                    "comment text,"+
                    "summ numeric default 0)");

            db.execSQL("CREATE INDEX \"rate_data_idx\" on rate (create_date ASC)");

            // обновляем записи в абонементах при создании спортсмена
            db.execSQL("CREATE TRIGGER sportsman_ai0\n" +
                    "   AFTER   INSERT \n" +
                    "   ON sportsman\n" +
                    " BEGIN\n" +
                    "    update abonement set sp_id=new._id\n" +
                    "    where sp_id= -1;\n" +
                    "END");
            // удаление связанных елементов
            db.execSQL("CREATE TRIGGER abonement_bd0 AFTER DELETE on ABONEMENT\n" +
                    "BEGIN\n" +
                    "  DELETE from REF_TABLE\n" +
                    "  where type_ref=3 and id1=old._id;\n" +
                    "  DELETE FROM REF_TABLE\n" +
                    "  where type_ref=3 and id2=old._id;\n" +
                    "END;\n");

             /*
            db.execSQL("CREATE TRIGGER abonement_ai1\n" +
                    "   after \n" +
                    "    INSERT \n" +
                    "   ON abonement\n" +
                    "   FOR EACH ROW\n" +
                    "BEGIN\n" +
                    " update abonement set pos_id =(\n" +
                    "   select coalesce(max(pos_id)+1,0,max(pos_id)+1)  from abonement\n" +
                    "   where sp_id=new.sp_id)\n" +
                    "   where _id=new._id;\n" +
                    "END");
             */

        }else {

        }
    }
}
