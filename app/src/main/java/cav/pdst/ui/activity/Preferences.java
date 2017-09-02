package cav.pdst.ui.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Calendar;

import cav.pdst.R;
import cav.pdst.utils.Utils;

public class Preferences extends PreferenceActivity {

    private Context mContext;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        mContext = (Context) this;

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

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
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
        Log.d("PREF","Stop");
        if (pref!=null) {
            if (pref.getBoolean("alarm_start_flg", false)) {
                Utils.restartAlarm(mContext, 0);
            }
        }
    }
}