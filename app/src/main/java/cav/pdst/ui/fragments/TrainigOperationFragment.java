package cav.pdst.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import cav.pdst.R;

public class TrainigOperationFragment extends DialogFragment{

    private TrainingOperationListener mOperationListener;

    public interface  TrainingOperationListener{
        public void onTrainingClickListener(int witch);
    }


    /*
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOperationListener = (TrainingOperationListener) activity;
    }
    */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Действие")
                .setItems(R.array.training_operation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int witch) {
                        if (mOperationListener!=null) {
                            mOperationListener.onTrainingClickListener(witch);
                        }
                    }
                });
        return builder.create();
    }


    public void setTrainingOperationListener(TrainingOperationListener listener){
        this.mOperationListener = listener;
    }



}
