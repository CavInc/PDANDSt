package cav.pdst.ui.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cav.pdst.R;

public class DatePickerFragment extends DialogFragment {
    private static final String SELECT_DATE = "SELECT_DATE";
    private DatePicker mDatePicker;
    private Date mInitDate;

    private OnDateGetListener mOnDateGetListener;

    public interface OnDateGetListener {
        public void OnDateGet(Date date);
    }

    public static DatePickerFragment newInstance(){
        Bundle args = new Bundle();
        DatePickerFragment fragment = new DatePickerFragment();
        return fragment;
    }

    public static DatePickerFragment newInstance(Date initDate){
        Bundle args = new Bundle();
        args.putSerializable(SELECT_DATE,initDate);
        DatePickerFragment fragment = new DatePickerFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnDateGetListener = (OnDateGetListener) activity;
        } catch (Exception e) {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnDateGetListener !=null){
            mOnDateGetListener = null;
        }
    }

    public DatePickerFragment() {
        if (getArguments() != null) {
            if (getArguments().containsKey(SELECT_DATE)) {
                mInitDate = (Date) getArguments().getSerializable(SELECT_DATE);
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Calendar calendar = Calendar.getInstance();

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);

        if (mInitDate != null) {
            this.setSelectedDate(mInitDate);
        }

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
                        if (mOnDateGetListener != null) {
                            mOnDateGetListener.OnDateGet(date);
                        }
                    }
                })
                .create();
        //return super.onCreateDialog(savedInstanceState);
    }

    public void setOnDateGetListener( OnDateGetListener listener){
        mOnDateGetListener = listener;
    }

    public void setSelectedDate(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        mDatePicker.init(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),null);
    }


}
