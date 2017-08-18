package cav.pdst.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.AbonementModel;
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.services.AlarmTaskReciver;
import cav.pdst.ui.activity.AbonementActivity;
import cav.pdst.ui.adapters.AbonementAdapter;
import cav.pdst.utils.ConstantManager;
import cav.pdst.utils.Utils;


public class SpAbonementFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener {

    private static final String TAG = "SPAB";
    private static final String SPORTSMAN_ID = "SP_ID";
    private static final String MODE = "SP_MODE";
    private static final String SPORTSMAN_NAME = "SP_NAME";
    private ListView mListView;
    private AbonementAdapter mAbonementAdapter;
    private FloatingActionButton mFab;

    private DataManager mDataManager;
    private int sp_id;
    private String sp_name;
    private int mode;

    private int last_ab = 1; // количество абонементов на спортсмене

    private AbonementCallback mAbonementCallback;




    public interface AbonementCallback {
        void updateData(AbonementModel model);
    }

    public static SpAbonementFragment newInstance(SportsmanModel model, int mode){
        Bundle args = new Bundle();
        args.putSerializable(SPORTSMAN_ID,model.getId());
        args.putString(SPORTSMAN_NAME,model.getName());
        args.putSerializable(MODE,mode);
        SpAbonementFragment fragment = new SpAbonementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        this.sp_id = getArguments().getInt(SPORTSMAN_ID);
        this.mode = getArguments().getInt(MODE);
        this.sp_name = getArguments().getString(SPORTSMAN_NAME);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAbonementCallback = (AbonementCallback) activity;
        Log.d("SPA","ATTACH");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAbonementCallback = null;
        Log.d("SPA","DETACH");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        Log.d("SPA","ON CREATE VIRE");
        View rootView = inflater.inflate(R.layout.fragment_sp_abonement, container, false);
        if (mListView == null) {
            mListView = (ListView) rootView.findViewById(R.id.sp_abom_list_view);
            mListView.setOnItemClickListener(this);
        }
        if (mFab == null)
            mFab = (FloatingActionButton) rootView.findViewById(R.id.frm_abonement_fab);

        if (mode != ConstantManager.VIEW_SPORTSMAN) {
            mFab.setOnClickListener(this);
            mListView.setOnItemLongClickListener(this);
        }else {
            mFab.setEnabled(false);
            mFab.setVisibility(View.GONE);
        }
        updateUI();
        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_item:
                return false;
            case R.id.edit_tr_item:
                this.mode = ConstantManager.EDIT_SPORTSMAN;
                mFab.setVisibility(View.VISIBLE);
                mFab.setOnClickListener(this);
                mListView.setOnItemLongClickListener(this);
                this.onStart();
                return true;
        }
        return false;
    }

    public void updateUI(){
        ArrayList<AbonementModel> data = mDataManager.getAbonement(sp_id);
        if (data.size()!= 0) {
            last_ab = data.get(data.size()-1).getId();
            last_ab = last_ab + 1;
        }
        if (mAbonementAdapter == null){
            mAbonementAdapter = new AbonementAdapter(this.getContext(),R.layout.abinement_item,data);
            mListView.setAdapter(mAbonementAdapter);

        }else {
            mAbonementAdapter.setData(data);
            mAbonementAdapter.notifyDataSetChanged();
            mListView.setAdapter(mAbonementAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final AbonementModel model = (AbonementModel) adapterView.getItemAtPosition(position);
        Intent intent = new Intent(SpAbonementFragment.this.getContext(), AbonementActivity.class);
        intent.putExtra(ConstantManager.MODE_ABONEMENT,ConstantManager.VIEW_ABONEMENT);
        intent.putExtra(ConstantManager.AB_DETAIL_DATA,model);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        final AbonementModel model = (AbonementModel) adapterView.getItemAtPosition(position);
        EditDeleteDialog dialog = EditDeleteDialog.newInstance();
        dialog.setEditDeleteDialogListener(new EditDeleteDialog.EditDeleteDialogListener() {
            @Override
            public void onDialogItemClick(int selectItem) {
                if (selectItem==R.id.dialog_del_item) {
                    mDataManager.delAbonememet(model.getId(),model.getSpId());
                    mAbonementAdapter.remove(model);
                }
                if (selectItem == R.id.dialog_edit_item){
                    Intent intent = new Intent(SpAbonementFragment.this.getContext(), AbonementActivity.class);
                    intent.putExtra(ConstantManager.MODE_ABONEMENT,ConstantManager.EDIT_ABONEMENT);
                    intent.putExtra(ConstantManager.AB_DETAIL_DATA,model);
                    startActivityForResult(intent,ConstantManager.EDIT_ABONEMENT);
                }
            }
        });

        FragmentManager fm = getChildFragmentManager();
        dialog.show(fm,ConstantManager.DIALOG_EDIT_DEL);
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this.getContext(), AbonementActivity.class);
        intent.putExtra(ConstantManager.MODE_ABONEMENT,ConstantManager.NEW_ABONEMENT);
        startActivityForResult(intent,ConstantManager.NEW_ABONEMENT);
    }

    @Override
    public void onResume() {
        super.onResume();
        //updateUI();
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
                    int type = data.getIntExtra(ConstantManager.AB_TYPE,0);
                    float debit = data.getFloatExtra(ConstantManager.AB_DEBIT,0.0f);
                    String sDateDebit = data.getStringExtra(ConstantManager.AB_DEBIT_DATETIME);
                    AbonementModel model = Utils.getConvertModel(sp_id,last_ab,createDate,startDate,endDate,countTr,pay,comment);
                    model.setType(type);
                    model.setDebit(debit);
                    if (sDateDebit.length()!=0) {
                        model.setDebitDate(Utils.getSteToDate(sDateDebit, "dd.MM.yyyy HH:mm"));
                    }
                    mAbonementAdapter.add(model);
                    mDataManager.addUpdateAbonement(model);
                    last_ab = last_ab + 1;
                    /*
                    if (sp_id != -1){
                        mDataManager.addUpdateAbonement(model);
                        last_ab = last_ab + 1;
                    }else {
                        mAbonementAdapter.add(model);
                    }
                    */
                    if (model.getDebitDate()!=null){
                        Utils.startAlarm(this.getContext(),model.getDebitDate(),
                                sp_name+" : ("+model.getDebit()+")"
                                        +new SimpleDateFormat("dd.MM.yy HH:mm").format(model.getDebitDate()),
                                model.getSpId());
                    }


                    updateUI();
                    break;
                case ConstantManager.EDIT_ABONEMENT:
                    AbonementModel model2= data.getParcelableExtra(ConstantManager.AB_DETAIL_DATA);
                    mDataManager.addUpdateAbonement(model2);
                    mAbonementAdapter.add(model2);
                    if (model2.getDebitDate()!=null){
                        Utils.startAlarm(this.getContext(),model2.getDebitDate(),
                                sp_name+" : ("+model2.getDebit()+")"
                                        + new SimpleDateFormat("dd.MM.yy HH:mm").format(model2.getDebitDate()),
                                model2.getSpId());
                    }
                    updateUI();
                    break;
            }
        }
    }


}
