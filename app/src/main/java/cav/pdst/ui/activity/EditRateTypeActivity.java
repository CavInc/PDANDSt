package cav.pdst.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.RateTypeSpinerModel;
import cav.pdst.ui.adapters.RateTypeAdapter;
import cav.pdst.ui.fragments.AddRateTypeDialogFragment;
import cav.pdst.ui.fragments.EditDeleteDialog;
import cav.pdst.utils.ConstantManager;

public class EditRateTypeActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener,
        EditDeleteDialog.EditDeleteDialogListener{

    private DataManager mDataManager;

    private ListView mListView;
    private RateTypeAdapter mRateTypeAdapter;

    private RateTypeSpinerModel selModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rate_type);

        mDataManager = DataManager.getInstance();

        mListView = (ListView) findViewById(R.id.edit_rate_list);
        mListView.setOnItemLongClickListener(this);

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

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        selModel = (RateTypeSpinerModel) adapterView.getItemAtPosition(position);
        EditDeleteDialog dialog = new EditDeleteDialog();
        dialog.setEditDeleteDialogListener(this);
        dialog.show(getSupportFragmentManager(), ConstantManager.DIALOG_EDIT_DEL);
        return true;
    }

    @Override
    public void onDialogItemClick(int selectItem) {
        if (selectItem==R.id.dialog_del_item) {
            //TODO поставить проверку на то что используем тип
            // удаляем
            mDataManager.getDB().delRateType(selModel.getId());
            updateUI();
        }
        if (selectItem == R.id.dialog_edit_item){
            // редактируем
            AddRateTypeDialogFragment dialog = AddRateTypeDialogFragment.newInstance(selModel);
            dialog.setOnRateTypeChangeListener(mOnRateTypeChangeListener);
            dialog.show(getSupportFragmentManager(),"ratetypeedit");

        }
    }

    AddRateTypeDialogFragment.OnRateTypeChangeListener mOnRateTypeChangeListener = new AddRateTypeDialogFragment.OnRateTypeChangeListener() {

        @Override
        public void OnRateTypeChange() {
            updateUI();
        }
    };
}
