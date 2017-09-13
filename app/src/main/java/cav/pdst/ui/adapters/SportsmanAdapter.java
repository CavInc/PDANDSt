package cav.pdst.ui.adapters;

import android.content.ClipData;
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


//http://atrios.ru/%D0%B4%D0%BE%D0%B1%D0%B0%D0%B2%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5-%D1%84%D0%B8%D0%BB%D1%8C%D1%82%D1%80%D0%B0-%D0%BA-%D0%B0%D0%B4%D0%B0%D0%BF%D1%82%D0%B5%D1%80%D1%83-arrayadapre/
//https://stackoverflow.com/questions/19122848/custom-getfilter-in-custom-arrayadapter-in-android
//https://www.survivingwithandroid.com/2012/10/android-listview-custom-filter-and.html
//https://ru.stackoverflow.com/questions/410657/%D0%A4%D0%B8%D0%BB%D1%8C%D1%82%D1%80%D0%B0%D1%86%D0%B8%D1%8F-%D0%BA%D0%B0%D1%81%D1%82%D0%BE%D0%BC%D0%BD%D0%BE%D0%B3%D0%BE-listview-%D0%B8%D0%B7-searchview

public class SportsmanAdapter extends ArrayAdapter<SportsmanModel> implements Filterable{
    private LayoutInflater mInflater;
    private int resLayout;
    private Context mContext;
    private List<SportsmanModel> originalData;

    public SportsmanAdapter(Context context, int resource, List<SportsmanModel> objects) {
        super(context, resource, objects);
        mContext = context;
        resLayout = resource;
        mInflater = LayoutInflater.from(context);
        originalData = objects;
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
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // constraint - параметр фильтра переданный через атрибуты
                if(constraint == null || constraint.length() == 0) {
                    results.values = originalData;
                    results.count = originalData.size();
                } else {
                    // создаём коллекцию, в которой будут храниться отфильтрованные результаты
                    List newList = new ArrayList();
                    // проходимся по коллекции
                    for(SportsmanModel i : originalData) {
                        if (i.toString().toUpperCase().startsWith(constraint.toString().toUpperCase())){
                            newList.add(i);
                        }
                    }
                    // возвращаем новую коллекцию
                    results.values = newList;
                    // и количество результатов
                    results.count = newList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    originalData = (List<SportsmanModel>) filterResults.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
    */

    private class ViewHolder{
        public TextView mName;
        public TextView mPhone;
        public TextView mTraining;
        private TextView mAbCount;
        private TextView mLastTraining;

    }
}
