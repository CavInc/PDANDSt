package cav.pdst.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.RateTypeSpinerModel;

public class AddRateTypeDialogFragment extends DialogFragment {

    private static final String IDRATE = "ID_RATE_TYPE";
    private static final String NAME = "NAME_RATE_TYPE";
    private TextView mName;
    private DataManager mDataManager;
    private int mId;
    private String mNameRateType;

    private OnRateTypeChangeListener mListener;

    public interface OnRateTypeChangeListener {
        public void OnRateTypeChange();
    }

    public static AddRateTypeDialogFragment newInstance(RateTypeSpinerModel model){
        Bundle args = new Bundle();
        args.putInt(IDRATE,model.getId());
        args.putString(NAME,model.getName());
        AddRateTypeDialogFragment fragment = new AddRateTypeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AddRateTypeDialogFragment() {
        mDataManager = DataManager.getInstance();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() !=null && getArguments().containsKey(IDRATE) ) {
            mId = getArguments().getInt(IDRATE);
            mNameRateType = getArguments().getString(NAME);
        }

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.add_rate_type_dialog, null);
        mName = (TextView) v.findViewById(R.id.add_rate_type_name);

        if (mNameRateType !=null){
            mName.setText(mNameRateType);
        } else {
            mId = -1;
        }


        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Тип расхода").setView(v)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDataManager.getDB().addUpdateRateType(mId,mName.getText().toString());
                if (mListener!=null){
                    mListener.OnRateTypeChange();
                }
            }
        }).setNegativeButton(R.string.button_cancel,null);

        return builder.create();
    }

    public void setOnRateTypeChangeListener (OnRateTypeChangeListener listner){
        mListener = listner;
    }
}
