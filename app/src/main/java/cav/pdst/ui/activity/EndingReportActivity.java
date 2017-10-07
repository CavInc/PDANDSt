package cav.pdst.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.ui.fragments.DatePickerFragment;
import cav.pdst.utils.ConstantManager;

/**
* Информация о оканчивающихся абонементов
 */

public class EndingReportActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerFragment.OnDateGetListener {

    private final int START_DATE = 0;
    private final int END_DATE = 1;

    private DataManager mDataManager;
    private int dialogMode;
    private Date mFirstDate;
    private Date mLastDate;

    private Button mDateStart;
    private Button mDateEnd;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending_report);

        mDataManager = DataManager.getInstance();

        mDateStart = (Button) findViewById(R.id.er_bt_start);
        mDateEnd = (Button) findViewById(R.id.er_bt_end);
        mListView = (ListView) findViewById(R.id.er_lv);

        mDateStart.setOnClickListener(this);
        mDateEnd.setOnClickListener(this);

        if (mDataManager.getPreferensManager().getDateEndingReport(ConstantManager.STORE_FIRST_REPORT_ED)!= null) {
            mDateStart.setText(mDataManager.getPreferensManager().getDateEndingReport(ConstantManager.STORE_FIRST_REPORT_ED));
        }
        if (mDataManager.getPreferensManager().getDateEndingReport(ConstantManager.STORE_LAST_REPORT_ED)!= null) {
            mDateEnd.setText(mDataManager.getPreferensManager().getDateEndingReport(ConstantManager.STORE_LAST_REPORT_ED));
        }

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
            finish();
            return true;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        DatePickerFragment dialog = DatePickerFragment.newInstance();
        switch (view.getId()){
            case R.id.er_bt_start:
                dialogMode = START_DATE;
                dialog.show(getSupportFragmentManager(), ConstantManager.DIALOG_DATE);
                break;
            case R.id.er_bt_end:
                dialogMode = END_DATE;
                dialog.show(getSupportFragmentManager(), ConstantManager.DIALOG_DATE);
                break;
        }
    }

    @Override
    public void OnDateGet(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        if (dialogMode == START_DATE){
            mFirstDate = date;
            mDateStart.setText(format.format(date));
            mDataManager.getPreferensManager().setDateEndingReport(format.format(date),ConstantManager.STORE_FIRST_REPORT_ED);
        }
        if (dialogMode == END_DATE){
            mLastDate = date;
            mDateEnd.setText(format.format(date));
            mDataManager.getPreferensManager().setDateEndingReport(format.format(date),ConstantManager.STORE_LAST_REPORT_ED);
        }
        updateUI();
    }

    private void updateUI() {

    }
}
