package cav.pdst.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.AbonementModel;
import cav.pdst.ui.activity.AbonementActivity;
import cav.pdst.ui.adapters.AbonementAdapter;
import cav.pdst.utils.ConstantManager;


public class SpAbonementFragment extends Fragment implements View.OnClickListener {

    private ListView mListView;
    private AbonementAdapter mAbonementAdapter;
    private FloatingActionButton mFab;

    private DataManager mDataManager;
    private int sp_id;

    public static SpAbonementFragment newInstance(int sp_id){
        SpAbonementFragment fragment = new SpAbonementFragment(sp_id);
        return fragment;
    }

    public SpAbonementFragment(int sp_id){
        mDataManager = DataManager.getInstance();
        this.sp_id = sp_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sp_abonement, container, false);
        mFab = (FloatingActionButton) rootView.findViewById(R.id.frm_abonement_fab);
        mFab.setOnClickListener(this);

        mListView = (ListView) rootView.findViewById(R.id.sp_abom_list_view);
        updateUI();

        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void updateUI(){
        ArrayList<AbonementModel> data = new ArrayList<>();
        if (mAbonementAdapter == null){
            mAbonementAdapter = new AbonementAdapter(this.getContext(),R.layout.abinement_item,data);
            mListView.setAdapter(mAbonementAdapter);
        }else {
            mAbonementAdapter.setData(data);
            mAbonementAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this.getContext(), AbonementActivity.class);
        intent.putExtra(ConstantManager.MODE_ABONEMENT,ConstantManager.NEW_ABONEMENT);
        startActivity(intent);
        startActivityForResult(intent,ConstantManager.NEW_ABONEMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data!=null){
            String createDate;
            String startDate;
            String endDate;
            int countTr;
            float pay;
            String comment;
            switch (requestCode){
                case ConstantManager.NEW_ABONEMENT:
                    createDate = data.getStringExtra(ConstantManager.AB_CREATEDATE);
                    startDate = data.getStringExtra(ConstantManager.AB_STARTDATE);
                    endDate = data.getStringExtra(ConstantManager.AB_ENDDATE);
                    countTr = data.getIntExtra(ConstantManager.AB_COUNT_TR,0);
                    pay = data.getFloatExtra(ConstantManager.AB_PAY,0.0f);
                    comment = data.getStringExtra(ConstantManager.AB_COMMENT);
                    AbonementModel model = getConvertModel(sp_id,-1,createDate,startDate,endDate,countTr,pay,comment);

                    break;
                case ConstantManager.EDIT_ABONEMENT:
                    break;
            }
        }
    }

    private AbonementModel getConvertModel (int sp_id,int id,String createDate,
                                            String startDate,String endDate,
                                            int countTr,float pay,String comment) {
        SimpleDateFormat format = new SimpleDateFormat("E dd.MM.yyyy");
        try {
            return new AbonementModel(id,sp_id,format.parse(createDate),format.parse(startDate),
                    format.parse(endDate),countTr,pay,0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
