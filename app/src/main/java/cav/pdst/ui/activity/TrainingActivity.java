package cav.pdst.ui.activity;

import android.app.TimePickerDialog;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.SportsmanTrainingModel;
import cav.pdst.data.models.TrainingModel;
import cav.pdst.ui.adapters.TrainingAdapter;
import cav.pdst.ui.fragments.DatePickerFragment;
import cav.pdst.utils.ConstantManager;

public class TrainingActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tren);

        mDataManager = DataManager.getInstance();
        mode = getIntent().getIntExtra(ConstantManager.MODE_TRAINING,ConstantManager.NEW_TRAINING);
        hour = getIntent().getIntExtra(ConstantManager.TRAINING_HOUR,-1);
        minute = getIntent().getIntExtra(ConstantManager.TRAINING_MINUTE,-1);


        mListView = (ListView) findViewById(R.id.training_list_view);
        mTraining = (EditText) findViewById(R.id.training_edit);
        mCountSportsman = (TextView) findViewById(R.id.count_sportsman);

        mDataButton = (Button) findViewById(R.id.date_button);
        mDataButton.setOnClickListener(this);
        mTimeButton = (Button) findViewById(R.id.time_button);
        mTimeButton.setOnClickListener(this);
        mTimeButton.setText(String.valueOf(hour)+":"+String.valueOf(minute));
        mTime = String.valueOf(hour)+":"+String.valueOf(minute);

        // все спортсмены у указанием количества тренировок
        ArrayList<SportsmanTrainingModel> model = mDataManager.getSpTraining();

        mAdapter = new TrainingAdapter(this,R.layout.training_item,model);
        mListView.setAdapter(mAdapter);

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
        Log.d(TAG,"BACKUP");
        if (mTraining.getText().toString().length()!=0) {
            TrainingModel model = new TrainingModel(mTraining.getText().toString(), ConstantManager.ONE, 0, new Date(), mTime);
            mDataManager.addTraining(model);
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
}
