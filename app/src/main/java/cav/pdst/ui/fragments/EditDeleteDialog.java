package cav.pdst.ui.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import cav.pdst.R;

public class EditDeleteDialog extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "EDDIALOG";

    public interface EditDeleteDialogListener {
        public void onDialogItemClick(int selectItem);
    }

    private EditDeleteDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"ATTACH");

        mListener = (EditDeleteDialogListener) getActivity();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"ATTACH ACTIVITY");
        mListener = (EditDeleteDialogListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.edit_delete_dialog, null);
        v.findViewById(R.id.dialog_edit_item).setOnClickListener(this);
        v.findViewById(R.id.dialog_del_item).setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setView(v).setTitle("Выбор действия").create();
    }

    @Override
    public void onClick(View view) {
        int id= view.getId();
        Log.d(TAG,String.valueOf(id));
        mListener.onDialogItemClick(id);
        dismiss();

    }

}
