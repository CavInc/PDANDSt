package cav.pdst.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.ui.fragments.DatePickerFragment;
import cav.pdst.utils.ConstantManager;

public class AbonementActivity extends AppCompatActivity implements View.OnClickListener,DatePickerFragment.OnDateGet {

    private TextView mStartDate;
    private TextView mCreateDate;
    private TextView mEndDate;
    private TextView mCountTraining;
    private TextView mComent;
    private TextView mPay;

    private  int mode;

    private int dMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonement);

        mCreateDate = (TextView) findViewById(R.id.et_create_date);
        mStartDate = (TextView) findViewById(R.id.et_start_date);
        mEndDate = (TextView) findViewById(R.id.et_end_date);
        mPay = (TextView) findViewById(R.id.et_price_ab);
        mCountTraining = (TextView) findViewById(R.id.et_count_tr);
        mComent = (TextView) findViewById(R.id.et_coment);

        mStartDate.setOnClickListener(this);
        mEndDate.setOnClickListener(this);

        SimpleDateFormat format = new SimpleDateFormat("E dd.MM.yyyy");
        mode = getIntent().getIntExtra(ConstantManager.MODE_ABONEMENT,ConstantManager.NEW_ABONEMENT);
        if (mode == ConstantManager.NEW_ABONEMENT) {
            Date date = new Date();
            mCreateDate.setText(format.format(date));
        } else {

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
        Intent answerIntent = new Intent();
        answerIntent.putExtra(ConstantManager.AB_CREATEDATE,mCreateDate.getText().toString());
        answerIntent.putExtra(ConstantManager.AB_STARTDATE,mStartDate.getText().toString());
        answerIntent.putExtra(ConstantManager.AB_ENDDATE,mEndDate.getText().toString());
        answerIntent.putExtra(ConstantManager.AB_COUNT_TR,Integer.parseInt(mCountTraining.getText().toString()));
        answerIntent.putExtra(ConstantManager.AB_COMMENT,mComent.getText().toString());
        answerIntent.putExtra(ConstantManager.AB_PAY,Float.parseFloat(mPay.getText().toString()));
        setResult(RESULT_OK,answerIntent);
    }

    @Override
    public void OnDateGet(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("E dd.MM.yyyy");
        if (dMode == 0 ){
            mStartDate.setText(format.format(date));
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
