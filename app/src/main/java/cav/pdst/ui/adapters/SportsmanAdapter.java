package cav.pdst.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cav.pdst.R;
import cav.pdst.data.models.SportsmanModel;

public class SportsmanAdapter extends ArrayAdapter<SportsmanModel>{
    private LayoutInflater mInflater;
    private int resLayout;
    private Context mContext;

    public SportsmanAdapter(Context context, int resource, List<SportsmanModel> objects) {
        super(context, resource, objects);
        mContext = context;
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
            holder.mAbCount.setTextColor(ContextCompat.getColor(getContext(),R.color.app_green));
        }else {
            holder.mAbCount.setText(R.string.no_use_abonement);
            holder.mAbCount.setTextColor(ContextCompat.getColor(getContext(),R.color.app_red));
        }
        holder.mTraining.setText("Тренировки: "+record.getTrainingAll()+" / ");
        if (!record.getLastDate().equals("2010-01-01")){
            String l = "";
            try {
                l= new SimpleDateFormat("E dd.MM.yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(record.getLastDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.mLastTraining.setText(mContext.getString(R.string.last_training_data)+" "+l+" "+record.getLastTime());
        }
        return row;
    }

    public void setData(ArrayList<SportsmanModel> data) {
        this.clear();
        this.addAll(data);
    }
/*
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            }
        };
    }
*/
    private class ViewHolder{
        //TODO добавить елементы после построения модели
        public TextView mName;
        public TextView mPhone;
        public TextView mTraining;
        private TextView mAbCount;
        private TextView mLastTraining;

    }
}
