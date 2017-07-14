package cav.pdst.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cav.pdst.R;


public class SpTrainingFragment extends Fragment {

    public static SpTrainingFragment newInstanse(){
        SpTrainingFragment fragment = new SpTrainingFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sp_training, container, false);
        return rootView;
       // return super.onCreateView(inflater, container, savedInstanceState);
    }
}
