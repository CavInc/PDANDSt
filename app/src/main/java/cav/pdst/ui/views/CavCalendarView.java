package cav.pdst.ui.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.utils.SwipeTouchListener;

//https://habrahabr.ru/post/124879/
public class CavCalendarView extends LinearLayout  {
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
    private GestureDetector mGestureDetector;

    private final String[] mMonths = {"Январь","Февраль","Март",
            "Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь",
            "Ноябрь","Декабрь"};

    private TextView dxSelect = null;

    // colors
    private int mSelectedColor;
    private int mDayColor;
    private int mWeekColor;



    //https://ru.stackoverflow.com/questions/560926/%D0%9A%D0%B0%D0%BA-%D0%BE%D1%82%D1%81%D0%BB%D0%B5%D0%B4%D0%B8%D1%82%D1%8C-swipe-%D0%B8%D0%BC%D0%B5%D0%BD%D0%BD%D0%BE-%D1%87%D1%82%D0%BE-%D0%BF%D0%B0%D0%BB%D1%8C%D1%86%D0%B5%D0%BC-%D0%BF%D1%80%D0%BE%D0%B2%D0%B5%D0%BB%D0%B8-%D0%BF%D0%BE-textview
    //https://habrahabr.ru/post/120931/
    //http://developer.alexanderklimov.ru/android/views/imageswitcher.php#gesture
    //https://habrahabr.ru/sandbox/76660/

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

        SwipeTouchListener swipeTouchListener = new SwipeTouchListener();
        //mGestureDetector = new GestureDetector(this.getContext(),new SwipeGestureDetector());
        LinearLayout mLayout = (LinearLayout) findViewById(R.id.cc_calendar_layout);
       // mLayout.setOnTouchListener(swipeTouchListener);
        /*
        mLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("SLL","TOUCH");
                return false;
            }
        });
        */

        swipeTouchListener.setSwipeListener(mDetectListener);

        setLegend();
        setCurrentDate(new Date());
        updateBody();
    }

    private void setLegend(){
        TableRow head = new TableRow(this.getContext());
        for (int i = 0;i<7;i++){
            TextView tv = new TextView(this.getContext());
            tv.setTextColor(mWeekColor);
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
        //int line_count = 4;
        //if ((day_cout % 7)!=0) line_count = 5;
        int line_count = 5;
        int day_i = 1;
        for (int i=1;i<=line_count;i++){
            TableRow line = new TableRow(this.getContext());
            for (int j=0;j<7;j++){
                if (day_i>day_cout) break;
                TextView tx = new TextView(this.getContext());
                tx.setPadding(0,8,0,8);
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
                dxSelect.setBackgroundColor(Color.TRANSPARENT);
            }
            TextView dx = (TextView) view;
            Log.d("CCL",dx.getText().toString());
            dx.setTextColor(mSelectedColor);
            //dx.setBackground(android.R.drawable.stat_notify_sync);
            dx.setBackgroundResource(R.drawable.today_circle_background);
            dxSelect = dx;

            Calendar c = Calendar.getInstance();
            if (mDateChangedListener!=null) {
                c.set(mCurrentYear,mCurrentMonth, Integer.parseInt(dx.getText().toString()));
                mDateChangedListener.onDateSelected(c.getTime());
            }
        }
    };

    SwipeTouchListener.SwipeDetectListener mDetectListener = new SwipeTouchListener.SwipeDetectListener() {
        @Override
        public void OnSwipeDirection(SwipeTouchListener.Action direct) {
            if (direct == SwipeTouchListener.Action.LR) {
                changeMonth(0);
            }
            if (direct == SwipeTouchListener.Action.RL) {
                changeMonth(1);
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

    //TODO  не запоминает дату выбранную при листании месяца будет скидывать
    public void setSelectedDate(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        if (y!=mCurrentYear) return;
        if (m!=mCurrentMonth) return;


    }

    public void setOnDateChangedListener(OnDateChangedListener listener){
        mDateChangedListener = listener;
    }

    private void onRightSwipe() {
        Log.d("CCL","Right");
    }

    private void onLeftSwipe() {
        Log.d("CCL","Left");
    }


    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 50;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try{
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();
                if (diffAbs > SWIPE_MAX_OFF_PATH) return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onLeftSwipe();
                }// Right swipe
                else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onRightSwipe();
                }

            }catch (Exception e){

            }
            return true;
        }
    }
}