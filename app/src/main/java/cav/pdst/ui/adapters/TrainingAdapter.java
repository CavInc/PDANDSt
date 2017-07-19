package cav.pdst.ui.adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cav.pdst.R;
import cav.pdst.data.models.SportsmanTrainingModel;
import cav.pdst.utils.ConstantManager;

public class TrainingAdapter extends ArrayAdapter<SportsmanTrainingModel> {
    private LayoutInflater mInflater;
    private int resLayout;

    public TrainingAdapter(Context context, int resource, List<SportsmanTrainingModel> objects) {
        super(context, resource, objects);
        resLayout = resource;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(resLayout,parent,false);
            holder = new ViewHolder();
            holder.mName = (CheckBox) row.findViewById(R.id.checkBox);
            /*
            holder.mName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                }
            });
            */
            holder.mCount = (TextView) row.findViewById(R.id.tr_item_count_item);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }
        SportsmanTrainingModel record = getItem(position);
        holder.mCount.setText(String.valueOf(record.getCount()));
        holder.mName.setText(record.getName());
        if (record.getCount()!=0){
            holder.mName.setTextColor(Color.GREEN);
        }else {
            holder.mName.setTextColor(Color.BLACK);
        }
        return row;
    }

    private class ViewHolder {
        public CheckBox mName;
        public TextView mCount;
    }

}
