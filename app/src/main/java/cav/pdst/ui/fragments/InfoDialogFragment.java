package cav.pdst.ui.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class InfoDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Предупреждение")
                .setMessage("Выбранный спортсмен не имеет активного абонемента. Вы хотите добавить новый абонемент ?")
                .setPositiveButton("Добавить",null)
                .setNegativeButton("Отмена",null);
        return builder.create();
    }

}
