package cav.pdst.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import cav.pdst.R;

public class Preferences extends PreferenceActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        mContext = (Context) this;

        Preference opendialog = (Preference) findPreference("alarm_time");
        opendialog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });

    }
}