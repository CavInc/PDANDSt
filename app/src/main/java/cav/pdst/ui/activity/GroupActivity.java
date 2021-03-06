package cav.pdst.ui.activity;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.GroupModel;
import cav.pdst.ui.adapters.GroupAdapter;
import cav.pdst.ui.fragments.EditDeleteDialog;
import cav.pdst.utils.ConstantManager;

public class GroupActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,AdapterView.OnItemLongClickListener,
        EditDeleteDialog.EditDeleteDialogListener,AdapterView.OnItemClickListener{

    private static final String TAG = "GROUP";
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private ListView mListView;

    private DataManager mDataManager;

    private GroupAdapter adapter;
    private int mItemId;

    private GroupModel mGroupModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mDataManager = DataManager.getInstance();

        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.group_fab);
        mFab.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.group_list_view);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        ArrayList<GroupModel> model = mDataManager.getGroup();


        adapter = new GroupAdapter(this,R.layout.group_item,model);
        mListView.setAdapter(adapter);

        setupToolBar();
        setupDrower();
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrower() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.drawer_group);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.drawer_tr:
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.drawer_stoptman:
                intent = new Intent(this,SportsmanActivity.class);
                startActivity(intent);
                break;
            case R.id.drawer_statistic:
                intent = new Intent(this,ReportActivity.class);
                startActivity(intent);
                break;
            case R.id.drawer_setting:
                intent = new Intent(this,Preferences.class);
                startActivity(intent);
                break;

        }
        mNavigationDrawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.group_fab){
            Intent intent = new Intent(this,ItemGroupActivity.class);
            intent.putExtra(ConstantManager.MODE_GROUP,ConstantManager.NEW_GROUP);
            startActivityForResult(intent, ConstantManager.NEW_GROUP);
        }

    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        mGroupModel = (GroupModel) adapterView.getItemAtPosition(position);
        mItemId = mGroupModel.getId();
        Intent intent = new Intent(this,ItemGroupActivity.class);
        intent.putExtra(ConstantManager.MODE_GROUP,ConstantManager.VIEW_GROUP);
        intent.putExtra(ConstantManager.GROUP_NAME,mGroupModel.getName());
        intent.putExtra(ConstantManager.GROUP_ID,mGroupModel.getId());
        intent.putExtra(ConstantManager.GROUP_COUNT,mGroupModel.getCount());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        mGroupModel = (GroupModel) adapterView.getItemAtPosition(position);
        mItemId = mGroupModel.getId();
        EditDeleteDialog dialog = new EditDeleteDialog();
        dialog.setEditDeleteDialogListener(this);
        dialog.show(getSupportFragmentManager(),ConstantManager.DIALOG_EDIT_DEL);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantManager.NEW_GROUP:
                if (resultCode == RESULT_OK && data !=null){
                    //Integer[] fm = (Integer[]) data.getSerializableExtra(ConstantManager.GROUP_SELECT_ITEM);
                    ArrayList<Integer> fm2 = data.getIntegerArrayListExtra(ConstantManager.GROUP_SELECT_ITEM);
                    Integer[] fm = fm2.toArray(new Integer[fm2.size()]);
                    mDataManager.addGroup(new GroupModel(data.getStringExtra(ConstantManager.GROUP_NAME),fm.length),
                            fm);
                    updateUI();
                }
                break;
            case ConstantManager.EDIT_GROUP:
                if (resultCode == RESULT_OK && data !=null){
                    //Integer[] fm = (Integer[]) data.getSerializableExtra(ConstantManager.GROUP_SELECT_ITEM);
                    ArrayList<Integer> fm2 = data.getIntegerArrayListExtra(ConstantManager.GROUP_SELECT_ITEM);
                    Integer[] fm = fm2.toArray(new Integer[fm2.size()]);
                    GroupModel model =new GroupModel(data.getIntExtra(ConstantManager.GROUP_ID,-1),
                            data.getStringExtra(ConstantManager.GROUP_NAME),
                            data.getIntExtra(ConstantManager.GROUP_COUNT,0));
                    mDataManager.updateGroup(model,fm);
                    updateUI();
                }
                break;
        }
    }


    private void updateUI(){
        ArrayList<GroupModel> model = mDataManager.getGroup();
        if (adapter == null){
            adapter = new GroupAdapter(this,R.layout.group_item,model);
            mListView.setAdapter(adapter);
        }else{
            adapter.setData(model);
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onDialogItemClick(int selectItem) {
        if (selectItem==R.id.dialog_del_item) {
            // удаляем
            mDataManager.delGrop(mItemId);
            updateUI();
        }
        if (selectItem == R.id.dialog_edit_item){
            // редактируем
            Intent intent = new Intent(this,ItemGroupActivity.class);
            intent.putExtra(ConstantManager.MODE_GROUP,ConstantManager.EDIT_GROUP);
            intent.putExtra(ConstantManager.GROUP_NAME,mGroupModel.getName());
            intent.putExtra(ConstantManager.GROUP_ID,mGroupModel.getId());
            intent.putExtra(ConstantManager.GROUP_COUNT,mGroupModel.getCount());
            startActivityForResult(intent, ConstantManager.EDIT_GROUP);
        }
    }


}
