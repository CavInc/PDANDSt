package cav.pdst.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cav.pdst.R;
import cav.pdst.data.models.TrainingModel;
import cav.pdst.utils.ConstantManager;

/**
 * Created by cav on 20.07.17.
 */
public class SpTrainingAdapter extends ArrayAdapter<TrainingModel> {
    private LayoutInflater mInflater;
    private int resLayout;
    private ArrayList<TrainingModel> mData;

    public SpTrainingAdapter(Context context, int resource, List<TrainingModel> objects) {
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
            holder.mTime = (TextView) row.findViewById(R.id.sptr_item_datetime);
            holder.mType = (TextView) row.findViewById(R.id.sptr_item_type);
            holder.mName = (TextView) row.findViewById(R.id.sptr_item_name);
            holder.mIcon = (ImageView) row.findViewById(R.id.sptr_item_icon);
            holder.mCount = (TextView) row.findViewById(R.id.sptr_item_count);
            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }
        TrainingModel record = getItem(position);
        //holder.mTime.setText(record.getTitle());
        holder.mName.setText(record.getName());
        SimpleDateFormat format = new SimpleDateFormat("E dd.MM.yyyy");
        holder.mTime.setText(format.format(record.getDate())+" "+record.getTime());
        if (record.getType()== ConstantManager.ONE){
            holder.mType.setText("Спортсмен");
            holder.mIcon.setImageResource(R.drawable.ic_directions_run_black_24dp);
        }else {
            holder.mType.setText("Группа");
            holder.mIcon.setImageResource(R.drawable.ic_group_black_24dp);
        }
        holder.mCount.setText(Integer.toString(record.getCount()));


        return row;
    }

    public void setData(ArrayList<TrainingModel> data) {
        this.clear();
        this.addAll(data);
    }

    class ViewHolder {
        public TextView mTime;
        public TextView mType;
        public TextView mName;
        public ImageView mIcon;
        public TextView mCount;
    }
}
