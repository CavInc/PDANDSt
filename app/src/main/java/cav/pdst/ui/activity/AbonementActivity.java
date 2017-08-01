package cav.pdst.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.AbonementModel;
import cav.pdst.ui.fragments.DatePickerFragment;
import cav.pdst.utils.ConstantManager;
import cav.pdst.utils.Utils;

public class AbonementActivity extends AppCompatActivity implements View.OnClickListener,DatePickerFragment.OnDateGet {

    private static final String TAG = "AB";
    private TextView mStartDate;
    private TextView mCreateDate;
    private TextView mEndDate;
    private TextView mCountTraining;
    private TextView mComent;
    private TextView mPay;

    private  int mode;

    private Spinner mSpinner;

    private int dMode;
    private DataManager mDataManager;
    private AbonementModel mAbonementModel;

    private int mAbType = 0;

    private String[] ab_type = new String[]{"Абонемент","Разовое занятие"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonement);

        mDataManager = DataManager.getInstance();

        mCreateDate = (TextView) findViewById(R.id.et_create_date);
        mStartDate = (TextView) findViewById(R.id.et_start_date);
        mEndDate = (TextView) findViewById(R.id.et_end_date);
        mPay = (TextView) findViewById(R.id.et_price_ab);
        mCountTraining = (TextView) findViewById(R.id.et_count_tr);
        mComent = (TextView) findViewById(R.id.et_coment);
        mSpinner = (Spinner) findViewById(R.id.tv_spiner_type);
        ArrayAdapter<String> spinerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ab_type);
        mSpinner.setAdapter(spinerAdapter);
        mSpinner.setOnItemSelectedListener(mItemSelected);

        mStartDate.setOnClickListener(this);
        mEndDate.setOnClickListener(this);

        SimpleDateFormat format = new SimpleDateFormat("E dd.MM.yyyy");
        mode = getIntent().getIntExtra(ConstantManager.MODE_ABONEMENT,ConstantManager.NEW_ABONEMENT);
        if (mode == ConstantManager.NEW_ABONEMENT) {
            Date date = new Date();
            mCreateDate.setText(format.format(date));
        } else {
            mAbonementModel = getIntent().getParcelableExtra(ConstantManager.AB_DETAIL_DATA);
            mCreateDate.setText(format.format(mAbonementModel.getCreateDate()));
            mStartDate.setText(format.format(mAbonementModel.getStartDate()));
            mEndDate.setText(format.format(mAbonementModel.getEndDate()));
            mCountTraining.setText(String.valueOf(mAbonementModel.getCountTraining()));
            mPay.setText(String.valueOf(mAbonementModel.getPay()));
            mComent.setText(mAbonementModel.getComment());
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
            onBackPressed();
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveResult();
        super.onBackPressed();
    }

    public void saveResult(){
        if (mStartDate.getText().toString().length()==0) {
            //setResult(RESULT_CANCELED,null);
            return;
        }

        Intent answerIntent = new Intent();
        if (mode == ConstantManager.NEW_ABONEMENT) {
            answerIntent.putExtra(ConstantManager.AB_CREATEDATE, mCreateDate.getText().toString());
            answerIntent.putExtra(ConstantManager.AB_STARTDATE, mStartDate.getText().toString());
            answerIntent.putExtra(ConstantManager.AB_ENDDATE, mEndDate.getText().toString());
            answerIntent.putExtra(ConstantManager.AB_COUNT_TR, Integer.parseInt(mCountTraining.getText().toString()));
            answerIntent.putExtra(ConstantManager.AB_COMMENT, mComent.getText().toString());
            answerIntent.putExtra(ConstantManager.AB_PAY, Float.parseFloat(mPay.getText().toString()));
            answerIntent.putExtra(ConstantManager.AB_TYPE,mAbType);
        }
        if (mode == ConstantManager.EDIT_ABONEMENT) {
            mAbonementModel.setStartDate(Utils.getSteToDate(mStartDate.getText().toString(),"E dd.MM.yyyy"));
            mAbonementModel.setEndDate(Utils.getSteToDate(mEndDate.getText().toString(),"E dd.MM.yyyy"));
            mAbonementModel.setCountTraining(Integer.parseInt(mCountTraining.getText().toString()));
            mAbonementModel.setComment(mComent.getText().toString());
            mAbonementModel.setPay(Float.parseFloat(mPay.getText().toString()));
            if (mAbonementModel.getUsedTraining()>mAbonementModel.getCountTraining()){
                //TODO Ругаться !!!!
                return;
            }
            answerIntent.putExtra(ConstantManager.AB_DETAIL_DATA, mAbonementModel);
        }
        setResult(RESULT_OK,answerIntent);

       // mDataManager.getDB().getLastIndex();

    }

    AdapterView.OnItemSelectedListener mItemSelected = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            // номер позиции равен типу 0- абонемент 1- разовое занятие
            Log.d(TAG,"POS "+position);
            mAbType = position;
            if (position==1) {
                mCountTraining.setText("1");
                mCountTraining.setVisibility(View.GONE);
                findViewById(R.id.tv_end_date).setVisibility(View.GONE);
                findViewById(R.id.tv_count_tr).setVisibility(View.GONE);
                mEndDate.setVisibility(View.GONE);
            } else {
                mCountTraining.setVisibility(View.VISIBLE);
                mEndDate.setVisibility(View.VISIBLE);
                findViewById(R.id.tv_end_date).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_count_tr).setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @Override
    public void OnDateGet(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("E dd.MM.yyyy");
        if (dMode == 0 ){
            mStartDate.setText(format.format(date));
            if (mAbType==1) {
                mEndDate.setText(format.format(date));
            }
        } else {
            mEndDate.setText(format.format(date));
        }

    }

    @Override
    public void onClick(View view) {
        DatePickerFragment dialog = DatePickerFragment.newInstance();
        switch (view.getId()){
            case R.id.et_start_date:
                dMode = 0;
                dialog.show(getSupportFragmentManager(), ConstantManager.DIALOG_DATE);
                break;
            case R.id.et_end_date:
                dMode = 1;
                dialog.show(getSupportFragmentManager(), ConstantManager.DIALOG_DATE);
                break;
        }

    }
}
