package cav.pdst.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import cav.pdst.utils.ConstantManager;

public class SportsmanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.
        OnItemLongClickListener,View.OnClickListener {

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DrawerLayout mNavigationDrawer;

    private ListView mListView;

    private DataManager mDataManager;

    private SportsmanAdapter adapter;

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

    private void updateUI(){
        ArrayList<SportsmanModel> model= mDataManager.getSportsman();
        if (adapter == null ) {
            adapter = new SportsmanAdapter(this, R.layout.sportsman_item, model);
            mListView.setAdapter(adapter);
        }else {

            adapter.notifyDataSetChanged();
        }
    }



    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,SportsmanDetailActivity.class);
        intent.putExtra(ConstantManager.MODE_SP_DETAIL,ConstantManager.NEW_SPORTSMAN);
        startActivity(intent);
    }
}
