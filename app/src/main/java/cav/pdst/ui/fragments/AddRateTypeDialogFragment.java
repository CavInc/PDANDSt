package cav.pdst.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;

public class AddRateTypeDialogFragment extends DialogFragment {

    private TextView mName;
    private DataManager mDataManager;

    public AddRateTypeDialogFragment() {
        mDataManager = DataManager.getInstance();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.add_rate_type_dialog, null);
        mName = (TextView) v.findViewById(R.id.add_rate_type_name);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Тип расхода").setView(v)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDataManager.getDB().addUpdateRateType(-1,mName.getText().toString());
            }
        }).setNegativeButton(R.string.button_cancel,null);

        return builder.create();
    }
}
