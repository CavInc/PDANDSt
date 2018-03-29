package cav.pdst.ui.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.data.database.DBHelper;
import cav.pdst.data.managers.DataManager;
import cav.pdst.ui.fragments.SelectStoreFileDialog;
import cav.pdst.utils.Utils;

public class Preferences extends PreferenceActivity {

    private Context mContext;
    private DataManager mDataManager;

    private SharedPreferences pref;

    private Preference mSaveSD;
    private Preference mRestoreSD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        mContext = (Context) this;
        mDataManager = DataManager.getInstance();

        Preference opendialog = (Preference) findPreference("alarm_time");
        opendialog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference) {
                pref = PreferenceManager.getDefaultSharedPreferences(mContext);
                String tm = pref.getString("alarm_time",null);
                int hour;
                int minute;
                Calendar c = Calendar.getInstance();
                if (tm == null){
                    hour = c.get(Calendar.HOUR_OF_DAY);
                    minute = c.get(Calendar.MINUTE);
                } else {
                    String[] tmx = tm.split(":");
                    hour = Integer.parseInt(tmx[0]);
                    minute = Integer.parseInt(tmx[1]);
                }

                new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                        String tm = Utils.formatTime(hours,minutes);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("alarm_time",tm);
                        editor.apply();
                    }
                }, hour, minute, true).show();
                return true;
            }
        });

        mSaveSD = findPreference("save_sd");
        mSaveSD.setOnPreferenceClickListener(saveSDListener);

        mRestoreSD = findPreference("restore_sd");
        mRestoreSD.setOnPreferenceClickListener(loadSDListener);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        bar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pref!=null) {
            if (pref.getBoolean("alarm_start_flg", false)) {
                mDataManager.getPreferensManager().setFirstStart(false);
                Utils.restartAlarm(mContext, 0);
            }
        }
    }

    Preference.OnPreferenceClickListener saveSDListener = new Preference.OnPreferenceClickListener(){

        @Override
        public boolean onPreferenceClick(Preference preference) {

            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                Toast.makeText(getApplicationContext(),
                        "SD-карта не доступна: " + Environment.getExternalStorageState(),
                        Toast.LENGTH_LONG).show();
            } else {
                // получаем путь к SD
                File path = new File (Environment.getExternalStorageDirectory(), "PDANDST");
                if (! path.exists()) {
                    if (!path.mkdirs()) {
                        Toast.makeText(getApplicationContext(),
                                "Каталог не создан: " + path.getAbsolutePath(),
                                Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HHmm");
                String dt = format.format(new Date());


                // in
                File fin = new File (getDatabasePath(DBHelper.DATABASE_NAME).getAbsolutePath());

                // выходной файл
                File fOut = new File(path, "CH_PDT"+dt+".db3");
                try {
                        InputStream in = new FileInputStream(fin);
                        OutputStream out = new FileOutputStream(fOut);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        // Закрываем потоки
                        in.close();
                        out.close();
                        Toast.makeText(getApplicationContext(),
                                "База сохранена : " + fOut.getAbsolutePath(),
                                Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                  e.printStackTrace();
                }
            }
            return true;
        }
    };

    Preference.OnPreferenceClickListener loadSDListener = new Preference.OnPreferenceClickListener(){

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                Toast.makeText(getApplicationContext(),
                        "SD-карта не доступна: " + Environment.getExternalStorageState(),
                        Toast.LENGTH_LONG).show();
            } else {
                // получаем путь к SD
                File path = new File (Environment.getExternalStorageDirectory(), "PDANDST");
                if (! path.exists()) {
                    if (!path.mkdirs()) {
                        Toast.makeText(getApplicationContext(),
                                "Каталог не создан: " + path.getAbsolutePath(),
                                Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
                File[] listF = path.listFiles();
                if (listF.length!= 0) {

                    //SelectStoreFileDialog dialog = SelectStoreFileDialog.newInctance(listF);
                   // dialog.show(getFragmentManager(),"SSD");

                    for (File f : listF){
                        Log.d("PF",f.getName());
                    }

                }
            }
            return true;
        }
    };

}