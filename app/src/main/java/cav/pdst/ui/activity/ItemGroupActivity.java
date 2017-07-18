package cav.pdst.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import cav.pdst.R;
import cav.pdst.data.models.GroupModel;
import cav.pdst.data.models.ItemSportsmanModel;
import cav.pdst.ui.adapters.ItemGroupAdapter;
import cav.pdst.utils.ConstantManager;

public class ItemGroupActivity extends AppCompatActivity  {

    private static final String TAG = "ITEMGROUP";
    private EditText mNameGroup;
    private ListView mListView;

    private int mode;

    private GroupModel mGroupModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_group);

        mNameGroup = (EditText) findViewById(R.id.group_name_edit);

        mListView = (ListView) findViewById(R.id.item_group_list_view);

        mode = getIntent().getIntExtra(ConstantManager.MODE_GROUP,ConstantManager.NEW_GROUP);

        if (mode == ConstantManager.EDIT_GROUP) {
            mGroupModel = new GroupModel(getIntent().getIntExtra(ConstantManager.GROUP_ID,-1),
                    getIntent().getStringExtra(ConstantManager.GROUP_NAME),getIntent().getIntExtra(ConstantManager.GROUP_COUNT,0));
            mNameGroup.setText(mGroupModel.getName());
        }

        ArrayList<ItemSportsmanModel> models = new ArrayList<>();
        models.add(new ItemSportsmanModel(false,"Иванов",0,"-"));


        ItemGroupAdapter adapter = new ItemGroupAdapter(this,R.layout.item_group_item,models);
        mListView.setAdapter(adapter);

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
            Log.d(TAG,"HOME BACK");
            //saveResult();
            onBackPressed();
            //finish();
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveResult();
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



    private void saveResult(){
        if (mNameGroup.getText().length()!=0) {
            Intent answerIntent = new Intent();
            answerIntent.putExtra(ConstantManager.GROUP_NAME, mNameGroup.getText().toString());
            if (mode == ConstantManager.EDIT_GROUP){
                answerIntent.putExtra(ConstantManager.GROUP_ID,mGroupModel.getId());
                answerIntent.putExtra(ConstantManager.GROUP_COUNT,mGroupModel.getCount());
            }
            setResult(RESULT_OK, answerIntent);
        }
    }
}
