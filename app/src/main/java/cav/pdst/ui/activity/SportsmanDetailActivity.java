package cav.pdst.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
        setSupportActionBar(toolbar);
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
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        if (item.getItemId() == R.id.save_item) {
            saveData();
            setResult(RESULT_OK,null);
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //saveData();
        super.onBackPressed();
    }

    @Override
    public void updateData(SportsmanModel model) {
        mSportsmanModel=model;
    }

    private void saveData(){
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


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return SpInfoFragment.newInstance(mSportsmanModel,mode);
                case 1:
                    return SpTrainingFragment.newInstanse(sp_id);
                case 2:
                    return SpAbonementFragment.newInstance(sp_id,mode);

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
