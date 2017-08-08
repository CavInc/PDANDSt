package cav.pdst.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
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
    private TableRow mHead;

    private int mCurrentMonth;
    private int mCurrentYear;


    private final String[] mDayName = {"пн","вт","ср","чт","пт","сб","вс"};
    private Date mFirstDate;
    private Date mLastDate;

    public CavCalendarView(Context context) {
        this(context,null);
    }

    public CavCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_calendar_layout, this);

        mMonth = (TextView) findViewById(R.id.cc_title_month);
        mLeftArrov = (ImageView) findViewById(R.id.cc_left_arrroy);
        mRightArrov = (ImageView) findViewById(R.id.cc_right_arroy);

        mTableLayout = (TableLayout) findViewById(R.id.cc_table_day);
        mHead = (TableRow) findViewById(R.id.cc_table_legend);

        setLegend();
        setCurrentDate(new Date());
        updateBody();
    }

    private void setLegend(){
        for (int i = 0;i<7;i++){
            TextView tv = new TextView(this.getContext());
            tv.setText(mDayName[i]);
            mHead.addView(tv);
        }
    }
    private void updateBody(){
        // определяем день недели
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(mFirstDate);
        int day = c.get(Calendar.DAY_OF_WEEK);
        Log.d("CCL", String.valueOf(day));
        c.setTime(mLastDate);
        for (int i=1;i<c.get(Calendar.DAY_OF_MONTH);i++){
            TableRow line = new TableRow(this.getContext());
            for (int j=0;j<7;j++){
                TextView tx = new TextView(this.getContext());
                tx.setText(""+i);
                line.addView(tx);
            }
        }
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
    }
}