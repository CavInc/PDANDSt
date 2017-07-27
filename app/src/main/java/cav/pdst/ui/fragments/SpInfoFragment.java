package cav.pdst.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import cav.pdst.R;
import cav.pdst.data.models.SportsmanModel;
import cav.pdst.utils.ConstantManager;

@SuppressLint("ValidFragment")
public class SpInfoFragment extends Fragment {

    private TextView mFullName;
    private TextView mPhone;
    private TextView mComment;

    private Button mCall;
    private Button mSendSMS;

    private SportsmanModel mSportsmanModel;

    private Callbacks mCallbacks;
    private int mode;


    public static SpInfoFragment newInstance(SportsmanModel model,int mode){
        Bundle args = new Bundle();
        SpInfoFragment fragment = new SpInfoFragment(model,mode);
        return  fragment;
    }

    public SpInfoFragment(SportsmanModel model,int mode) {
        mSportsmanModel = new SportsmanModel();
        this.mode = mode;
        if ((mode == ConstantManager.EDIT_SPORTSMAN) || (mode == ConstantManager.VIEW_SPORTSMAN)){
            mSportsmanModel = model;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sp_info, container, false);

        mFullName = (TextView) rootView.findViewById(R.id.info_full_name);
        mPhone = (TextView) rootView.findViewById(R.id.info_phone);
        mComment = (TextView) rootView.findViewById(R.id.info_comment);
        mPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        if ((mode == ConstantManager.EDIT_SPORTSMAN) || (mode == ConstantManager.VIEW_SPORTSMAN)) {
            mFullName.setText(mSportsmanModel.getName());
            mPhone.setText(mSportsmanModel.getTel());
            mComment.setText(mSportsmanModel.getComment());
        }
        if (mode == ConstantManager.VIEW_SPORTSMAN) {
            mFullName.setEnabled(false);
            mPhone.setEnabled(false);
            mComment.setEnabled(false);
            mFullName.setFocusable(false);
            mPhone.setFocusable(false);
            mComment.setFocusable(false);
        }else {

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

        mCall = (Button) rootView.findViewById(R.id.info_call_button);
        mSendSMS = (Button) rootView.findViewById(R.id.info_send_sms);

        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void updateData(){
        mCallbacks.updateData(mSportsmanModel);
    }


    public interface Callbacks{
        void updateData(SportsmanModel model);
    }
}
