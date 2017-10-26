package cav.pdst.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.AbEndingModel;
import cav.pdst.data.models.AbonementModel;
import cav.pdst.ui.adapters.AbonementEndigRepAdapter;
import cav.pdst.ui.fragments.DatePickerFragment;
import cav.pdst.utils.ConstantManager;

/**
* Информация о оканчивающихся абонементов
 */

public class EndingReportActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private final int START_DATE = 0;
    private final int END_DATE = 1;

    private DataManager mDataManager;
    private int dialogMode;
    private Date mFirstDate;
    private Date mLastDate;

    private ListView mListView;

    private AbonementEndigRepAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending_report);

        mDataManager = DataManager.getInstance();


        mListView = (ListView) findViewById(R.id.er_lv);



        mListView.setOnItemClickListener(this);

        updateUI();
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

    }

    private void updateUI() {
          ArrayList<AbEndingModel> model = mDataManager.getDB().getAbonementEnding();
          if (adapter == null) {
               adapter = new AbonementEndigRepAdapter(this, R.layout.abend_item, model);
              mListView.setAdapter(adapter);
          } else {
              adapter.setData(model);
              adapter.notifyDataSetChanged();
          }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        AbEndingModel record = (AbEndingModel) adapterView.getItemAtPosition(position);
        Intent intent = new Intent(this, SportsmanDetailActivity.class);
        intent.putExtra(ConstantManager.ALARM_ID,record.getSpId());
        intent.putExtra(ConstantManager.MODE_SP_DETAIL,ConstantManager.ALARM_SPORTSMAN);
        intent.putExtra(ConstantManager.MODE_RETURN_SP,ConstantManager.RETURN_IN_BACK);
        startActivity(intent);

       /*
        AbEndingModel record = (AbEndingModel) adapterView.getItemAtPosition(position);
        AbonementModel data = mDataManager.getAbonementId(record.getAbonementID());
        Intent intent = new Intent(this, AbonementInfoActivity.class);
        intent.putExtra(ConstantManager.AB_DETAIL_DATA,data);
        startActivity(intent);
        */

    }
}
