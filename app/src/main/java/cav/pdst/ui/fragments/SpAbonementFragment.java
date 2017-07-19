package cav.pdst.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.AbonementModel;
import cav.pdst.ui.activity.AbonementActivity;
import cav.pdst.ui.adapters.AbonementAdapter;


public class SpAbonementFragment extends Fragment implements View.OnClickListener {

    private ListView mListView;
    private AbonementAdapter mAbonementAdapter;
    private FloatingActionButton mFab;

    private DataManager mDataManager;

    public static SpAbonementFragment newInstance(){
        SpAbonementFragment fragment = new SpAbonementFragment();
        return fragment;
    }

    public SpAbonementFragment(){
        mDataManager = DataManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sp_abonement, container, false);
        mFab = (FloatingActionButton) rootView.findViewById(R.id.frm_abonement_fab);
        mFab.setOnClickListener(this);

        mListView = (ListView) rootView.findViewById(R.id.sp_abom_list_view);

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
        startActivity(intent);
    }
}
