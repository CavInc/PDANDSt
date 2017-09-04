package cav.pdst.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.RateTypeSpinerModel;
import cav.pdst.ui.adapters.RateTypeAdapter;

public class EditRateTypeActivity extends AppCompatActivity {

    private DataManager mDataManager;

    private ListView mListView;
    private RateTypeAdapter mRateTypeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rate_type);

        mDataManager = DataManager.getInstance();

        mListView = (ListView) findViewById(R.id.edit_rate_list);

        setupToolBar();
        updateUI();
    }

    private void updateUI(){
        ArrayList<RateTypeSpinerModel> model = mDataManager.getRateType();
        if (mRateTypeAdapter == null){
            mRateTypeAdapter = new RateTypeAdapter(this,R.layout.ratetype_edit_item,model);
            mListView.setAdapter(mRateTypeAdapter);
        }else {
            mRateTypeAdapter.setData(model);
            mRateTypeAdapter.notifyDataSetChanged();
        }
    }

    private void setupToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}
