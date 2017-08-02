package cav.pdst.ui.activity;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        //return super.onOptionsItemSelected(item);
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
            TextView sp = new TextView(this);
            sp.setText(cursor.getString(0));
            TextView pay = new TextView(this);
            pay.setText(String.valueOf(cursor.getFloat(2)));
            TextView period = new TextView(this);
            period.setText(cursor.getString(3)+"-"+cursor.getString(4));

            row.addView(sp);
            row.addView(pay);
            row.addView(period);
            mTableLayout.addView(row);
        }
        mDataManager.getDB().close();
    }
}
