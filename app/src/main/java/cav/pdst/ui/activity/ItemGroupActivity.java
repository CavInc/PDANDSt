package cav.pdst.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.GroupModel;
import cav.pdst.data.models.ItemSportsmanModel;
import cav.pdst.ui.adapters.ItemGroupAdapter;
import cav.pdst.utils.ConstantManager;

public class ItemGroupActivity extends AppCompatActivity  {

    private static final String TAG = "ITEMGROUP";
    private EditText mNameGroup;
    private ListView mListView;

    private int mode;
    private DataManager mDataManager;

    private GroupModel mGroupModel;
    private ItemGroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_group);
        mDataManager = DataManager.getInstance();

        mNameGroup = (EditText) findViewById(R.id.group_name_edit);

        mListView = (ListView) findViewById(R.id.item_group_list_view);

        mode = getIntent().getIntExtra(ConstantManager.MODE_GROUP,ConstantManager.NEW_GROUP);

        int id=-1;

        if (mode == ConstantManager.EDIT_GROUP) {
            mGroupModel = new GroupModel(getIntent().getIntExtra(ConstantManager.GROUP_ID,-1),
                    getIntent().getStringExtra(ConstantManager.GROUP_NAME),getIntent().getIntExtra(ConstantManager.GROUP_COUNT,0));
            mNameGroup.setText(mGroupModel.getName());
            id = mGroupModel.getId();
        }

        ArrayList<ItemSportsmanModel> models = mDataManager.getSportsmanInGroup(id);
       // models.add(new ItemSportsmanModel(false,"Иванов",0,"-"));


        adapter = new ItemGroupAdapter(this,R.layout.item_group_item,models);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(mItemClickListener);

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //saveResult();
            onBackPressed();
            //finish();
        }
        if (item.getItemId() == R.id.save_item){
            saveResult();
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
       // saveResult();
        super.onBackPressed();
    }

    private void changeState(int mode){
        if (mode == 1){
            mNameGroup.setEnabled(true);
            mNameGroup.setFocusable(true);
            mNameGroup.requestFocus();
        }else{
            mNameGroup.setEnabled(false);
            mNameGroup.setFocusable(false);
        }
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ItemSportsmanModel model = (ItemSportsmanModel) adapterView.getItemAtPosition(position);
           ((ItemSportsmanModel) adapterView.getItemAtPosition(position)).setCheckItem(!model.isCheckItem());
            adapter.notifyDataSetChanged();
        }
    };

    private ArrayList<Integer> getCheckElement(){
        ArrayList<Integer> rec = new ArrayList<>();
        for (int i=0;i<adapter.getCount();i++){
            if (adapter.getItem(i).isCheckItem()){
                rec.add(adapter.getItem(i).getId());
            }
        }
        //return rec.toArray(new Integer[rec.size()]);
        return rec;
    }


    private void saveResult(){
        if (mNameGroup.getText().length()!=0) {
            //Integer[] selItem = getCheckElement();
            ArrayList<Integer> selItem = getCheckElement();
            Intent answerIntent = new Intent();
            answerIntent.putExtra(ConstantManager.GROUP_NAME, mNameGroup.getText().toString());
            if (mode == ConstantManager.EDIT_GROUP){
                mGroupModel.setCount(selItem.size());
                answerIntent.putExtra(ConstantManager.GROUP_ID,mGroupModel.getId());
                answerIntent.putExtra(ConstantManager.GROUP_COUNT,mGroupModel.getCount());
            }
            //answerIntent.putExtra(ConstantManager.GROUP_SELECT_ITEM,selItem);
            answerIntent.putIntegerArrayListExtra(ConstantManager.GROUP_SELECT_ITEM,selItem);
            setResult(RESULT_OK, answerIntent);
        }
    }
}
