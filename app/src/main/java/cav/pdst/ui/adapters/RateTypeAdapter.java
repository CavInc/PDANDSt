package cav.pdst.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cav.pdst.R;
import cav.pdst.data.models.RateTypeSpinerModel;

public class  RateTypeAdapter extends ArrayAdapter<RateTypeSpinerModel> {
    private LayoutInflater mInflater;
    private int resLayout;

    public RateTypeAdapter(Context context, int resource, List<RateTypeSpinerModel> objects) {
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
            holder.mName = (TextView) row.findViewById(R.id.rate_type_et);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        RateTypeSpinerModel record = getItem(position);
        holder.mName.setText(record.getName());
        return row;
    }

    public void setData(ArrayList<RateTypeSpinerModel> data) {
        this.clear();
        this.addAll(data);
    }

    class ViewHolder {
        public TextView mName;
    }
}