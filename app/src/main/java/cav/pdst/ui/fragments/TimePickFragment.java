package cav.pdst.ui.fragments;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

//http://androiddocs.ru/vidzhet-vybora-vremeni-timepicker/
public class TimePickFragment extends DialogFragment  implements TimePickerDialog.OnTimeSetListener{


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // создаем TimePickerDialog и возвращаем его
        Dialog picker = new TimePickerDialog(getActivity(), this, hour, minute, true);
        //picker.setTitle(getResources().getString(R.string.choose_time));
        return picker;
    }

    @Override
    public void onStart() {
        super.onStart();
        // добавляем кастомный текст для кнопки
        //Button nButton =  ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        //nButton.setText(getResources().getString(R.string.ready));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hours, int minute) {

    }
}
