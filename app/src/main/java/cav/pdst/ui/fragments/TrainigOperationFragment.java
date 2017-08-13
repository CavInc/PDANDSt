package cav.pdst.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import cav.pdst.R;
import cav.pdst.data.models.TestAbonementModel;

public class TrainigOperationFragment extends DialogFragment{

    private static final String WORKING_MODE = "WORKING_MODE";
    private TrainingOperationListener mOperationListener;

    private int wrk_mode;

    public interface  TrainingOperationListener{
        public void onTrainingClickListener(int witch);
    }

    public static TrainigOperationFragment newInstance(int woking_mode){
        Bundle args = new Bundle();
        args.putInt(WORKING_MODE,woking_mode);
        TrainigOperationFragment dialog = new TrainigOperationFragment();
        dialog.setArguments(args);
        return dialog;
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
        wrk_mode = getArguments().getInt(WORKING_MODE);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Действие")
                .setItems((wrk_mode==0? R.array.training_operation_work : R.array.training_operation),
                        new DialogInterface.OnClickListener() {
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
