package cav.pdst.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.AbonementModel;
import cav.pdst.utils.ConstantManager;

public class AbonementInfoActivity extends AppCompatActivity {

    private DataManager mDataManager;
    private AbonementModel mAbonementModel;

    private TextView mStartDate;
    private TextView mCreateDate;
    private TextView mEndDate;
    private TextView mCountTraining;
    private TextView mPay;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonement_info);

        mDataManager = DataManager.getInstance();
        mAbonementModel = getIntent().getParcelableExtra(ConstantManager.AB_DETAIL_DATA);

        mCreateDate = (TextView) findViewById(R.id.ab_create_date);
        mStartDate = (TextView) findViewById(R.id.ab_start_date);
        mEndDate = (TextView) findViewById(R.id.ab_end_date);
        mPay = (TextView) findViewById(R.id.ab_pay_summ);
        mCountTraining = (TextView) findViewById(R.id.ab_training);

        mListView = (ListView) findViewById(R.id.abonement_info_lv);

        SimpleDateFormat format = new SimpleDateFormat("E dd.MM.yyyy");

        mCreateDate.setText(getString(R.string.create_date)+":"+format.format(mAbonementModel.getCreateDate()));
        mStartDate.setText(format.format(mAbonementModel.getStartDate()));
        mEndDate.setText(format.format(mAbonementModel.getEndDate()));
        mPay.setText(String.valueOf(mAbonementModel.getPay()));
        mCountTraining.setText(("Использовано: "+mAbonementModel.getUsedTraining()+
                " из "+mAbonementModel.getCountTraining()+
                ", Доступно: "+((mAbonementModel.getCountTraining())-(mAbonementModel.getUsedTraining()+mAbonementModel.getWarning()))));


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
}
