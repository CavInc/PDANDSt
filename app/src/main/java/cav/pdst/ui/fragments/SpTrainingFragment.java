package cav.pdst.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.TrainingModel;
import cav.pdst.ui.adapters.SpTrainingAdapter;


public class SpTrainingFragment extends Fragment {

    private static final String SPORTSMAN_ID = "SP_ID";
    private int sp_id;

    private ListView mListView;
    private SpTrainingAdapter mAdapter;

    private DataManager mDataManager;
    private MaterialCalendarView calendarView;

    public static SpTrainingFragment newInstanse(int sp_id){
        Bundle args = new Bundle();
        args.putSerializable(SPORTSMAN_ID,sp_id);
        SpTrainingFragment fragment = new SpTrainingFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        this.sp_id = getArguments().getInt(SPORTSMAN_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sp_training, container, false);

        Calendar newYear = Calendar.getInstance();
        newYear.add(Calendar.YEAR, 1);
        calendarView = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2016,12,31))
                .setMaximumDate(newYear)
                .commit();

        mListView = (ListView) rootView.findViewById(R.id.sp_info_list_view);


        ArrayList<TrainingModel> model = mDataManager.getTraining(sp_id);
        mAdapter = new SpTrainingAdapter(this.getContext(),R.layout.sp_training_item,model);

        mListView.setAdapter(mAdapter);

        return rootView;
       // return super.onCreateView(inflater, container, savedInstanceState);
    }
}
