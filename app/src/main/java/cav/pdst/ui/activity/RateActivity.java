package cav.pdst.ui.activity;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.ui.fragments.AddRateDialogFragment;
import cav.pdst.ui.fragments.AddRateTypeDialogFragment;
import cav.pdst.ui.fragments.EditDeleteDialog;
import cav.pdst.utils.ConstantManager;

public class RateActivity extends AppCompatActivity {
    private DataManager mDataManager;
    private Date mStart;
    private Date mEnd;

    private TableLayout mTableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        mDataManager = DataManager.getInstance();

        mStart = (Date) getIntent().getSerializableExtra(ConstantManager.AB_STARTDATE);
        mEnd = (Date) getIntent().getSerializableExtra(ConstantManager.AB_ENDDATE);

        mTableLayout = (TableLayout) findViewById(R.id.rate_tb_l);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rate_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.add_rate){
            AddRateDialogFragment dialog = new AddRateDialogFragment();
            dialog.setAddRateDialogListener(mAddRateDialogListener);
            dialog.show(getSupportFragmentManager(),"ratenew");
        }
        if (item.getItemId() == R.id.add_rate_type){
            AddRateTypeDialogFragment dialogFragment = new AddRateTypeDialogFragment();
            dialogFragment.show(getSupportFragmentManager(),"ratetypenew");
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }

    AddRateDialogFragment.AddRateDialogListener mAddRateDialogListener = new AddRateDialogFragment.AddRateDialogListener(){

        @Override
        public void OnSelected(int rate_type, String create_date, float summ) {
            mDataManager.getDB().addUpdateRate(rate_type,create_date,summ);
            setupTable();
        }
    };

    private void setupTable(){
        mTableLayout.removeAllViews();
        //mTableLayout.setStretchAllColumns(true);
        mTableLayout.setColumnStretchable(0,true);
        mTableLayout.setColumnStretchable(1,true);


        TableRow head = new TableRow(this);
       // TextView h_id = new TextView(this);

        TextView h_sp = new TextView(this);
        h_sp.setText("Тип расхода");
        h_sp.setTextColor(Color.WHITE);
        h_sp.setGravity(Gravity.CENTER);

        TextView h_sum = new TextView(this);
        h_sum.setText("Сумма");
        h_sum.setTextColor(Color.WHITE);
        h_sum.setGravity(Gravity.CENTER);

        head.addView(h_sp);
        head.addView(h_sum);
        //head.addView(h_id);
        head.setBackgroundColor(ContextCompat.getColor(this,R.color.app_green));
        mTableLayout.addView(head);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mDataManager.getDB().open();
        Cursor cursor = mDataManager.getDB().getRateDetail(format.format(mStart),format.format(mEnd));
        while (cursor.moveToNext()){
            TableRow row = new TableRow(this);
            row.setPadding(0,8,0,8);
            TextView sp = new TextView(this);
            sp.setText(cursor.getString(cursor.getColumnIndex("name")));
            TextView pay = new TextView(this);
            pay.setGravity(Gravity.RIGHT);
            pay.setText(String.valueOf(cursor.getFloat(cursor.getColumnIndex("summ"))));
            TextView id = new TextView(this);
            id.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))));
            row.addView(sp);
            row.addView(pay);
            row.addView(id);
            row.setOnLongClickListener(mRowClickListener);
            mTableLayout.addView(row);
        }

        mDataManager.getDB().close();
        mTableLayout.setColumnCollapsed(0,false);
    }

    View.OnLongClickListener mRowClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Log.d("RARE AC"," ROW CLICK");
            TableRow row = (TableRow) view;
            Log.d("RARE_AC", (String) ((TextView) row.getVirtualChildAt(2)).getText());
            final int row_id = Integer.valueOf ((String) ((TextView) row.getVirtualChildAt(2)).getText());
            EditDeleteDialog dialog = EditDeleteDialog.newInstance();
            dialog.setEditDeleteDialogListener(new EditDeleteDialog.EditDeleteDialogListener() {
                @Override
                public void onDialogItemClick(int selectItem) {
                    if (selectItem==R.id.dialog_del_item) {
                        mDataManager.getDB().delRate(row_id);
                        setupTable();
                    }
                }
            });

            dialog.show(getSupportFragmentManager(),"rate_edit_date");
            return false;
        }
    };
}
