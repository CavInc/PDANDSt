package cav.pdst.ui.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cav.pdst.R;
import cav.pdst.data.models.AbonementModel;

public class AbonementAdapter extends ArrayAdapter<AbonementModel> {
    private LayoutInflater mInflater;
    private int resLayout;

    public AbonementAdapter(Context context, int resource, List<AbonementModel> objects) {
        super(context, resource, objects);
        resLayout = resource;
        mInflater = LayoutInflater.from(context);
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
            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }
        AbonementModel record = getItem(position);
        holder.mPay.setText(String.valueOf(record.getPay()));
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

    }
}
