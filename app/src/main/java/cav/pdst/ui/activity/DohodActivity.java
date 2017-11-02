package cav.pdst.ui.activity;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.utils.ConstantManager;

public class DohodActivity extends AppCompatActivity {
    private DataManager mDataManager;

    private TableLayout mTableLayout;

    private Date mStart;
    private Date mEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dohod);
        mDataManager = DataManager.getInstance();

        mTableLayout = (TableLayout) findViewById(R.id.dohod_tb_l);

        mStart = (Date) getIntent().getSerializableExtra(ConstantManager.AB_STARTDATE);
        mEnd = (Date) getIntent().getSerializableExtra(ConstantManager.AB_ENDDATE);

        setupTable();
        setupToolBar();
    }

    private void setupToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void setupTable(){
        mTableLayout.setStretchAllColumns(true);

        TableRow head = new TableRow(this);
        TextView h_sp = new TextView(this);
        h_sp.setText("Спортсмен");
        h_sp.setTextColor(Color.WHITE);
        h_sp.setGravity(Gravity.CENTER);

        TextView h_sum = new TextView(this);
        h_sum.setText("Сумма");
        h_sum.setTextColor(Color.WHITE);
        h_sum.setGravity(Gravity.CENTER);

        TextView h_period = new TextView(this);
        h_period.setText("Период");
        h_period.setTextColor(Color.WHITE);
        h_period.setGravity(Gravity.CENTER);

        head.addView(h_sp);
        head.addView(h_sum);
        head.addView(h_period);
        head.setBackgroundColor(ContextCompat.getColor(this,R.color.app_green));
        mTableLayout.addView(head);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mDataManager.getDB().open();
        Cursor cursor = mDataManager.getDB().getDohodDetail(format.format(mStart),format.format(mEnd));
        while (cursor.moveToNext()){
            TableRow row = new TableRow(this);
            row.setPadding(0,8,0,8);
            TextView sp = new TextView(this);
            sp.setText(cursor.getString(0));
            TextView pay = new TextView(this);
            pay.setText(String.valueOf(cursor.getFloat(2)));
            pay.setBackgroundColor(getResources().getColor(R.color.app_green_light));
            pay.setPadding(4,0,4,0);
            TextView period = new TextView(this);
            try {
                period.setText(new SimpleDateFormat("dd.MM.yyyy").format(format.parse(cursor.getString(3)))
                        +"-"+new SimpleDateFormat("dd.MM.yyyy").format(format.parse(cursor.getString(4))));
            } catch (ParseException e) {

            }

            row.addView(sp);
            row.addView(pay);
            row.addView(period);
            mTableLayout.addView(row);
        }
        mDataManager.getDB().close();
    }
}
