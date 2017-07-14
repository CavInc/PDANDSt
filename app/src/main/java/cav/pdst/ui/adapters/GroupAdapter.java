package cav.pdst.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cav.pdst.R;
import cav.pdst.data.models.GroupModel;


public class GroupAdapter extends ArrayAdapter<GroupModel> {

    private LayoutInflater mInflater;
    private int resLayout;

    public GroupAdapter(Context context, int resource, List<GroupModel> objects) {
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
            holder.mName = (TextView) row.findViewById(R.id.group_item_name);
            holder.mCount = (TextView) row.findViewById(R.id.group_item_count);
            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }
        GroupModel record = getItem(position);
        holder.mName.setText(record.getName());
        holder.mCount.setText(Integer.toString(record.getCount()));
        return row;
    }

    private class ViewHolder{
        public TextView mName;
        public TextView mCount;
    }
}
