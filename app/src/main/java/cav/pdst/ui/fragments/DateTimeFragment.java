package cav.pdst.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cav.pdst.R;

public class DateTimeFragment extends DialogFragment {

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    private OnDateTimeChangeListener mListener;

    public interface OnDateTimeChangeListener {
        public void OnDateTimeChange(Date date);
    }

    public DateTimeFragment newInstance(){
        DateTimeFragment fragment = new DateTimeFragment();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.date_time_dialog, null);
        mDatePicker = (DatePicker) v.findViewById(R.id.dt_datePicker);
        mTimePicker = (TimePicker) v.findViewById(R.id.dt_timePicker);
        mTimePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(c.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setMinute(c.get(Calendar.MINUTE));

        }else {
            mTimePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setCurrentMinute(c.get(Calendar.MINUTE));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выбор даты и времен")
                .setView(v)
                .setPositiveButton(R.string.button_ok,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                int h;
                int m;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    h = mTimePicker.getHour();
                    m = mTimePicker.getMinute();
                }else {
                    h = mTimePicker.getCurrentHour();
                    m = mTimePicker.getCurrentMinute();
                }
                Date date = new GregorianCalendar(year, month, day,h,m).getTime();
                if (mListener != null){
                    mListener.OnDateTimeChange(date);
                }
            }
        }).setNegativeButton(R.string.button_cancel,null);

        return builder.create();
    }

    public void setOnDateTimeChangeListener (OnDateTimeChangeListener listener){
        mListener = listener;
    }

}