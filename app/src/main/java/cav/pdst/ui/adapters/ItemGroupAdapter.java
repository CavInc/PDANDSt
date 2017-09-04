package cav.pdst.ui.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(resLayout, parent, false);
            holder = new ViewHolder();
            holder.mCheckBox = (CheckedTextView) row.findViewById(R.id.item_group_item_cb);
            /*
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.d(TAG,"CHECK POS"+position+" boolean "+b);
                    getItem(position).setCheckItem(b);
                }
            });
            */
            holder.mName = (TextView) row.findViewById(R.id.item_group_item_name);
            holder.mGroup = (TextView) row.findViewById(R.id.item_group_item_group);
            row.setTag(holder);
           // holder.mCheckBox.setTag(getItem(position)); // ? https://habrahabr.ru/post/133575/
        }else {
            holder = (ViewHolder) row.getTag();
            //holder.mCheckBox.setTag(getItem(position));
        }
        ItemSportsmanModel record = getItem(position);
        holder.mName.setText(record.getName());
        if (record.getGroup() != null && record.getGroup().length()!=0) {
            holder.mGroup.setText(record.getGroup());
        }else{
            holder.mGroup.setText("----");
        }
        holder.mCheckBox.setChecked(record.isCheckItem());
        return row;
    }


    private class ViewHolder{
        public CheckedTextView mCheckBox;
        public TextView mName;
        public TextView mGroup;
    }

}
