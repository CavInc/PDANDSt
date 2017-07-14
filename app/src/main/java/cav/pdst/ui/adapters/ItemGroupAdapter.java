package cav.pdst.ui.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import cav.pdst.R;
import cav.pdst.data.models.ItemSportsmanModel;

public class ItemGroupAdapter extends ArrayAdapter<ItemSportsmanModel> {
    private LayoutInflater mInflater;
    private int resLayout;

    public ItemGroupAdapter(Context context, int resource, List<ItemSportsmanModel> objects) {
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
            holder.mCheckBox = (CheckBox) row.findViewById(R.id.item_group_item_cb);
            holder.mName = (TextView) row.findViewById(R.id.item_group_item_name);
            holder.mGroup = (TextView) row.findViewById(R.id.item_group_item_group);
            row.setTag(holder);
        }else {
            holder = (ViewHolder)row.getTag();
        }
        ItemSportsmanModel record = getItem(position);
        holder.mName.setText(record.getName());
        holder.mGroup.setText(record.getGroup());
        holder.mCheckBox.setChecked(record.isCheckItem());
        return row;
    }

    private class ViewHolder{
        public CheckBox mCheckBox;
        public TextView mName;
        public TextView mGroup;
    }

}
