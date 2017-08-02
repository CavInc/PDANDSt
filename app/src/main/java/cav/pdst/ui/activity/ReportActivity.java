package cav.pdst.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.ui.fragments.DatePickerFragment;
import cav.pdst.utils.ConstantManager;
import cav.pdst.utils.SwipeTouchListener;

public class ReportActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,
        DatePickerFragment.OnDateGet{
    private static final String TAG = "RA";
    private final int START_DATE = 0;
    private final int END_DATE = 1;

    private DrawerLayout mNavigationDrawer;

    private DataManager mDataManager;
    private TextView mItogo;
    private TextView mMonth;
    private Button mStartDate;
    private Button mEndDate;
    private Button mDohod;
    private Button mRashod;
    private LinearLayout mMonthLayout;

    private Date mFirstDate;
    private Date mLastDate;

    private int dialogMode;

    private SwipeTouchListener swipeTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mDataManager = DataManager.getInstance();

        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mItogo = (TextView) findViewById(R.id.r_itogo);
        mMonth = (TextView) findViewById(R.id.r_month);
        mStartDate = (Button) findViewById(R.id.r_start_date);
        mEndDate = (Button) findViewById(R.id.r_end_date);
        mDohod = (Button) findViewById(R.id.r_doxod_button);
        mRashod = (Button) findViewById(R.id.r_rashod_button);
        mMonthLayout = (LinearLayout) findViewById(R.id.r_month_l);

        swipeTouchListener = new SwipeTouchListener();
        mMonthLayout.setOnTouchListener(swipeTouchListener);
        mMonthLayout.setOnClickListener(mLayoutClickListener);

        mStartDate.setOnClickListener(this);
        mEndDate.setOnClickListener(this);
        mDohod.setOnClickListener(this);
        mRashod.setOnClickListener(this);
        
        setupCurrentMontData();

        setupToolBar();
        setupDrower();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void setupCurrentMontData() {
        Date dt = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MMM yyyy");
        mMonth.setText(format.format(dt));

        format = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        // первый день месяца
        c.set(Calendar.DAY_OF_MONTH,1);
        mFirstDate = c.getTime();
        mStartDate.setText(format.format(mFirstDate));
        // последний день месяца
        c.setTime(dt);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        mLastDate=c.getTime();
        mEndDate.setText(format.format(c.getTime()));
    }

    private void updateUI(){
        ArrayList<Float> data = mDataManager.getReportAll(mFirstDate,mLastDate);
        if (data.size()!=0) {
            mDohod.setText(String.valueOf(data.get(0)));
            mRashod.setText(String.valueOf(data.get(1)));
            mItogo.setText(String.valueOf(data.get(0)-data.get(1)));
        }else {
            mDohod.setText("0.00");
            mRashod.setText("0.00");
            mItogo.setText("0.00");
        }
    }

    private void setupDrower() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupToolBar() {
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.drawer_tr:
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.drawer_stoptman:
                intent = new Intent(this,SportsmanActivity.class);
                startActivity(intent);
                break;
            case R.id.drawer_group:
                intent = new Intent(this,GroupActivity.class);
                startActivity(intent);
                break;
        }
        mNavigationDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    View.OnClickListener mLayoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG,"CLIK DETECT");
            if (swipeTouchListener.swipeDetected()){
                Log.d(TAG,"SWEEP DETECT");
                if (swipeTouchListener.getAction() == SwipeTouchListener.Action.LR) {
                    Log.d(TAG,"LEFT");
                }
                if (swipeTouchListener.getAction() == SwipeTouchListener.Action.RL) {
                    Log.d(TAG,"RIGTH");
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        DatePickerFragment dialog = DatePickerFragment.newInstance();
        switch (view.getId()){
            case R.id.r_month:
                break;
            case R.id.r_start_date:
                dialogMode = START_DATE;
                dialog.show(getSupportFragmentManager(), ConstantManager.DIALOG_DATE);
                break;
            case R.id.r_end_date:
                dialogMode = END_DATE;
                dialog.show(getSupportFragmentManager(), ConstantManager.DIALOG_DATE);
                break;
            case R.id.r_doxod_button:
                //TODO переделать константы
                Intent dx = new Intent(this,DohodActivity.class);
                dx.putExtra(ConstantManager.AB_STARTDATE,mFirstDate);
                dx.putExtra(ConstantManager.AB_ENDDATE,mLastDate);
                startActivity(dx);
                break;
            case R.id.r_rashod_button:
                Intent rt = new Intent(this,RateActivity.class);
                rt.putExtra(ConstantManager.AB_STARTDATE,mFirstDate);
                rt.putExtra(ConstantManager.AB_ENDDATE,mLastDate);
                startActivity(rt);
                break;
        }
    }

    @Override
    public void OnDateGet(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        if (dialogMode == START_DATE){
            mFirstDate = date;
            mStartDate.setText(format.format(date));
        }
        if (dialogMode == END_DATE){
            mLastDate = date;
            mEndDate.setText(format.format(date));
        }
        updateUI();
    }
}
