package com.rainjm.droneapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rainjm.droneapp.R;

/**
 * Created by stone on 11/11/17.
 */

public class PIDFragment extends Fragment {
    private View view;

    public PIDFragment()
    {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_pid,container,false);

        return view;
    }
}