package cav.pdst.ui.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.utils.ConstantManager;

public class SpInfoFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private static final String MODE = "MODE";
    private static final String MODEL = "MODEL";
    private TextView mFullName;
    private TextView mPhone;
    private TextView mComment;

    private Button mCall;
    private Button mSendSMS;
    private CheckBox mCheckBox;

    private SportsmanModel mSportsmanModel;
    private DataManager mDataManager;

    private Callbacks mCallbacks;
    private int mode;

   //private static SpInfoFragment sFragment = null;
    private static boolean oneFrm = false;

    public static SpInfoFragment newInstance(SportsmanModel model,int mode) {
        Bundle args = new Bundle();
        args.putInt(MODE, mode);
        args.putParcelable(MODEL, model);
        /*
        if (sFragment == null) {
            sFragment = new SpInfoFragment();
            sFragment.setArguments(args);
        } else{
            sFragment.mode = mode;
            sFragment.mSportsmanModel = model;
            if (mode == ConstantManager.EDIT_SPORTSMAN) {
                sFragment.setFieldData();
                sFragment.changeMode(true);
            }
        }
        */
        SpInfoFragment sFragment = new SpInfoFragment();
        sFragment.setArguments(args);
        return sFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks  = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDataManager = DataManager.getInstance();
        mode = getArguments().getInt(MODE);
        mSportsmanModel = new SportsmanModel();
        if ((mode == ConstantManager.EDIT_SPORTSMAN) || (mode == ConstantManager.VIEW_SPORTSMAN)){
            mSportsmanModel = getArguments().getParcelable(MODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sp_info, container, false);

        mFullName = (TextView) rootView.findViewById(R.id.info_full_name);
        mPhone = (TextView) rootView.findViewById(R.id.info_phone);
        mComment = (TextView) rootView.findViewById(R.id.info_comment);
        mCheckBox = (CheckBox) rootView.findViewById(R.id.info_cb_arhive);

        mCheckBox.setOnCheckedChangeListener(this);

        mPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        if ((mode == ConstantManager.EDIT_SPORTSMAN) || (mode == ConstantManager.VIEW_SPORTSMAN)) {
            setFieldData();
        }

        setupListener();

        if (mode == ConstantManager.VIEW_SPORTSMAN) {
            changeMode(false);
        }else {
            //setupListener();
        }


        mCall = (Button) rootView.findViewById(R.id.info_call_button);
        mSendSMS = (Button) rootView.findViewById(R.id.info_send_sms);

        mCall.setOnClickListener(mCallListener);
        mSendSMS.setOnClickListener(mSendSMSListener);
        Log.d("SPF","ON VIEW");
        if (!mDataManager.getPreferensManager().getPhoneGrand()){
            mCall.setEnabled(false);
        }



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
                setFieldData();
                changeMode(true);
                return true;
        }
        return false;
    }


    public void setMode(int mode){
        this.mode = mode;
        setFieldData();
        changeMode(true);
    }

    private void setFieldData(){
        mFullName.setText(mSportsmanModel.getName());
        mPhone.setText(mSportsmanModel.getTel());
        mComment.setText(mSportsmanModel.getComment());
        mCheckBox.setChecked(!mSportsmanModel.isActive());
    }

    private void changeMode(boolean flg){
        mFullName.setEnabled(flg);
        mPhone.setEnabled(flg);
        mComment.setEnabled(flg);

        mFullName.setFocusable(flg);
        mPhone.setFocusable(flg);
        mComment.setFocusable(flg);

        mFullName.setFocusableInTouchMode(flg);
        mPhone.setFocusableInTouchMode(flg);
        mComment.setFocusableInTouchMode(flg);
        mCheckBox.setEnabled(flg);
        /*
        if (flg){
            setupListener();
        }
        */
    }

    private void updateData(){
        mCallbacks.updateData(mSportsmanModel);
    }



    private void setupListener(){

        mFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mSportsmanModel.setName(charSequence.toString());
                updateData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mSportsmanModel.setTel(charSequence.toString());
                updateData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mSportsmanModel.setComment(charSequence.toString());
                updateData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    View.OnClickListener mCallListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPhone.getText().toString().length()!=0){
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mPhone.getText()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        }
    };

    View.OnClickListener mSendSMSListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPhone.getText().toString().length()!=0){
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"+mPhone.getText()));
                startActivity(smsIntent);
            }
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mSportsmanModel.setActive(!b);
    }

    public interface Callbacks{
        void updateData(SportsmanModel model);
    }
}
