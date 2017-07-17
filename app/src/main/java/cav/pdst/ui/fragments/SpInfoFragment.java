package cav.pdst.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import cav.pdst.R;


public class SpInfoFragment extends Fragment {

    private TextView mFullName;
    private TextView mPhone;
    private TextView mComment;

    private Button mCall;
    private Button mSendSMS;

    public static SpInfoFragment newInstance(){
        SpInfoFragment fragment = new SpInfoFragment();
        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sp_info, container, false);

        mFullName = (TextView) rootView.findViewById(R.id.info_full_name);
        mPhone = (TextView) rootView.findViewById(R.id.info_phone);
        mComment = (TextView) rootView.findViewById(R.id.info_comment);

        mFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCall = (Button) rootView.findViewById(R.id.info_call_button);
        mSendSMS = (Button) rootView.findViewById(R.id.info_send_sms);

        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
