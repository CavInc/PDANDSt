package cav.pdst.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.ui.adapters.SportsmanAdapter;
import cav.pdst.ui.fragments.EditDeleteDialog;
import cav.pdst.utils.ConstantManager;

//https://habrahabr.ru/post/256643/

public class SportsmanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.
        OnItemLongClickListener,View.OnClickListener,EditDeleteDialog.EditDeleteDialogListener,AdapterView.OnItemClickListener {

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DrawerLayout mNavigationDrawer;

    private ListView mListView;

    private DataManager mDataManager;

    private SportsmanAdapter adapter;

    private SportsmanModel selModel;

    private int selId;
    private boolean viewSP = true;

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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Активные"));
        tabLayout.addTab(tabLayout.newTab().setText("Архив"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                if (tab.getPosition()==0){
                    viewSP = true;
                    mFab.setVisibility(View.VISIBLE);
                }else{
                    viewSP = false;
                    mFab.setVisibility(View.INVISIBLE);
                }
                updateUI(viewSP);
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });

        setupToolBar();
        setupDrower();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateUI(viewSP);
    }

    private void setupDrower() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.drawer_stoptman);
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

    private MenuItem searchItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sportsman_menu, menu);

        ImageButton btnToolbarButton = null;
        try {
            Field field = mToolbar.getClass().getDeclaredField("mNavButtonView");
            field.setAccessible(true);
            btnToolbarButton = (ImageButton) field.get(mToolbar);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        searchItem = menu.findItem(R.id.sp_menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if(null!=searchManager ) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("SAA","SUBMIT");
                //adapter.getFilter().filter(query);
                return  false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()==0){
                   // adapter.getFilter().filter(null);
                   // adapter.notifyDataSetChanged();
                   // adapter.notifyDataSetInvalidated();
                   adapter = null;
                   updateUI(viewSP);
                }else {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener(){
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
               // adapter.getFilter().filter(null);
                adapter.notifyDataSetChanged();
                return  true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateUI(boolean mode){
        ArrayList<SportsmanModel> model= mDataManager.getSportsman(mode);
        if (adapter == null ) {
            adapter = new SportsmanAdapter(this, R.layout.sportsman_item, model);
            mListView.setAdapter(adapter);
        }else {
            adapter.setData(model);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (searchItem.isActionViewExpanded()){
            searchItem.collapseActionView();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        selModel = (SportsmanModel) adapterView.getItemAtPosition(position);
        selId = selModel.getId();
        EditDeleteDialog dialog = new EditDeleteDialog();
        dialog.setEditDeleteDialogListener(this);
        dialog.show(getSupportFragmentManager(),ConstantManager.DIALOG_EDIT_DEL);
        return true;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        selModel = (SportsmanModel) adapterView.getItemAtPosition(position);
       // mDataManager.getPreferensManager().saveUseSportsman(selModel.getId()); // сохранили выбранного спортсмена
        Intent intent = new Intent(this,SportsmanDetailActivity.class);
        intent.putExtra(ConstantManager.MODE_SP_DETAIL,ConstantManager.VIEW_SPORTSMAN);
        intent.putExtra(ConstantManager.SP_DETAIL_DATA,selModel);
        startActivity(intent);
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
            updateUI(viewSP);
        }
        if (selectItem == R.id.dialog_edit_item){
            // редактируем
            Intent intent = new Intent(this,SportsmanDetailActivity.class);
            intent.putExtra(ConstantManager.MODE_SP_DETAIL,ConstantManager.EDIT_SPORTSMAN);
            intent.putExtra(ConstantManager.SP_DETAIL_DATA,selModel);
            startActivity(intent);
        }
    }

}
