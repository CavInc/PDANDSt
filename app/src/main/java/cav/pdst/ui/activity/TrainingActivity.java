package cav.pdst.ui.activity;


import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.AbonementModel;
import cav.pdst.data.models.SpRefAbModeModel;
import cav.pdst.data.models.SportsmanTrainingModel;
import cav.pdst.data.models.TrainingGroupModel;
import cav.pdst.data.models.TrainingModel;
import cav.pdst.ui.adapters.TrainingAdapter;
import cav.pdst.ui.fragments.DatePickerFragment;
import cav.pdst.ui.fragments.InfoDialogFragment;
import cav.pdst.ui.fragments.TrainigOperationFragment;
import cav.pdst.utils.ConstantManager;
import cav.pdst.utils.Utils;

public class TrainingActivity extends AppCompatActivity implements View.OnClickListener,DatePickerFragment.OnDateGetListener,
        InfoDialogFragment.InfoCallback {

    private static final String TAG = "TRA";
    private ListView mListView;
    private EditText mTraining;
    private TextView mCountSportsman;

    private Button mTimeButton;
    private Button mDataButton;
    private Spinner mSpinner;

    private DataManager mDataManager;

    private String mTime;

    private TrainingAdapter mAdapter;
    private int mode;

    private int hour;
    private int minute;

    private Date mDate;
    private TrainingModel mModel;

    private int group_id = -1;

    //private ArrayList<LinkSpABTrModel> mSpAB = new ArrayList<>();


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
        mSpinner = (Spinner) findViewById(R.id.group_spiner);

        mDataButton = (Button) findViewById(R.id.date_button);
        mDataButton.setOnClickListener(this);
        mTimeButton = (Button) findViewById(R.id.time_button);
        mTimeButton.setOnClickListener(this);
        mTimeButton.setText(formatTime(hour,minute));
        mTime = formatTime(hour,minute);

        if (mode == ConstantManager.EDIT_TRAINING) {
            mTraining.setText(mModel.getName());
            mCountSportsman.setText(getString(R.string.count_training_sportsman)+" "+mModel.getCount());
        }

        mListView.setOnItemClickListener(mItemClickListener);

        ArrayList<TrainingGroupModel> spinnerData = mDataManager.getGroupString();
        spinnerData.add(0,new TrainingGroupModel(-1,"Все"));
        ArrayAdapter<TrainingGroupModel> spinnerAdapter = new ArrayAdapter<TrainingGroupModel>(this,android.R.layout.simple_spinner_item,spinnerData);
        mSpinner.setAdapter(spinnerAdapter);

        setDateButton(mDate);
        setupToolBar();
        //setSpinerListener();
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
                        String tm = formatTime(hours,minutes);
                        mTimeButton.setText(tm);
                        mTime = tm;
                    }
                }, hour, minute, true).show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateUI();
        setSpinerListener();

    }


    private void updateUI(){
        // все спортсмены у указанием количества абонементов
        ArrayList<SportsmanTrainingModel> model = null;
        if (mode == ConstantManager.NEW_TRAINING) {
          model = mDataManager.getSpTraining(group_id);
        } else {
            model = mDataManager.getSpTraining(group_id,mModel.getId());
        }
        if (mAdapter == null){
            mAdapter = new TrainingAdapter(this,R.layout.training_item,model);
            mListView.setAdapter(mAdapter);
        }else {
            mAdapter.setData(model);
            mAdapter.notifyDataSetChanged();
        }

    }

    private void setSpinerListener(){
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d(TAG," SELECT POSITION :"+adapterView.getItemAtPosition(position));
                TrainingGroupModel model = (TrainingGroupModel) adapterView.getItemAtPosition(position);
                Log.d(TAG," ID "+model.getId()+" "+model.getName());
                group_id = model.getId();
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int selSportspam;

    //private LinkSpABTrModel mLinkSpABTrModel;
    private int ab;

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
            Log.d(TAG," POSITION Item "+position);
            SportsmanTrainingModel mx = (SportsmanTrainingModel) adapterView.getItemAtPosition(position);
            Log.d(TAG,mx.getId()+" "+mx.getName()+" "+mx.isCheck());
            selSportspam = mx.getId();
            if (mx.getCount() == 0 ){
                InfoDialogFragment dialog= new InfoDialogFragment();
                dialog.show(getFragmentManager(),"INFODIALOG");
                return;
            }
            if (!mx.isCheck()){
                //TODO действия по привязке тренировки к абонементу
                // по id спростмена получаем его абонементы у которых дата действия в диапазоне тренировки
                // из всего списка если несколько то возвращаестя тот у кого младший номер и есть не распределеннны тренировки
                ab = getAbonement(mx.getId(),mDate);
                if (ab==-1) {
                    // показать что куй ?

                    return;
                }
                //mLinkSpABTrModel = new LinkSpABTrModel(mx.getId(),ab);

                TrainigOperationFragment dialog = new TrainigOperationFragment();
                dialog.setTrainingOperationListener(new TrainigOperationFragment.TrainingOperationListener() {
                    @Override
                    public void onTrainingClickListener(int witch) {
                        Log.d(TAG," ITEM "+witch);
                        mAdapter.getItem(position).setMode(witch);

                        if ((witch == ConstantManager.SPORTSMAN_MODE_TRAINING) || (witch == ConstantManager.SPORTSMAN_MODE_PASS)) {
                            //mSpAB.add(mLinkSpABTrModel);
                            mAdapter.getItem(position).setLinkAbonement(ab);
                        }

                        mAdapter.getItem(position).setCheck(true);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                dialog.show(getFragmentManager(),"OPERATIONDIALOG");
            } else {
                //снятие абонемента
                Log.d(TAG,"СНЯТИЕ ");
                /*
                if (mLinkSpABTrModel != null) {
                    mSpAB.remove(mLinkSpABTrModel);
                    mLinkSpABTrModel = null;
                }
                */
                mAdapter.getItem(position).setLinkAbonement(-1);
                ((SportsmanTrainingModel) adapterView.getItemAtPosition(position)).setCheck(! mx.isCheck());
                ((SportsmanTrainingModel) adapterView.getItemAtPosition(position)).setMode(-1);
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private int getAbonement(int id, Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Integer> rec = new ArrayList<>();
        mDataManager.getDB().open();
        Cursor cursor = mDataManager.getDB().getAbonementInDate(id,format.format(date));
        while (cursor.moveToNext()){
            rec.add(cursor.getInt(0));
        }
        mDataManager.getDB().close();
        if (rec.size()!=0) {
            return rec.get(0);
        }else {
            return -1;
        }
    }


    private ArrayList<SpRefAbModeModel> getCheckElement(){
        ArrayList<SpRefAbModeModel> rec = new ArrayList<>();
        for (int i=0;i<mAdapter.getCount();i++){
            if (mAdapter.getItem(i).isCheck()) {
                rec.add(new SpRefAbModeModel(mAdapter.getItem(i).getId(),
                        mAdapter.getItem(i).getLinkAbonement(),
                        mAdapter.getItem(i).getMode()));
            }
        }
        return rec;
    }

    private void saveResult(){
        ArrayList<SpRefAbModeModel> fm = getCheckElement();
        int type = ConstantManager.ONE;
        if (fm.size()>0) type = ConstantManager.GROUP;
        TrainingModel model = new TrainingModel(mTraining.getText().toString(), type, fm.size(), mDate, mTime);
        if (mode == ConstantManager.NEW_TRAINING) {
            mDataManager.addTraining(model,fm);
        } else {
            model.setId(mModel.getId());
            mDataManager.updateTraining(model,fm);
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

    private String formatTime(int hour,int minute){
        String h = String.valueOf(hour);
        String m = String.valueOf(minute);
        if (h.length()!=2) h = "0"+h;
        if (m.length()!=2) m = "0"+m;
        return h+":"+m;
    }

    @Override
    public void OnInfoButtonClick(int button_id) {
        if (button_id== Dialog.BUTTON_POSITIVE){
            Intent intent = new Intent(this, AbonementActivity.class);
            intent.putExtra(ConstantManager.MODE_ABONEMENT,ConstantManager.NEW_ABONEMENT);
            startActivityForResult(intent,ConstantManager.NEW_ABONEMENT);
        }
    }


    //TODO переделать к хуям на заведение абонемента в самой активности.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data!=null){
            if (requestCode == ConstantManager.NEW_ABONEMENT) {
                int mx = mDataManager.getDB().getLastIndex(selSportspam);

                String createDate = data.getStringExtra(ConstantManager.AB_CREATEDATE);
                String startDate = data.getStringExtra(ConstantManager.AB_STARTDATE);
                String endDate = data.getStringExtra(ConstantManager.AB_ENDDATE);
                int countTr = data.getIntExtra(ConstantManager.AB_COUNT_TR, 0);
                float pay = data.getFloatExtra(ConstantManager.AB_PAY, 0.0f);
                String comment = data.getStringExtra(ConstantManager.AB_COMMENT);
                AbonementModel model = Utils.getConvertModel(selSportspam,mx,createDate,startDate,endDate,countTr,pay,comment);
                mDataManager.addUpdateAbonement(model);
                updateUI();
            }
        }
    }

}


