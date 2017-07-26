package cav.pdst.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cav.pdst.R;
import cav.pdst.data.models.SportsmanModel;

public class SportsmanAdapter extends ArrayAdapter<SportsmanModel>{
    private LayoutInflater mInflater;
    private int resLayout;

    public SportsmanAdapter(Context context, int resource, List<SportsmanModel> objects) {
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
            holder.mName = (TextView) row.findViewById(R.id.spman_item_name);
            holder.mPhone = (TextView) row.findViewById(R.id.spman_item_phone);
            holder.mTraining = (TextView) row.findViewById(R.id.spman_item_training_count);
            holder.mAbCount = (TextView) row.findViewById(R.id.spman_item_abonement_count);
            holder.mLastTraining = (TextView) row.findViewById(R.id.spman_item_last_tr);
            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        SportsmanModel record = getItem(position);
        holder.mName.setText(record.getName());
        holder.mPhone.setText(record.getTel());
        if (record.getTraninigWrk()>0) {
            holder.mAbCount.setText("доступно:"+record.getTraninigWrk());
            holder.mAbCount.setTextColor(Color.GREEN);
        }else {
            holder.mAbCount.setText("Нет действующих авонеметнов");
            holder.mAbCount.setTextColor(Color.RED);
        }
        holder.mTraining.setText("Тренировки: "+record.getTrainingAll()+" / ");
        return row;
    }

    public void setData(ArrayList<SportsmanModel> data) {
        this.clear();
        this.addAll(data);
    }

    private class ViewHolder{
        //TODO добавить елементы после построения модели
        public TextView mName;
        public TextView mPhone;
        public TextView mTraining;
        private TextView mAbCount;
        private TextView mLastTraining;

    }
}
