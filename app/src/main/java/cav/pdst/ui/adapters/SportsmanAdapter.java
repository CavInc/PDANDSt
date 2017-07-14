package cav.pdst.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        SportsmanModel record = getItem(position);
        holder.mName.setText(record.getName());
        holder.mPhone.setText(record.getTel());
        return row;
    }

    private class ViewHolder{
        //TODO добавить елементы после построения модели
        public TextView mName;
        public TextView mPhone;

    }
}
