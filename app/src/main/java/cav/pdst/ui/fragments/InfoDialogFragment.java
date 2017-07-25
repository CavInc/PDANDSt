package cav.pdst.ui.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.DialogInterface.OnClickListener;

public class InfoDialogFragment extends DialogFragment implements OnClickListener{

    private InfoCallback mInfoCallback;



    public interface  InfoCallback{
        public void OnInfoButtonClick(int button_id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mInfoCallback = (InfoCallback) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Предупреждение")
                .setMessage("Выбранный спортсмен не имеет активного абонемента. Вы хотите добавить новый абонемент ?")
                .setPositiveButton("Добавить",this)
                .setNegativeButton("Отмена",this)
                .setCancelable(false);
        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int witch) {
        //TODO изменить потом городушку на один вызов.
        switch (witch){
            case Dialog.BUTTON_POSITIVE:
                mInfoCallback.OnInfoButtonClick(Dialog.BUTTON_POSITIVE);
                break;
            case Dialog.BUTTON_NEGATIVE:
                mInfoCallback.OnInfoButtonClick(Dialog.BUTTON_NEGATIVE);
                break;
        }
        this.dismiss();
    }
}
