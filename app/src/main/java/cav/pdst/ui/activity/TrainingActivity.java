package cav.pdst.ui.activity;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.SportsmanTrainingModel;
import cav.pdst.data.models.TrainingModel;
import cav.pdst.ui.adapters.TrainingAdapter;
import cav.pdst.ui.fragments.DatePickerFragment;
import cav.pdst.utils.ConstantManager;

public class TrainingActivity extends AppCompatActivity implements View.OnClickListener,DatePickerFragment.OnDateGet {

    private static final String TAG = "TRA";
    private ListView mListView;
    private EditText mTraining;
    private TextView mCountSportsman;

    private Button mTimeButton;
    private Button mDataButton;

    private DataManager mDataManager;

    private String mTime;

    private TrainingAdapter mAdapter;
    private int mode;

    private int hour;
    private int minute;

    private Date mDate;
    private TrainingModel mModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tren);

        mDataManager = DataManager.getInstance();
        mode = getIntent().getIntExtra(ConstantManager.MODE_TRAINING,ConstantManager.NEW_TRAINING);
        if (mode == ConstantManager.NEW_TRAINING) {
            hour = getIntent().getIntExtra(ConstantManager.TRAINING_HOUR, -1);
            minute = getIntent().getIntExtra(ConstantManager.TRAINING_MINUTE, -1);
        } else {
            mModel = getIntent().getParcelableExtra(ConstantManager.TRAINING_OBJECT);
            String[] tm = mModel.getTime().split(":");
            hour = Integer.parseInt(tm[0]);
            minute = Integer.parseInt(tm[1]);

        }
        Bundle buingle = getIntent().getExtras();
        mDate = (Date) buingle.get(ConstantManager.TRAINING_DATE);


        mListView = (ListView) findViewById(R.id.training_list_view);
        mTraining = (EditText) findViewById(R.id.training_edit);
        mCountSportsman = (TextView) findViewById(R.id.count_sportsman);

        mDataButton = (Button) findViewById(R.id.date_button);
        mDataButton.setOnClickListener(this);
        mTimeButton = (Button) findViewById(R.id.time_button);
        mTimeButton.setOnClickListener(this);
        mTimeButton.setText(String.valueOf(hour)+":"+String.valueOf(minute));
        mTime = String.valueOf(hour)+":"+String.valueOf(minute);

        if (mode == ConstantManager.EDIT_TRAINING) {
            mTraining.setText(mModel.getName());
            mCountSportsman.setText(getString(R.string.count_training_sportsman)+" "+mModel.getCount());
        }

        // все спортсмены у указанием количества тренировок
        ArrayList<SportsmanTrainingModel> model = mDataManager.getSpTraining();

        mAdapter = new TrainingAdapter(this,R.layout.training_item,model);
        mListView.setAdapter(mAdapter);

        setDateButton(mDate);
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
        super.onBackPressed();
        if (mTraining.getText().toString().length()!=0) {
            saveResult();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.date_button:
                final FragmentManager manager = getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance();
                dialog.show(manager, ConstantManager.DIALOG_DATE);
                break;
            case R.id.time_button:
                //final Calendar c = Calendar.getInstance();
                //final int hour = c.get(Calendar.HOUR_OF_DAY);
                //int minute = c.get(Calendar.MINUTE);
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                        mTimeButton.setText(String.valueOf(hours)+":"+String.valueOf(minutes));
                        mTime = String.valueOf(hours)+":"+String.valueOf(minutes);
                    }
                }, hour, minute, true).show();
                break;
        }
    }

    private Integer[] getCheckElement(){
        ArrayList<Integer> rec = new ArrayList<>();
        for (int i=0;i<mAdapter.getCount();i++){
            if (mAdapter.getItem(i).isCheck()) {
                rec.add(mAdapter.getItem(i).getId());
            }
        }
        return rec.toArray(new Integer[rec.size()]);
    }
    private void saveResult(){
        Integer[] fm = getCheckElement();
        int type = ConstantManager.ONE;
        if (fm.length!=0) type = ConstantManager.GROUP;
        TrainingModel model = new TrainingModel(mTraining.getText().toString(), type, fm.length, mDate, mTime);
        if (mode == ConstantManager.NEW_TRAINING) {
            mDataManager.addTraining(model,fm);
        } else {

        }
    }

    private void setDateButton(Date date){
        SimpleDateFormat format = new SimpleDateFormat("E dd.MM.yyyy");
        mDataButton.setText(format.format(date));
    }

    @Override
    public void OnDateGet(Date date) {
        setDateButton(date);
        mDate = date;
    }
}
