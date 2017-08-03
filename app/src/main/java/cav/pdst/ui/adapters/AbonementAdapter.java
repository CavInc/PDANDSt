package cav.pdst.ui.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cav.pdst.R;
import cav.pdst.data.models.AbonementModel;

public class AbonementAdapter extends ArrayAdapter<AbonementModel> {
    private LayoutInflater mInflater;
    private int resLayout;
    SimpleDateFormat mFormat;
    private Context mContext;

    public AbonementAdapter(Context context, int resource, List<AbonementModel> objects) {
        super(context, resource, objects);
        mContext = context;
        resLayout = resource;
        mInflater = LayoutInflater.from(context);
        mFormat = new SimpleDateFormat("E dd.MM.yyyy");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(resLayout, parent, false);
            holder = new ViewHolder();
            holder.mCreateDate = (TextView) row.findViewById(R.id.ab_create_date);
            holder.mStartDate = (TextView) row.findViewById(R.id.ab_start_date);
            holder.mEndDate = (TextView) row.findViewById(R.id.ab_end_date);
            holder.mTraining = (TextView) row.findViewById(R.id.ab_training);
            holder.mPay = (TextView) row.findViewById(R.id.ab_pay_summ);
            holder.mPrime = (TextView) row.findViewById(R.id.ab_prime);
            holder.mNum = (TextView) row.findViewById(R.id.ab_num_num);
            holder.mAbType = (TextView) row.findViewById(R.id.ab_num_text);
            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }
        AbonementModel record = getItem(position);

        holder.mNum.setText(String.valueOf(record.getId()));
        holder.mCreateDate.setText(mContext.getString(R.string.create_date)+":"+mFormat.format(record.getCreateDate()));
        holder.mStartDate.setText(mFormat.format(record.getStartDate()));
        holder.mEndDate.setText(mFormat.format(record.getEndDate()));
        holder.mPay.setText(String.valueOf(record.getPay()));
        holder.mTraining.setText("Использовано: "+record.getUsedTraining()+
                " из "+record.getCountTraining()+", Доступно: "+(record.getCountTraining()-record.getUsedTraining()));
        if ((record.getCountTraining()-record.getUsedTraining())!=0) {
            holder.mPrime.setText("Доступны тренировки");
            holder.mPrime.setTextColor(ContextCompat.getColor(getContext(),R.color.app_green));
        } else {
            holder.mPrime.setText("Потрачен");
        }
        if (record.getType() == 0) {
            holder.mAbType.setText("Номер абонемента:");
        }else {
            holder.mAbType.setText("Разовое занятие");
        }

        //holder.mPrime.setText();
        return row;
    }

    public void setData(ArrayList<AbonementModel> data) {
        this.clear();
        this.addAll(data);
    }

    private class ViewHolder{
        public TextView mCreateDate;
        public TextView mStartDate;
        public TextView mEndDate;
        public TextView mTraining;
        public TextView mPay;
        public TextView mPrime;
        public TextView mNum;
        public TextView mAbType;

    }
}
