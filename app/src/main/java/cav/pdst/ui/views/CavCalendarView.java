package cav.pdst.ui.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import cav.pdst.R;

//https://habrahabr.ru/post/124879/
public class CavCalendarView extends LinearLayout {
    private TextView mMonth;
    private ImageView mLeftArrov;
    private ImageView mRightArrov;
    private TableLayout mTableLayout;

    private int mCurrentMonth;
    private int mCurrentYear;


    private final String[] mDayName = {"пн","вт","ср","чт","пт","сб","вс"};
    private Date mFirstDate;
    private Date mLastDate;

    private OnDateChangedListener mDateChangedListener;

    private final String[] mMonths = {"Январь","Февраль","Март",
            "Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь" +
            "Ноябрь","Декабрь"};

    private TextView dxSelect = null;

    // colors
    private int mSelectedColor;
    private int mDayColor;
    private int mWeekColor;

    public interface OnDateChangedListener{
        public void onDateSelected(Date date);
    }

    public CavCalendarView(Context context) {
        this(context,null);
    }

    public CavCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CavCalendarView,0,0);
        mDayColor = a.getColor(R.styleable.CavCalendarView_cc_dayColor,
                getResources().getColor(android.R.color.primary_text_light));
        mWeekColor = a.getColor(R.styleable.CavCalendarView_cc_weekColor,
                getResources().getColor(android.R.color.darker_gray));
        mSelectedColor = a.getColor(R.styleable.CavCalendarView_cc_selectColor,
                getResources().getColor(R.color.colorAccent));
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_calendar_layout, this);

        mMonth = (TextView) findViewById(R.id.cc_title_month);
        mLeftArrov = (ImageView) findViewById(R.id.cc_left_arrroy);
        mRightArrov = (ImageView) findViewById(R.id.cc_right_arroy);

        mLeftArrov.setOnClickListener(mArroyClickListener);
        mRightArrov.setOnClickListener(mArroyClickListener);

        mTableLayout = (TableLayout) findViewById(R.id.cc_table_day);

        setLegend();
        setCurrentDate(new Date());
        updateBody();
    }

    private void setLegend(){
        TableRow head = new TableRow(this.getContext());
        for (int i = 0;i<7;i++){
            TextView tv = new TextView(this.getContext());
            tv.setText(mDayName[i]);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            head.addView(tv);
        }
        mTableLayout.addView(head);
    }
    private void updateBody(){
        // определяем день недели
        Calendar c = Calendar.getInstance();
        //c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(mFirstDate);
        int day = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day == 0 ) day=7;
        day -=1;
        c.setTime(mLastDate);
        int day_cout = c.get(Calendar.DAY_OF_MONTH);
        int line_count = 4;
        if ((day_cout % 7)!=0) line_count = 5;
        int day_i = 1;
        for (int i=1;i<=line_count;i++){
            TableRow line = new TableRow(this.getContext());
            for (int j=0;j<7;j++){
                if (day_i>day_cout) break;
                TextView tx = new TextView(this.getContext());
                if ((j<day) && i==1) {
                    tx.setText(" ");
                }else {
                    tx.setText("" + day_i);
                    tx.setOnClickListener(mItemClickListener);
                    day_i +=1;
                }
                tx.setGravity(Gravity.CENTER);
                line.addView(tx);
            }
            mTableLayout.addView(line);
        }
    }

    OnClickListener mArroyClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.cc_left_arrroy:
                    changeMonth(0);
                    break;
                case R.id.cc_right_arroy:
                    changeMonth(1);
                    break;
            }
        }
    };
    OnClickListener mItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (dxSelect!= null){
                dxSelect.setTextColor(mDayColor);
            }
            TextView dx = (TextView) view;
            Log.d("CCL",dx.getText().toString());
            dx.setTextColor(Color.RED);
            dxSelect = dx;

            Calendar c = Calendar.getInstance();
            if (mDateChangedListener!=null) {
                c.set(mCurrentYear,mCurrentMonth, Integer.parseInt(dx.getText().toString()));
                mDateChangedListener.onDateSelected(c.getTime());
            }
        }
    };

    private void changeMonth(int direction){
        Calendar c = Calendar.getInstance();
        c.set(mCurrentYear,mCurrentMonth,1);
        if (direction == 0) {
            c.add(Calendar.MONTH,-1);
        }else {
            c.add(Calendar.MONTH,1);

        }
        setCurrentDate(c.getTime());
        mTableLayout.removeAllViews();
        setLegend();
        updateBody();
    }

    public void setCurrentDate(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        mCurrentMonth = c.get(Calendar.MONTH);
        mCurrentYear = c.get(Calendar.YEAR);

        // первый день месяца
        c.set(Calendar.DAY_OF_MONTH,1);
        mFirstDate = c.getTime();

        // последний день месяца
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        mLastDate=c.getTime();
        mMonth.setText(mMonths[mCurrentMonth]+" "+mCurrentYear);
    }

    public void setSelectedDate(Date date){

    }

    public void setOnDateChangedListener(OnDateChangedListener listener){
        mDateChangedListener = listener;
    }
}