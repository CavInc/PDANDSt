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
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.ui.adapters.SportsmanAdapter;

public class SportsmanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.
        OnItemLongClickListener,View.OnClickListener {

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DrawerLayout mNavigationDrawer;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsman);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mFab = (FloatingActionButton) findViewById(R.id.main_tr_fab);
        mFab.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.sportsman_list_view);

        ArrayList<SportsmanModel> model= new ArrayList<>();


        SportsmanAdapter adapter = new SportsmanAdapter(this,R.layout.sportsman_item,model);
        mListView.setAdapter(adapter);

        setupToolBar();
        setupDrower();
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
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
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
        startActivity(intent);
    }
}
