package cav.pdst.ui.adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cav.pdst.R;
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.data.models.SportsmanTrainingModel;
import cav.pdst.utils.ConstantManager;

public class TrainingAdapter extends ArrayAdapter<SportsmanTrainingModel> {
    private static final String TAG = "TADAPTER";
    private LayoutInflater mInflater;
    private int resLayout;

    public TrainingAdapter(Context context, int resource, List<SportsmanTrainingModel> objects) {
        super(context, resource, objects);
        resLayout = resource;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(resLayout,parent,false);
            holder = new ViewHolder();
            holder.mName = (CheckedTextView) row.findViewById(R.id.checkBox);
/*
            holder.mName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.d(TAG,"POSITION: "+position+" "+b);
                    getItem(position).setCheck(b);
                }
            });
            */

            holder.mCount = (TextView) row.findViewById(R.id.tr_item_count_item);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }
        //holder.mName.setChecked(false);
        SportsmanTrainingModel record = getItem(position);
        holder.mCount.setText(String.valueOf(record.getCount()));
        holder.mName.setText(record.getName());
        holder.mName.setBackgroundColor(Color.TRANSPARENT);
        if (record.getCount()!=0){
            holder.mName.setTextColor(ContextCompat.getColor(getContext(),R.color.app_green));
        }else {
            holder.mName.setTextColor(Color.BLACK);
        }
        switch (record.getMode()){
            case ConstantManager.SPORTSMAN_MODE_TRAINING:
                holder.mName.setTextColor(Color.BLACK);
                holder.mName.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.app_green));
                break;
            case ConstantManager.SPORTSMAN_MODE_PASS:
                holder.mName.setTextColor(Color.BLACK);
                holder.mName.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.app_red));
                break;
            case ConstantManager.SPORTSMAN_MODE_WARNING:
                holder.mName.setTextColor(Color.BLACK);
                holder.mName.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.app_blue));
                break;
            case ConstantManager.SPORTSMAN_MODE_WORKINGOFF:
                holder.mName.setTextColor(Color.BLACK);
                holder.mName.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.app_yellow));

                break;
        }
        holder.mName.setChecked(record.isCheck());
        return row;
    }

    public void setData(ArrayList<SportsmanTrainingModel> data){
        this.clear();
        this.addAll(data);
    }

    private class ViewHolder {
        public CheckedTextView mName;
        public TextView mCount;
    }

}
