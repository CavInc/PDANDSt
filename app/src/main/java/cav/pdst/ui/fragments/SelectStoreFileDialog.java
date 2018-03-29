package cav.pdst.ui.fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import cav.pdst.R;

public class SelectStoreFileDialog extends DialogFragment {

    private final ArrayList<String> fname;
    private ListView mListView;


    public static SelectStoreFileDialog  newInctance(File[] files){
        Bundle args = new Bundle();
        args.putParcelableArray("FILES", (Parcelable[]) files);
        SelectStoreFileDialog dialog = new SelectStoreFileDialog();
        dialog.setArguments(args);
        return dialog;
    }

    public SelectStoreFileDialog (){
        fname = null;
        if (getArguments() != null){
            File[] files = (File[]) getArguments().getParcelableArray("FILES");
            for (File f:files){
             fname.add(f.getName());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.selectstore_dialog,container,false);
        mListView = (ListView) v.findViewById(R.id.ssf_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_2, fname);
        mListView.setAdapter(adapter);
        return v;
    }

}