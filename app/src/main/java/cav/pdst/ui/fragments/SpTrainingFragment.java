package cav.pdst.ui.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

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
    private Date selectedDate;

    private Collection<CalendarDay> mCalendarDays;

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

        selectedDate = new Date();

        Calendar newYear = Calendar.getInstance();
        newYear.add(Calendar.YEAR, 1);
        calendarView = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2016,12,31))
                .setMaximumDate(newYear)
                .commit();



        /*
        mCalendarDays = new ArrayList<>();

        for (Date l : mDataManager.getTrainingDay(sp_id)) {
            mCalendarDays.add(CalendarDay.from(l));
        }

        calendarView.addDecorator(new StartDayViewDecorator(mCalendarDays));
        */
        //TODO разобраться с асинхоронностью
        // асинхронно грузим данный о испоьзованных днях
        //new LoadUseDay().execute();

        loadUseDay();

        calendarView.setOnDateChangedListener(mDateSelectedListener);

        calendarView.setCurrentDate(new Date());
        calendarView.setDateSelected(new Date(),true);

        mListView = (ListView) rootView.findViewById(R.id.sp_info_list_view);

        updateUI();

        return rootView;
       // return super.onCreateView(inflater, container, savedInstanceState);
    }



    OnDateSelectedListener mDateSelectedListener = new OnDateSelectedListener() {
        @Override
        public void onDateSelected(MaterialCalendarView widget,CalendarDay date, boolean selected) {
            selectedDate = date.getDate();
            updateUI();
        }
    };

    private void updateUI() {
        ArrayList<TrainingModel> model = mDataManager.getTraining(sp_id,selectedDate);
        if (mAdapter == null){
            mAdapter = new SpTrainingAdapter(this.getContext(),R.layout.sp_training_item,model);
            mListView.setAdapter(mAdapter);
        }else {
            mAdapter.setData(model);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void loadUseDay(){
        mCalendarDays = new ArrayList<>();
        for (Date l : mDataManager.getTrainingDay(sp_id)) {
            mCalendarDays.add(CalendarDay.from(l));
        }
        calendarView.addDecorator(new StartDayViewDecorator(mCalendarDays));
    }

    private class StartDayViewDecorator implements DayViewDecorator {

        private final HashSet<CalendarDay> dates;
        private final int color;

        // передали список дат
        public StartDayViewDecorator(Collection<CalendarDay> dates){
            this.color = Color.GREEN;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day); // входит ли обрабатываемая дата в диапазон переданых и если да то вызов decorate;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),R.color.app_green_dark)));
            //view.addSpan(new DotSpan(5, color));
            //view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.custom_select_green_background));
        }
    }

    private class LoadUseDay extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mCalendarDays = new ArrayList<>();

            for (Date l : mDataManager.getTrainingDay(sp_id)) {
                mCalendarDays.add(CalendarDay.from(l));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            calendarView.addDecorator(new StartDayViewDecorator(mCalendarDays));
        }
    }


}
