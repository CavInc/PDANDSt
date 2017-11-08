package cav.pdst.ui.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import cav.pdst.R;


public class TrainigOperationFragment extends DialogFragment{

    private static final String WORKING_MODE = "WORKING_MODE";
    private static final String TRAINING_COUNT = "TRAINING_COUNT";
    private TrainingOperationListener mOperationListener;

    private int wrk_mode;
    private int mTraining;

    public interface  TrainingOperationListener{
        public void onTrainingClickListener(int witch);
    }

    public static TrainigOperationFragment newInstance(int woking_mode,int training){
        Bundle args = new Bundle();
        args.putInt(WORKING_MODE,woking_mode);
        args.putInt(TRAINING_COUNT,training);
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
        mTraining = getArguments().getInt(TRAINING_COUNT);

        int offset = 0;
        int link = (wrk_mode==0? R.array.training_operation_work : R.array.training_operation);
        if (mTraining == 0) {
            link = R.array.training_operation_no_training;
            offset +=1;
        }

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        final int finalOffset = offset;
        builder.setTitle("Действие")
                .setItems(link,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int witch) {
                        if (mOperationListener!=null) {
                            mOperationListener.onTrainingClickListener(witch+ finalOffset);
                        }
                    }
                });

        return builder.create();
    }


    public void setTrainingOperationListener(TrainingOperationListener listener){
        this.mOperationListener = listener;
    }
}
