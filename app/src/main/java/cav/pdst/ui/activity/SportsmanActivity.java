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
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.ui.adapters.SportsmanAdapter;
import cav.pdst.ui.fragments.EditDeleteDialog;
import cav.pdst.utils.ConstantManager;

public class SportsmanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.
        OnItemLongClickListener,View.OnClickListener,EditDeleteDialog.EditDeleteDialogListener,AdapterView.OnItemClickListener {

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DrawerLayout mNavigationDrawer;

    private ListView mListView;

    private DataManager mDataManager;

    private SportsmanAdapter adapter;

    private int selId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsman);

        mDataManager = DataManager.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mFab = (FloatingActionButton) findViewById(R.id.main_tr_fab);
        mFab.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.sportsman_list_view);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnItemClickListener(this);


        setupToolBar();
        setupDrower();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void setupDrower() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI(){
        ArrayList<SportsmanModel> model= mDataManager.getSportsman();
        if (adapter == null ) {
            adapter = new SportsmanAdapter(this, R.layout.sportsman_item, model);
            mListView.setAdapter(adapter);
        }else {
            adapter.setData(model);
            adapter.notifyDataSetChanged();
        }
    }



    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        SportsmanModel model = (SportsmanModel) adapterView.getItemAtPosition(position);
        selId = model.getId();
        EditDeleteDialog dialog = new EditDeleteDialog();
        dialog.show(getFragmentManager(),ConstantManager.DIALOG_EDIT_DEL);
        return true;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        SportsmanModel model = (SportsmanModel) adapterView.getItemAtPosition(position);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.drawer_tr:
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.drawer_group:
                intent = new Intent(this,GroupActivity.class);
                startActivity(intent);
                break;
            case R.id.drawer_statistic:
                break;
            case R.id.drawer_setting:
                break;

        }
        mNavigationDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,SportsmanDetailActivity.class);
        intent.putExtra(ConstantManager.MODE_SP_DETAIL,ConstantManager.NEW_SPORTSMAN);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data !=null){

        }
    }

    @Override
    public void onDialogItemClick(int selectItem) {
        if (selectItem==R.id.dialog_del_item) {
            // удаляем
            mDataManager.delSportsman(selId);
            //TODO сделать удаление елемента из адаптера не трогая весь
            updateUI();
        }
        if (selectItem == R.id.dialog_edit_item){
            // редактируем
            SportsmanModel model = adapter.getItem(selId);
            Intent intent = new Intent(this,SportsmanDetailActivity.class);
            intent.putExtra(ConstantManager.MODE_SP_DETAIL,ConstantManager.EDIT_SPORTSMAN);
            intent.putExtra(ConstantManager.SP_DETAIL_DATA,model);
            startActivity(intent);
        }
    }

}
