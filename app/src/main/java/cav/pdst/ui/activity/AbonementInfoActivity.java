package cav.pdst.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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


        setupToolBar();
    }

    private void setupToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
