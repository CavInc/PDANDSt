package cav.pdst.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cav.pdst.R;


public class SpInfoFragment extends Fragment {

    public static SpInfoFragment newInstance(){
        SpInfoFragment fragment = new SpInfoFragment();
        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sp_info, container, false);

        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
