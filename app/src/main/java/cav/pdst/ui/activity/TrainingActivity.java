package cav.pdst.ui.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import cav.pdst.data.models.TestAbonementModel;
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
    private Spinner mRepeatSpiner;

    private DataManager mDataManager;

    private String mTime;

    private TrainingAdapter mAdapter;
    private int mode;

    private int hour;
    private int minute;

    private Date mDate;
    private TrainingModel mModel;

    private int group_id = -1;

    private Menu mMenu;

    private String [] repeat_type = {"нет повторений","ежедневно","еженедельно","ежемесячно"};

    private int training_repeat=0;

    private boolean noSelected = false;

    private ArrayList<SpRefAbModeModel> chekItems; // сохраняем выделенные элементы.

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
        mRepeatSpiner = (Spinner) findViewById(R.id.repeat_spiner);

        mDataButton = (Button) findViewById(R.id.date_button);
        mDataButton.setOnClickListener(this);
        mTimeButton = (Button) findViewById(R.id.time_button);
        mTimeButton.setOnClickListener(this);
        mTimeButton.setText(Utils.formatTime(hour,minute));
        mTime = Utils.formatTime(hour,minute);

        mTraining.setText(R.string.training_default_name);

        if (mode == ConstantManager.EDIT_TRAINING || mode == ConstantManager.VIEW_TRAINING) {
            mTraining.setText(mModel.getName());
            mCountSportsman.setText(getString(R.string.count_training_sportsman)+" "+mModel.getCount());
        } else {
            mCountSportsman.setText(getString(R.string.count_training_sportsman)+" 0");
        }

        if (mode == ConstantManager.VIEW_TRAINING) {
            changeEditable(false);
        }

        mListView.setOnItemClickListener(mItemClickListener);

        ArrayList<TrainingGroupModel> spinnerData = mDataManager.getGroupString();
        spinnerData.add(0,new TrainingGroupModel(-1,"Все"));
        ArrayAdapter<TrainingGroupModel> spinnerAdapter = new ArrayAdapter<TrainingGroupModel>(this,android.R.layout.simple_spinner_item,spinnerData);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);

        ArrayAdapter<String> repeatAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,repeat_type);
        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRepeatSpiner.setAdapter(repeatAdapter);

        setDateButton(mDate);
        setupToolBar();
        setRepeatSpinerListener();
        //setSpinerListener();
        if (mode !=  ConstantManager.NEW_TRAINING) {
            mRepeatSpiner.setSelection(mModel.getRepeatType());
        }
    }

    private void setupToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trainint_menu, menu);
        if (mode == ConstantManager.VIEW_TRAINING) {
            mMenu.findItem(R.id.add_sportsman_item).setVisible(false);
            mMenu.findItem(R.id.save_item).setVisible(false);
            mMenu.findItem(R.id.edit_tr_item).setVisible(true);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.save_item) {
            if (mTraining.getText().toString().length()!=0) {
                saveResult();
            }
            onBackPressed();
        }
        if (item.getItemId() == R.id.add_sportsman_item) {
            Intent intent = new Intent(this,SportsmanDetailActivity.class);
            intent.putExtra(ConstantManager.MODE_SP_DETAIL,ConstantManager.NEW_SPORTSMAN);
            startActivityForResult(intent,ConstantManager.NEW_SPORTSMAN);
        }
        if (item.getItemId() == R.id.edit_tr_item) {
            mMenu.findItem(R.id.add_sportsman_item).setVisible(true);
            mMenu.findItem(R.id.save_item).setVisible(true);
            mMenu.findItem(R.id.edit_tr_item).setVisible(false);
            mode = ConstantManager.EDIT_TRAINING;
            changeEditable(true);
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                        String tm = Utils.formatTime(hours,minutes);
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

    private void changeEditable(boolean flg) {
        mTraining.setFocusable(flg);
        mTraining.setFocusableInTouchMode(true);
        mTraining.requestFocus();
        mTraining.setEnabled(flg);
<<<<<<< HEAD
        mListView.setEnabled(flg);
        //mListView.setClickable(flg);
=======
        //mListView.setEnabled(flg);
        mListView.setClickable(flg);
>>>>>>> 1560cc424583f277516b97be546580073ea2aeb2

        mTimeButton.setEnabled(flg);
        mDataButton.setEnabled(flg);
        mSpinner.setEnabled(flg);
        mRepeatSpiner.setEnabled(flg);
        noSelected = !flg;
    }


    private void updateUI(){
       // Log.d(TAG,"UPDATE");
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
           // Log.d(TAG,"UPDATE NEW ");
            setChekItems(model);
        }else {
            // -- тут заполнить модель из checkItems
            setChesketModel(model);
            mAdapter.setData(model);
            mAdapter.notifyDataSetChanged();
            //Log.d(TAG,"UPDATE OLD");
        }
    }

    private void setChesketModel(ArrayList<SportsmanTrainingModel> model) {
        for (SpRefAbModeModel l : chekItems){
            int pos = model.indexOf(new SportsmanTrainingModel(l.getSpId(),l.getAbonement(),l.getMode()));
            if (pos !=-1){
                model.get(pos).setCheck(true);
            }
        }
    }

    // устанавливает отмеченные елементы
    private void setChekItems(ArrayList<SportsmanTrainingModel> model){
        if (chekItems == null) {
            chekItems = new ArrayList<>();
        }
        for (SportsmanTrainingModel l:model){
            if (l.isCheck()) {
                chekItems.add(new SpRefAbModeModel(
                        l.getId(),
                        l.getLinkAbonement(),
                        l.getMode()
                ));
            }
        }
    }

    private void setSpinerListener(){
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                TrainingGroupModel model = (TrainingGroupModel) adapterView.getItemAtPosition(position);
                group_id = model.getId();
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setRepeatSpinerListener(){
        mRepeatSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                training_repeat = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void changeCountSportsman(boolean mode){
        int count = Integer.parseInt(mCountSportsman.getText().toString().split(":")[1].trim());
        if (mode){
            count += 1;
        } else {
            count -=1;
        }
       mCountSportsman.setText(getString(R.string.count_training_sportsman)+" "+count);
    }

    private int selSportspam;

    //private LinkSpABTrModel mLinkSpABTrModel;
    private TestAbonementModel ab;

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
            if (noSelected) return;
            SportsmanTrainingModel mx = (SportsmanTrainingModel) adapterView.getItemAtPosition(position);
            selSportspam = mx.getId();
            if (mx.getCount() == 0 && (!mx.isCheck())){
                InfoDialogFragment dialog= new InfoDialogFragment();
                dialog.show(getFragmentManager(),"INFODIALOG");
                return;
            }
            if (!mx.isCheck()){
                //TODO действия по привязке тренировки к абонементу
                // по id спростмена получаем его абонементы у которых дата действия в диапазоне тренировки
                // из всего списка если несколько то возвращаестя тот у кого младший номер и есть не распределеннны тренировки
                ab = getAbonement(mx.getId(),mDate);
                if (ab.getId() == -1) {
                    // показать что куй ?
                    AlertDialog.Builder dialog =new AlertDialog.Builder(TrainingActivity.this)
                            .setTitle("Предупреждение")
                            .setMessage("Не подходящий абонемент")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton(R.string.button_ok,null);
                    dialog.show();
                    return;
                }
                //mLinkSpABTrModel = new LinkSpABTrModel(mx.getId(),ab);

                TrainigOperationFragment dialog = TrainigOperationFragment.newInstance(ab.getWorking(),ab.getTraining());
                dialog.setTrainingOperationListener(new TrainigOperationFragment.TrainingOperationListener() {
                    @Override
                    public void onTrainingClickListener(int witch) {
                        mAdapter.getItem(position).setMode(witch);

                        mAdapter.getItem(position).setLinkAbonement(ab.getId());

                        mAdapter.getItem(position).setCheck(true);
                        mAdapter.getItem(position).setCount(mAdapter.getItem(position).getCount()-1);
                        mAdapter.notifyDataSetChanged();
                        // установили метку
                        chekItems.add(new SpRefAbModeModel(
                                mAdapter.getItem(position).getId(),
                                mAdapter.getItem(position).getLinkAbonement(),
                                mAdapter.getItem(position).getMode()));

                        changeCountSportsman(true);
                    }
                });
                dialog.show(getFragmentManager(),"OPERATIONDIALOG");
            } else {
                // сняли метку
                int ir = chekItems.indexOf(new SpRefAbModeModel(
                        mAdapter.getItem(position).getId(),
                        mAdapter.getItem(position).getLinkAbonement(),
                        mAdapter.getItem(position).getMode()));

                Log.d(TAG, String.valueOf(ir));

                chekItems.remove(new SpRefAbModeModel(
                        mAdapter.getItem(position).getId(),
                        mAdapter.getItem(position).getLinkAbonement(),
                        mAdapter.getItem(position).getMode()));

                //снятие абонемента
                mAdapter.getItem(position).setLinkAbonement(-1);
                ((SportsmanTrainingModel) adapterView.getItemAtPosition(position)).setCheck(! mx.isCheck());
                ((SportsmanTrainingModel) adapterView.getItemAtPosition(position)).setMode(-1);
                ((SportsmanTrainingModel) adapterView.getItemAtPosition(position))
                        .setCount(((SportsmanTrainingModel) adapterView.getItemAtPosition(position)).getCount()+1);
                mAdapter.notifyDataSetChanged();

                changeCountSportsman(false);
            }
        }
    };

    private TestAbonementModel getAbonement(int id, Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<TestAbonementModel> rec = new ArrayList<>();
        mDataManager.getDB().open();
        Cursor cursor = mDataManager.getDB().getAbonementInDate(id,format.format(date));
        while (cursor.moveToNext()){
            rec.add(new TestAbonementModel(cursor.getInt(0),cursor.getInt(1)-cursor.getInt(2),
                    cursor.getInt(3)));
        }
        mDataManager.getDB().close();
        if (rec.size()!=0) {
            return rec.get(0);
        }else {
            return new TestAbonementModel(-1,0,0);
        }
    }

    // выделенные элементы
    private ArrayList<SpRefAbModeModel> getCheckElement(){
        /*
        ArrayList<SpRefAbModeModel> rec = new ArrayList<>();
        for (int i=0;i<mAdapter.getCount();i++){
            if (mAdapter.getItem(i).isCheck()) {
                rec.add(new SpRefAbModeModel(mAdapter.getItem(i).getId(),
                        mAdapter.getItem(i).getLinkAbonement(),
                        mAdapter.getItem(i).getMode()));
            }
        }
        return rec;
        */
        return chekItems;
    }

    // сохраняем данных о тренировки и спрортсменах
    private void saveResult(){
        if (mode == ConstantManager.VIEW_TRAINING) return;

        ArrayList<SpRefAbModeModel> fm = getCheckElement();
        int type = ConstantManager.ONE;
        if (fm.size()>0) type = ConstantManager.GROUP;
        TrainingModel model = new TrainingModel(mTraining.getText().toString(), type, fm.size(), mDate, mTime);
        model.setRepeatType(training_repeat);
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
                int type = data.getIntExtra(ConstantManager.AB_TYPE,0);
                float debit = data.getFloatExtra(ConstantManager.AB_DEBIT,0.0f);
                AbonementModel model = Utils.getConvertModel(selSportspam,mx,createDate,startDate,endDate,countTr,pay,comment);
                model.setType(type);
                model.setDebit(debit);
                mDataManager.addUpdateAbonement(model);
                updateUI();
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == ConstantManager.NEW_SPORTSMAN){
            updateUI();
        }

    }

}


