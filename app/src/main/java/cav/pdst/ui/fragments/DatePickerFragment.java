package cav.pdst.ui.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cav.pdst.R;
import cav.pdst.utils.ConstantManager;

public class DatePickerFragment extends DialogFragment {
    private static final String TAG = "DP";
    private DatePicker mDatePicker;

    private OnDateGet mOnDateGetListener;

    public interface OnDateGet{
        public void OnDateGet(Date date);
    }

    public static DatePickerFragment newInstance(){
        Bundle args = new Bundle();
        DatePickerFragment fragment = new DatePickerFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnDateGetListener =  (OnDateGet) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Calendar calendar = Calendar.getInstance();

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Установить дату")
                .setView(v)
                .setPositiveButton(R.string.button_ok,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year, month, day).getTime();
                        mOnDateGetListener.OnDateGet(date);
                    }
                })
                .create();
        //return super.onCreateDialog(savedInstanceState);
    }


}
