package cav.pdst.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;


import cav.pdst.R;
import cav.pdst.data.models.TrainingModel;
import cav.pdst.ui.adapters.TraningMainAdapter;
import cav.pdst.utils.ConstantManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MAIN";
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DrawerLayout mNavigationDrawer;

    private ListView mListView;
    private CalendarView mCalendarView;

    // https://github.com/prolificinteractive/material-calendarview
    // https://github.com/dpreussler/clean-simple-calendar
    // https://github.com/mahendramahi/CalendarView
    // https://github.com/square/android-times-square/blob/master/sample/src/main/res/layout/sample_calendar_picker.xml
    // https://ru.stackoverflow.com/questions/574045/android-range-datepicker-material-design

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mFab = (FloatingActionButton) findViewById(R.id.main_tr_fab);
        mFab.setOnClickListener(mClickListener);
        mListView = (ListView) findViewById(R.id.tr_list_view);


        Calendar newYear = Calendar.getInstance();
        newYear.add(Calendar.YEAR, 1);

        MaterialCalendarView calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2016,12,31))
                .setMaximumDate(newYear)
                .commit();
        calendarView.setCurrentDate(new Date());
        calendarView.setDateSelected(new Date(),true);
        Collection<CalendarDay> m = new ArrayList<>();
        m.add(CalendarDay.from(2017,6,4));
        m.add(CalendarDay.from(2017,6,10));

        calendarView.addDecorator(new StartDayViewDecorator(m));
        calendarView.setOnDateChangedListener(mDateSelectedListener);



        ArrayList<TrainingModel> model = new ArrayList<>();
        model.add(new TrainingModel("Йога", ConstantManager.ONE,0, new Date()));
        model.add(new TrainingModel("Йога + пробежка", ConstantManager.GROUP,0, new Date()));

        TraningMainAdapter adapter = new TraningMainAdapter(this,R.layout.main_item,model);
        mListView.setAdapter(adapter);

        setupToolBar();
        setupDrower();
    }

    private void setupDrower() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.drawer_stoptman:
                intent = new Intent(this,SportsmanActivity.class);
                startActivity(intent);
                break;
            case R.id.drawer_group:
                intent = new Intent(this,GroupActivity.class);
                startActivity(intent);
                break;
            case R.id.drawer_statistic:
                break;
            case R.id.drawer_setting:
                break;

        }
        mNavigationDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,TrainingActivity.class);
            intent.putExtra(ConstantManager.MODE_TRAINING,ConstantManager.NEW_TRAINING);
            startActivity(intent);
        }
    };

    OnDateSelectedListener mDateSelectedListener = new OnDateSelectedListener() {
        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
            Log.d(TAG,"DAY SELECTED "+date.toString());

        }
    };

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
            //view.setSelectionDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.calendar_day_selector_out_range_transparent));
           // view.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.calendar_day_selector_in_range));

          //  view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.addSpan(new ForegroundColorSpan(Color.GREEN));
            view.addSpan(new DotSpan(5, color));

        }
    }
}
