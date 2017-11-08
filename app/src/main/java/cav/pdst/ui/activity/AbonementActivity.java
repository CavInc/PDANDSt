package cav.pdst.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.AbonementModel;
import cav.pdst.ui.fragments.DatePickerFragment;
import cav.pdst.ui.fragments.DateTimeFragment;
import cav.pdst.utils.ConstantManager;
import cav.pdst.utils.Utils;

public class AbonementActivity extends AppCompatActivity implements View.OnClickListener,DatePickerFragment.OnDateGetListener {

    private TextView mStartDate;
    private TextView mCreateDate;
    private TextView mEndDate;
    private TextView mCountTraining;
    private TextView mComent;
    private TextView mPay;
    private EditText mDebit;
    private Button mDebitDate;

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
        mDebit = (EditText) findViewById(R.id.et_debit);
        mDebitDate = (Button) findViewById(R.id.button_debit_date);

        mSpinner = (Spinner) findViewById(R.id.tv_spiner_type);
        ArrayAdapter<String> spinerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ab_type);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinerAdapter);
        mSpinner.setOnItemSelectedListener(mItemSelected);

        mCreateDate.setOnClickListener(this);
        mStartDate.setOnClickListener(this);
        mEndDate.setOnClickListener(this);
        mDebitDate.setOnClickListener(this);

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
            mDebit.setText(String.valueOf(mAbonementModel.getDebit()));
            if (mAbonementModel.getDebitDate()!=null){
                mDebitDate.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(mAbonementModel.getDebitDate()));
            }
        }
        if (mode == ConstantManager.VIEW_ABONEMENT) {
            mCreateDate.setEnabled(false);
            mStartDate.setEnabled(false);
            mEndDate.setEnabled(false);
            mCountTraining.setEnabled(false);
            mPay.setEnabled(false);
            mComent.setEnabled(false);
            mSpinner.setEnabled(false);
            mDebit.setEnabled(false);
            mDebitDate.setEnabled(false);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.all_save_menu, menu);
        if (mode == ConstantManager.VIEW_ABONEMENT) {
            menu.findItem(R.id.save_item).setVisible(false);
           // menu.findItem(R.id.edit_tr_item).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.save_item) {
            saveResult();
            onBackPressed();
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
       // saveResult();
        super.onBackPressed();
    }

    public void saveResult(){
        if (mStartDate.getText().toString().length()==0) {
            return;
        }
        if (mode == ConstantManager.VIEW_ABONEMENT) return;;

        Intent answerIntent = new Intent();
        if (mode == ConstantManager.NEW_ABONEMENT) {
            answerIntent.putExtra(ConstantManager.AB_CREATEDATE, mCreateDate.getText().toString());
            answerIntent.putExtra(ConstantManager.AB_STARTDATE, mStartDate.getText().toString());
            answerIntent.putExtra(ConstantManager.AB_ENDDATE, mEndDate.getText().toString());
            if (mCountTraining.getText().toString().length()!=0 ) {
                answerIntent.putExtra(ConstantManager.AB_COUNT_TR, Integer.parseInt(mCountTraining.getText().toString()));
            }  else {
                answerIntent.putExtra(ConstantManager.AB_COUNT_TR, 0);
            }
            answerIntent.putExtra(ConstantManager.AB_COMMENT, mComent.getText().toString());
            if (mPay.getText().toString().length() != 0) {
                answerIntent.putExtra(ConstantManager.AB_PAY, Float.parseFloat(mPay.getText().toString()));
            } else {
                answerIntent.putExtra(ConstantManager.AB_PAY,0.0f);
            }
            answerIntent.putExtra(ConstantManager.AB_TYPE,mAbType);
            if (mDebit.getText().toString().length()!=0) {
                answerIntent.putExtra(ConstantManager.AB_DEBIT, Float.parseFloat(mDebit.getText().toString()));
            }else {
                answerIntent.putExtra(ConstantManager.AB_DEBIT,0.0f);
            }
            answerIntent.putExtra(ConstantManager.AB_DEBIT_DATETIME,mDebitDate.getText().toString());
        }
        if (mode == ConstantManager.EDIT_ABONEMENT) {
            mAbonementModel.setStartDate(Utils.getSteToDate(mStartDate.getText().toString(),"E dd.MM.yyyy"));
            mAbonementModel.setEndDate(Utils.getSteToDate(mEndDate.getText().toString(),"E dd.MM.yyyy"));
            mAbonementModel.setCountTraining(Integer.parseInt(mCountTraining.getText().toString()));
            mAbonementModel.setComment(mComent.getText().toString());
            mAbonementModel.setPay(Float.parseFloat(mPay.getText().toString()));
            mAbonementModel.setDebit(Float.parseFloat(mDebit.getText().toString()));
            if (mDebitDate.getText().toString().length()!=0) {
                mAbonementModel.setDebitDate(Utils.getSteToDate(mDebitDate.getText().toString(), "dd.MM.yyyy HH:mm"));
            }
            if (mAbonementModel.getUsedTraining()>mAbonementModel.getCountTraining()){
                showInfoDialog();
                return;
            }
            answerIntent.putExtra(ConstantManager.AB_DETAIL_DATA, mAbonementModel);
        }
        setResult(RESULT_OK,answerIntent);


    }

    private void showInfoDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Внимание !")
                .setMessage("Нельзя поставить количество тренировок меньше использованного")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(R.string.dialog_yes,null);
        dialog.show();

    }

    AdapterView.OnItemSelectedListener mItemSelected = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            // номер позиции равен типу 0- абонемент 1- разовое занятие
            mAbType = position;
            if (position==1) {
                mCountTraining.setText("1");
                mCountTraining.setVisibility(View.GONE);
                findViewById(R.id.tv_end_date).setVisibility(View.GONE);
                findViewById(R.id.tv_count_tr).setVisibility(View.GONE);
                mEndDate.setVisibility(View.GONE);
                mDebit.setVisibility(View.GONE);
                mDebitDate.setVisibility(View.GONE);
                findViewById(R.id.tv_debit_ti).setVisibility(View.GONE);
                findViewById(R.id.tv_alarm_txt).setVisibility(View.GONE);
            } else {
                mCountTraining.setVisibility(View.VISIBLE);
                mEndDate.setVisibility(View.VISIBLE);
                findViewById(R.id.tv_end_date).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_count_tr).setVisibility(View.VISIBLE);
                mDebit.setVisibility(View.VISIBLE);
                mDebitDate.setVisibility(View.VISIBLE);
                findViewById(R.id.tv_debit_ti).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_alarm_txt).setVisibility(View.VISIBLE);
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
        } else if (dMode == 1){
            mEndDate.setText(format.format(date));
        } else if (dMode == 2){
            mCreateDate.setText(format.format(date));
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
            case R.id.et_create_date:
                dMode = 2;
                dialog.show(getSupportFragmentManager(),ConstantManager.DIALOG_DATE);
                break;
            case R.id.button_debit_date:
                DateTimeFragment dateTimeFragment = new DateTimeFragment();
                dateTimeFragment.setOnDateTimeChangeListener(new DateTimeFragment.OnDateTimeChangeListener() {
                    @Override
                    public void OnDateTimeChange(Date date) {
                        mDebitDate.setText(new SimpleDateFormat("dd.MM.yyy HH:mm").format(date));
                    }
                });
                dateTimeFragment.show(getSupportFragmentManager(),"date_time");
                break;
        }

    }
}
