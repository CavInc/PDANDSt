package cav.pdst.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.AbonementModel;
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.ui.fragments.SpAbonementFragment;
import cav.pdst.ui.fragments.SpInfoFragment;
import cav.pdst.ui.fragments.SpTrainingFragment;
import cav.pdst.utils.ConstantManager;


public class SportsmanDetailActivity extends AppCompatActivity implements SpInfoFragment.Callbacks,
        SpAbonementFragment.AbonementCallback {

    private static final String TAG = "SPDETAIL";
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private SportsmanModel mSportsmanModel;
    private AbonementModel mAbonementModel;

    private int mode;

    private DataManager mDataManager;
    private int sp_id = -1;

    private Menu mMenu;

    private boolean return_flg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsman_detail);

        mDataManager = DataManager.getInstance();

        mode = getIntent().getIntExtra(ConstantManager.MODE_SP_DETAIL,ConstantManager.NEW_SPORTSMAN);
        if ((mode == ConstantManager.EDIT_SPORTSMAN) || (mode == ConstantManager.VIEW_SPORTSMAN)) {
            mSportsmanModel = getIntent().getParcelableExtra(ConstantManager.SP_DETAIL_DATA);
            sp_id = mSportsmanModel.getId();
        }

        if (mode == ConstantManager.ALARM_SPORTSMAN){
            return_flg = true;
            mSportsmanModel = mDataManager.getSportsman(getIntent().getIntExtra(ConstantManager.ALARM_ID,-1));
            sp_id = mSportsmanModel.getId();
            mode = ConstantManager.VIEW_SPORTSMAN;
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setupToolBar();
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar!=null  && mode != ConstantManager.NEW_SPORTSMAN){
            toolbar.setTitle(mSportsmanModel.getName());
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.all_save_menu, menu);
        if (mode == ConstantManager.VIEW_SPORTSMAN) {
            mMenu.findItem(R.id.save_item).setVisible(false);
            mMenu.findItem(R.id.edit_tr_item).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            mDataManager.getDB().delAbonement();
            onBackPressed();
        }
        if (item.getItemId() == R.id.save_item) {
            saveData();
            setResult(RESULT_OK,null);
            onBackPressed();
        }
        if (item.getItemId() == R.id.edit_tr_item) {
            mMenu.findItem(R.id.save_item).setVisible(true);
            mMenu.findItem(R.id.edit_tr_item).setVisible(false);
            mode = ConstantManager.EDIT_SPORTSMAN;
            switch (mViewPager.getCurrentItem()){
                case 0:
                    //((SpInfoFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem())).setMode(mode);
                    break;
                case 2:
                    break;
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //saveData();
        super.onBackPressed();
        if (return_flg) {
            Intent intent = new Intent(this,SportsmanActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void updateData(SportsmanModel model) {
        mSportsmanModel=model;
    }

    private void saveData(){
        if (mode == ConstantManager.VIEW_SPORTSMAN) return;

        if (mSportsmanModel.getName().length()!=0){
            if (mode == ConstantManager.NEW_SPORTSMAN) {
                mDataManager.addSportsman(mSportsmanModel);
            } else if (mode == ConstantManager.EDIT_SPORTSMAN) {
                mDataManager.updateSportsman(mSportsmanModel);
            }
        }else{
            mDataManager.getDB().delAbonement();
        }

    }

    @Override
    public void updateData(AbonementModel model) {
        mAbonementModel = model;
    }

    //
    // FragmentPagerAdapter   FragmentStatePagerAdapter
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("SPF","COUNT +"+this.getCount());
            switch (position){
                case 0:
                    return SpInfoFragment.newInstance(mSportsmanModel,mode);
                case 1:
                    return SpTrainingFragment.newInstanse(sp_id);
                case 2:
                    return SpAbonementFragment.newInstance(mSportsmanModel,mode);

            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Инфо";
                case 1:
                    return "Тренировки";
                case 2:
                    return "Абонементы";

            }
            return null;
        }
    }
}
