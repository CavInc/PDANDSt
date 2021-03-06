package cav.pdst.ui.fragments;


import android.app.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import cav.pdst.R;

public class EditDeleteDialog extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "EDDIALOG";


    public interface EditDeleteDialogListener {
        public void onDialogItemClick(int selectItem);
    }

    private EditDeleteDialogListener mListener;

    public static EditDeleteDialog newInstance(){
        Bundle args = new Bundle();
        EditDeleteDialog f= new EditDeleteDialog();
        return f;
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
        if (mListener != null) {
            mListener.onDialogItemClick(id);
        }
        dismiss();

    }

    public void setEditDeleteDialogListener(EditDeleteDialogListener listener){
        this.mListener = listener;
    }

}
