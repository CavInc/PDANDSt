package cav.pdst.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;


import cav.pdst.R;
import cav.pdst.data.database.DBHelper;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.TrainingModel;
import cav.pdst.ui.adapters.TraningMainAdapter;
import cav.pdst.ui.fragments.EditDeleteDialog;
import cav.pdst.utils.ConstantManager;
import cav.pdst.utils.Utils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        EditDeleteDialog.EditDeleteDialogListener {

    private static final String TAG = "MAIN";
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DrawerLayout mNavigationDrawer;
    private ImageButton mToDay;
    private MaterialCalendarView calendarView;

    private ListView mListView;
    private TraningMainAdapter adapter;

    private DataManager mDataManager;

    private Date selectedDate;
    TrainingModel selModel;
    private Collection<CalendarDay> mCalendarDays;


    //
    // https://github.com/dpreussler/clean-simple-calendar
    // https://github.com/mahendramahi/CalendarView
    // https://github.com/square/android-times-square/blob/master/sample/src/main/res/layout/sample_calendar_picker.xml
    // https://ru.stackoverflow.com/questions/574045/android-range-datepicker-material-design
    //https://material.io/guidelines/style/color.html#color-color-system

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = DataManager.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToDay = (ImageButton) findViewById(R.id.to_day);
        mToDay.setOnClickListener(mToDayListener);
        mFab = (FloatingActionButton) findViewById(R.id.main_tr_fab);
        mFab.setOnClickListener(mClickListener);
        mListView = (ListView) findViewById(R.id.tr_list_view);
        mListView.setOnItemClickListener(mItemClickListener);
        mListView.setOnItemLongClickListener(mItemLongClickListener);


        Calendar newYear = Calendar.getInstance();
        newYear.add(Calendar.YEAR, 1);

        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2016,12,31))
                //.setMaximumDate(CalendarDay.from(2017,11,30))
                .setMaximumDate(newYear)
                .commit();
        calendarView.setCurrentDate(new Date());
        calendarView.setDateSelected(new Date(),true);
        mCalendarDays = new ArrayList<>();

        calendarView.addDecorator(new StartDayViewDecorator(mCalendarDays));
        calendarView.setOnDateChangedListener(mDateSelectedListener);

        selectedDate = new Date();
        // lock in pay
        /*
        if (new SimpleDateFormat("dd.MM.yyy").format(selectedDate).equals("15.11.2017")) {
            Log.d(TAG,"YES DATE");
            AlertDialog.Builder dialog =  new AlertDialog.Builder(this);
            dialog.setTitle(R.string.app_name)
                    .setMessage("Завершение работы демоверсии")
                    .setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .create();
            dialog.show();

        }
        */

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        // если все пустой запускаем с дефолтовыми настройками. если нет то с настройками из
        // конфига
        if (preference!=null) {
            if (preference.getBoolean("alarm_start_flg",false) && !mDataManager.getPreferensManager().isFirstStart()){
                Utils.restartAlarm(this,0);
            }
        }else {
            Utils.restartAlarm(this,0);
        }

        setupToolBar();
        setupDrower();

        // автоматическая коррекция количества
        mDataManager.getDB().checketCount();
        mDataManager.getDB().checkDelTraining();

        mDataManager.getDB().checkAndCorrectAbonement();

    }

    private void setupDrower() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.drawer_tr);
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
    protected void onResume() {
        super.onResume();
        updateUI(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},ConstantManager.REQUEST_PHONE);

        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},ConstantManager.REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.REQUEST_PHONE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // недали разрешение
                mDataManager.getPreferensManager().setNoPhoneGrand(true);
            }
        }
        if (requestCode == ConstantManager.REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                // обругать что не дали разрешение.
            }
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
                intent = new Intent(this,ReportActivity.class);
                startActivity(intent);
                break;
            case R.id.drawer_setting:
                intent = new Intent(this,Preferences.class);
                startActivity(intent);
                break;
            /*
            case R.id.drawer_send_sd:
                sendSD();
                break;
                */

        }
        mNavigationDrawer.closeDrawer(GravityCompat.START);
        return false;
    }



    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Calendar c = Calendar.getInstance();
            final int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            Intent intent = new Intent(MainActivity.this,TrainingActivity.class);
            intent.putExtra(ConstantManager.MODE_TRAINING,ConstantManager.NEW_TRAINING);
            intent.putExtra(ConstantManager.TRAINING_HOUR,hour);
            intent.putExtra(ConstantManager.TRAINING_MINUTE,minute);
            intent.putExtra(ConstantManager.TRAINING_DATE,selectedDate);
            startActivity(intent);
        }
    };
   View.OnClickListener mToDayListener = new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           selectedDate = new Date();
           calendarView.clearSelection();
           calendarView.setCurrentDate(selectedDate);
           calendarView.setDateSelected(selectedDate,true);
           updateUI(1);
       }
   };

    private int selectID = -1;

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            selModel = (TrainingModel) adapterView.getItemAtPosition(position);
            selectID = selModel.getId();
            Intent intent = new Intent(MainActivity.this,TrainingActivity.class);
            intent.putExtra(ConstantManager.MODE_TRAINING,ConstantManager.VIEW_TRAINING);
            intent.putExtra(ConstantManager.TRAINING_DATE,selModel.getDate());
            intent.putExtra(ConstantManager.TRAINING_OBJECT,selModel);
            startActivity(intent);
        }
    };

    AdapterView.OnItemLongClickListener mItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            selModel = (TrainingModel) adapterView.getItemAtPosition(position);
            selectID = selModel.getId();
            EditDeleteDialog dialog = new EditDeleteDialog();
            dialog.setEditDeleteDialogListener(MainActivity.this);
            dialog.show(getSupportFragmentManager(),ConstantManager.DIALOG_EDIT_DEL);
            return true;
        }
    };

    OnDateSelectedListener mDateSelectedListener = new OnDateSelectedListener() {
        @Override
        public void onDateSelected(MaterialCalendarView widget,CalendarDay date, boolean selected) {
            selectedDate = date.getDate();
            updateUI(1);
        }
    };

    private void updateUI(int mode){
        ArrayList<TrainingModel> model = mDataManager.getTraining(selectedDate);
        if (adapter == null){
            adapter = new TraningMainAdapter(this,R.layout.main_item,model);
            mListView.setAdapter(adapter);
        }else {
            adapter.setData(model);
            adapter.notifyDataSetChanged();
        }
        if (mode == 0) {
            mCalendarDays.clear();
            for (Date l : mDataManager.getTrainingDay()) {
                mCalendarDays.add(CalendarDay.from(l));
            }
            calendarView.removeDecorators();
            calendarView.addDecorator(new StartDayViewDecorator(mCalendarDays));
        }
    }

    @Override
    public void onDialogItemClick(int selectItem) {
        if (selectItem==R.id.dialog_del_item) {
            // удаляем
            mDataManager.delTraining(selectID);
            updateUI(0);
        }
        if (selectItem == R.id.dialog_edit_item){
            // редактируем

            Intent intent = new Intent(MainActivity.this,TrainingActivity.class);
            intent.putExtra(ConstantManager.MODE_TRAINING,ConstantManager.EDIT_TRAINING);
            intent.putExtra(ConstantManager.TRAINING_DATE,selModel.getDate());
            intent.putExtra(ConstantManager.TRAINING_OBJECT,selModel);

            startActivity(intent);
        }
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
            //view.setSelectionDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.calendar_day_selector_out_range_transparent));
           // view.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.calendar_day_selector_in_range));

            view.addSpan(new ForegroundColorSpan(Color.WHITE));
          //  view.addSpan(new ForegroundColorSpan(Color.GREEN));
          //  view.addSpan(new DotSpan(5, color));
          //  view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(MainActivity.this,R.color.app_green_dark)));
            view.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.custom_select_green_background));
        }
    }

    // перенос базы на SD
    private void sendSD() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(getApplicationContext(),
                    "SD-карта не доступна: " + Environment.getExternalStorageState(),
                    Toast.LENGTH_LONG).show();
        } else {
            // получаем путь к SD
            File path = new File (Environment.getExternalStorageDirectory(), "PDANDST");
            if (! path.exists()) {
                if (!path.mkdirs()){
                    Toast.makeText(getApplicationContext(),
                            "Каталог не создан: " + path.getAbsolutePath(),
                            Toast.LENGTH_LONG).show();
                    return ;
                }
                Log.d(TAG,getDatabasePath(DBHelper.DATABASE_NAME).getAbsolutePath());

                // in
                File fin = new File (getDatabasePath(DBHelper.DATABASE_NAME).getAbsolutePath());

                // выходной файл
                File fOut = new File(path, "CH_PDT"+".db3");

                try {
                    InputStream in = new FileInputStream(fin);
                    OutputStream out = new FileOutputStream(fOut);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    // Закрываем потоки
                    in.close();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }
}
