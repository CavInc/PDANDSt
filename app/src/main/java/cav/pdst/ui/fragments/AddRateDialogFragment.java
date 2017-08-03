package cav.pdst.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cav.pdst.R;
import cav.pdst.data.managers.DataManager;
import cav.pdst.data.models.RateTypeSpinerModel;
import cav.pdst.utils.ConstantManager;


public class AddRateDialogFragment extends DialogFragment implements View.OnClickListener{

    private DataManager mDataManager;
    private TextView mSumm;
    private Spinner mTypeRate;
    private Button mDateButton;

    private int mIdRateType;

    private AddRateDialogListener mAddRateDialogListener;
    private Date mCreateDate;


    public interface AddRateDialogListener {
        public void OnSelected(int rate_type,String create_date,float summ);
    }

    public AddRateDialogFragment() {
        mDataManager = DataManager.getInstance();
        mCreateDate = new Date();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.add_rate_dialog, null);
        mDateButton = (Button) v.findViewById(R.id.add_rate_data);
        mDateButton.setOnClickListener(this);
        mDateButton.setText(new SimpleDateFormat("E dd.MM.yyyy").format(mCreateDate));

        mSumm = (TextView) v.findViewById(R.id.add_rate_summ);
        mTypeRate = (Spinner) v.findViewById(R.id.add_rate_spiner);

        ArrayList<RateTypeSpinerModel> model = mDataManager.getRateType();
        ArrayAdapter<RateTypeSpinerModel> adapter = new ArrayAdapter<RateTypeSpinerModel>(getActivity(),android.R.layout.simple_spinner_item,model);
        mTypeRate.setAdapter(adapter);

        mTypeRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mIdRateType = ((RateTypeSpinerModel) adapterView.getItemAtPosition(position)).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Добавить расход")
                .setView(v)
                .setNegativeButton(R.string.button_cancel,null)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        mAddRateDialogListener.OnSelected(mIdRateType,
                                format.format(mCreateDate),
                                Float.parseFloat(mSumm.getText().toString()));
                    }
                });

        return builder.create();
    }

    @Override
    public void onClick(View view) {
        DatePickerFragment dialog = DatePickerFragment.newInstance();
        dialog.setOnDateGetListener(new DatePickerFragment.OnDateGetListener() {
            @Override
            public void OnDateGet(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("E dd.MM.yyyy");
                mDateButton.setText(format.format(date));
                mCreateDate = date;
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(), ConstantManager.DIALOG_DATE);
    }


    public void setAddRateDialogListener(AddRateDialogListener listener){
        mAddRateDialogListener = listener;
    }
}
