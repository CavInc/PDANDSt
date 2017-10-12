package cav.pdst.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cav.pdst.R;
import cav.pdst.data.models.AbEndingModel;

public class AbonementEndigRepAdapter extends ArrayAdapter<AbEndingModel> {

    private LayoutInflater mInflater;
    private int resLayout;
    SimpleDateFormat mFormat;

    public AbonementEndigRepAdapter(Context context, int resource, ArrayList<AbEndingModel> objects) {
        super(context, resource, objects);
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
            holder.mSportsman = (TextView) row.findViewById(R.id.ab_ed_sportsman);
            holder.mAbonement = (TextView) row.findViewById(R.id.ab_ed_ab);
            holder.mStartDate = (TextView) row.findViewById(R.id.ab_ed_start_date);
            holder.mEndDate = (TextView) row.findViewById(R.id.ab_ed_end_date);
            holder.mCount = (TextView) row.findViewById(R.id.ab_ed_training_count);
            holder.mType = (TextView) row.findViewById(R.id.ab_ed_type_rec);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }
        AbEndingModel record = getItem(position);

        holder.mSportsman.setText(record.getSportsman());
        holder.mAbonement.setText("Абонемент № : "+record.getAbonementNum());
        holder.mCount.setText("Неиспользованные тренировки : "+record.getNoUseCount());
        holder.mType.setText(record.getTypeEnding());

        holder.mStartDate.setText(mFormat.format(record.getStart()));
        holder.mEndDate.setText(mFormat.format(record.getEnd()));

        return row;
    }

    public void setData(ArrayList<AbEndingModel> data){
        this.clear();
        this.addAll(data);
    }

    class ViewHolder {
        public TextView mSportsman;
        public TextView mAbonement;
        public TextView mStartDate;
        public TextView mEndDate;
        public TextView mCount;
        public TextView mType;
    }
}